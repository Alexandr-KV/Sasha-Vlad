package ru.otus.repository;

import ru.otus.entities.Note;
import ru.otus.entities.User;
import ru.otus.exception.NoteNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class NoteRepository {
    private final Connection connection;
    private final Statement statement;


    public NoteRepository(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    public List<Note> readAllNotes(User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM notes WHERE user_id = (?)");
        ps.setLong(1, user.getId());
        var resSet = ps.executeQuery();
        List<Note> notes = new ArrayList<>();
        while (resSet.next()) {
            Long id = resSet.getLong("id");
            String title = resSet.getString("title");
            String message = resSet.getString("message");
            notes.add(new Note(id, title, message));
        }
        return notes;
    }

    public Note readNoteById(long id, User user) throws SQLException, NoteNotFoundException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM notes WHERE id = (?) AND user_id = (?)");
        ps.setLong(1, id);
        ps.setLong(2,user.getId());
        var resSet = ps.executeQuery();
        String title = resSet.getString("title");
        String message = resSet.getString("message");
        if (title == null && message == null) {
            throw new NoteNotFoundException("Note с данным id отсутствует");
        }
        return new Note(id, title, message);
    }

    public Long writeNote(String title, String message, User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into notes (user_id, title, message) values (?, ?, ?);", RETURN_GENERATED_KEYS);
        ps.setLong(1,user.getId());
        ps.setString(2, title);
        ps.setString(3, message);
        ps.executeUpdate();
        ResultSet generatedKeys = ps.getGeneratedKeys();
        return (long) generatedKeys.getInt(1);
    }

    public void patchNoteById(Long id, String title, String message, User user) throws SQLException {
        if (title != null) {
            if (message != null) {
                PreparedStatement ps = connection.prepareStatement("UPDATE notes SET title = (?), message = (?) WHERE id = (?) AND user_id = (?)");
                ps.setString(1, title);
                ps.setString(2, message);
                ps.setLong(3, id);
                ps.setLong(4,user.getId());
                ps.executeUpdate();
            } else {
                PreparedStatement ps = connection.prepareStatement("UPDATE notes SET title = (?) WHERE id = (?) AND user_id = (?)");
                ps.setString(1, title);
                ps.setLong(2, id);
                ps.setLong(3,user.getId());
                ps.executeUpdate();
            }

        } else {
            PreparedStatement ps = connection.prepareStatement("UPDATE notes SET message = (?) WHERE id = (?) AND user_id = (?)");
            ps.setString(1, message);
            ps.setLong(2, id);
            ps.setLong(3,user.getId());
            ps.executeUpdate();
        }
    }

    public void deleteNoteById(Long id, User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM notes WHERE id = (?) AND user_id = (?)");
        ps.setLong(1, id);
        ps.setLong(2, user.getId());
        ps.executeUpdate();
    }

}
