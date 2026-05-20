package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    private static final String url = "jdbc:mysql://localhost:3306/CaddiePlusDataBase";
    private static final String utilisateur = "root";
    private static final String mdp = "1234";
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        conn = DriverManager.getConnection(url, utilisateur, mdp);
        return conn;
    }
}
