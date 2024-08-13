package ru.otus.repository;

import ru.otus.authentication.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.otus.authentication.Role.*;

public class RoleRepository {
    private final Connection connection;
    private final Statement statement;

    public RoleRepository(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void writeNewClientIntoUserRoleLink(Long userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into user_role_link (id_user, id_role) values (?, ?);");
        ps.setLong(1, userId);
        ps.setLong(2, 2);
        ps.executeUpdate();
    }

    public Set<Role> getRolesByUserId(Long userId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM user_role_link WHERE id_user = (?)");
        ps.setLong(1, userId);
        var resSet = ps.executeQuery();
        List<Long> rolesId = new ArrayList<>();
        while (resSet.next()) {
            Long id = resSet.getLong("id_role");
            rolesId.add(id);
        }
        Set<Role> roles = new HashSet<>();
        for (Long aLong : rolesId) {
            ps = connection.prepareStatement("SELECT * FROM role WHERE id = (?)");
            ps.setLong(1, aLong);
            resSet = ps.executeQuery();
            String roleString = resSet.getString("role");
            if (roleString.equals("ADMIN")) {
                roles.add(ADMIN);
            }
            if (roleString.equals("CLIENT")) {
                roles.add(CLIENT);
            }
            if (roleString.equals("NOT_REGISTERED")) {
                roles.add(NOT_REGISTERED);
            }

        }
        return roles;
    }
}
