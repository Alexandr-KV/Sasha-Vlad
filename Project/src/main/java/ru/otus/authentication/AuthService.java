package ru.otus.authentication;

import io.javalin.http.Context;
import ru.otus.authentication.Role;
import ru.otus.exception.AuthException;
import ru.otus.repository.RoleRepository;
import ru.otus.repository.UserRepository;
import ru.otus.utils.JwtUtils;

import java.sql.SQLException;
import java.util.Set;

import static ru.otus.authentication.Role.NOT_REGISTERED;

public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    public void handleAccess(Context ctx) throws SQLException{

        var routeRoles = ctx.routeRoles();
        if (routeRoles.contains(NOT_REGISTERED)){
            return;
        }

        var userRoles = getUserRoles(ctx);

        routeRoles.retainAll(userRoles);
        if (!routeRoles.isEmpty()){
            return;
        }

        ctx.status(401);
        throw new AuthException("У тебя нет доступа!");
    }

    private Set<Role> getUserRoles(Context ctx) throws SQLException {
        var token = ctx.header("Authorization");
        if (token == null){
            return Set.of(NOT_REGISTERED);
        }
        String email = jwtUtils.parse(token);
        Long userId = userRepository.getUserIdByEmail(email);
        return roleRepository.getRolesByUserId(userId);
    }
}
