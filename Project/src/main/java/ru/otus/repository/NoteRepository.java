package ru.otus.repository;

import ru.otus.entities.Note;
import ru.otus.exception.NoteNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class NoteRepository {
    private final Connection connection;
    private final Statement statement;

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public NoteRepository(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
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

    public Long writeNote(String title, String message) throws SQLException {
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

}
