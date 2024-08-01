package ru.otus;

import ru.otus.entities.Note;
import ru.otus.entities.User;
import ru.otus.exception.LoginException;
import ru.otus.exception.NoteNotFoundException;
import ru.otus.request.LoginRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class ProjectRepository {
    private final Connection connection;
    private final Statement statement;

    public ProjectRepository() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:D:/ProjectDatabase.db");
        statement = connection.createStatement();
    }

    public List<Note> readAllNotes() throws SQLException {
        var resSet = statement.executeQuery("SELECT * FROM notes");
        List<Note> notes = new ArrayList<>();
        while (resSet.next()) {
            Long id = resSet.getLong("id");
            String title = resSet.getString("title");
            String message = resSet.getString("message");
            notes.add(new Note(id, title, message));
        }
        return notes;
    }

    public Note readNoteById(long id) throws SQLException, NoteNotFoundException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM notes WHERE id = (?)");
        ps.setLong(1, id);
        var resSet = ps.executeQuery();
        String title = resSet.getString("title");
        String message = resSet.getString("message");
        if (title == null && message == null) {
            throw new NoteNotFoundException("Note с данным id отсутствует");
        }
        return new Note(id, title, message);
    }

    public Long writeNewNote(String title, String message) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into notes (title, message) values (?, ?);", RETURN_GENERATED_KEYS);
        ps.setString(1, title);
        ps.setString(2, message);
        ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        return (long) generatedKeys.getInt(1);
    }

    public void patchNoteById(Long id, String title, String message) throws SQLException {
        if (title != null) {
            if (message != null) {
                PreparedStatement ps = connection.prepareStatement("UPDATE notes SET title = (?), message = (?) WHERE id = (?)");
                ps.setString(1, title);
                ps.setString(2, message);
                ps.setLong(3, id);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = connection.prepareStatement("UPDATE notes SET title = (?) WHERE id = (?)");
                ps.setString(1, title);
                ps.setLong(2, id);
                ps.executeUpdate();
            }

        } else {
            PreparedStatement ps = connection.prepareStatement("UPDATE notes SET message = (?) WHERE id = (?)");
            ps.setString(1, message);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public void deleteNoteById(Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM notes WHERE id = (?)");
        ps.setLong(1, id);
        ps.executeUpdate();
    }

    public boolean isSuchUserExist(String email, String nickname) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE email = (?) AND nickname = (?)");
        ps.setString(1, email);
        ps.setString(2,nickname);
        var resSet = ps.executeQuery();
        if (email.equals(resSet.getString("email")) || nickname.equals(resSet.getString("nickname"))) {
            return true;
        }
        return false;
    }

    public void writeNewUser(String nickname, String email, String password) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("insert into users (email, nickname, password) values (?, ?, ?);");
        ps.setString(1, email);
        ps.setString(2, nickname);
        ps.setString(3, password);
        ps.executeUpdate();
    }

    public User getUserByEmailOrNickname(LoginRequest loginRequest, boolean searchByEmail) throws SQLException{
        if (searchByEmail){
            String email = loginRequest.getEmail();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE email = (?)");
            ps.setString(1, email);
            var resSet = ps.executeQuery();
            if (resSet == null){
                throw new LoginException("Неверный email");
            }
            String nickname = resSet.getString("nickname");
            email = resSet.getString("email");
            String password = resSet.getString("password");
            return new User(nickname,email,password);
        }else {
            String nickname = loginRequest.getNickname();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE nickname = (?)");
            ps.setString(1, nickname);
            var resSet = ps.executeQuery();
            if (resSet == null){
                throw new LoginException("Неверный nickname");
            }
            nickname = resSet.getString("nickname");
            String email = resSet.getString("email");
            String password = resSet.getString("password");
            return new User(nickname,email,password);
        }
    }

    public void closeDb() throws SQLException {
        connection.close();
        statement.close();
    }
}
