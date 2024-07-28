package ru.otus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbRepository {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resSet;

    public static void Connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:D:/notes.db");
        statement = connection.createStatement();
    }

    public static List<Note> ReadAllDB() throws SQLException {
        resSet = statement.executeQuery("SELECT * FROM notes");
        List<Note> notes = new ArrayList<>();
        while (resSet.next()) {
            Long id = resSet.getLong("id");
            String title = resSet.getString("title");
            String message = resSet.getString("message");
            notes.add(new Note(id, title, message));
        }
        return notes;
    }

    public static Note ReadDBbyId(long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM notes WHERE id = (?)");
        ps.setLong(1, id);
        resSet = ps.executeQuery();
        String title = resSet.getString("title");
        String message = resSet.getString("message");
        return new Note(id, title, message);
    }

    public static Long WriteNewNoteIntoDB(String title, String message) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("insert into notes (title, message) values (?, ?)");
        ps.setString(1, title);
        ps.setString(2, message);
        ps.executeUpdate();
        resSet = statement.executeQuery("SELECT `id` FROM `notes` ORDER BY `id` DESC LIMIT 1");
        return resSet.getLong("id");
    }

    public static void patchNoteById(Long id, Note note) throws SQLException{
        if (note.getTitle() != null) {
            if (note.getMessage() != null) {
                PreparedStatement ps = connection.prepareStatement("UPDATE notes SET title = (?), message = (?) WHERE id = (?)");
                ps.setString(1, note.getTitle());
                ps.setString(2, note.getMessage());
                ps.setLong(3, id);
                ps.executeUpdate();
            } else {
                PreparedStatement ps = connection.prepareStatement("UPDATE notes SET title = (?) WHERE id = (?)");
                ps.setString(1, note.getTitle());
                ps.setLong(2, id);
                ps.executeUpdate();
            }

        } else {
            PreparedStatement ps = connection.prepareStatement("UPDATE notes SET message = (?) WHERE id = (?)");
            ps.setString(1, note.getMessage());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public static void deleteById(Long id) throws SQLException{
        PreparedStatement ps = connection.prepareStatement("DELETE FROM notes WHERE id = (?)");
        ps.setLong(1,id);
        ps.executeUpdate();
    }

    public static void CloseDB() throws SQLException {
        connection.close();
        statement.close();
        if (resSet != null) {
            resSet.close();
        }
    }
}
