package com.glowbyte.connection;

import org.h2.tools.RunScript;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionFactory {
    private static final String DRIVER = "org.h2.Driver";
    private static final String URL = "jdbc:h2:mem:./test";
    private static final String USER = "ivan";
    private static final String PASSWORD = "";
    private static final String INIT_DB_FILE_PATH = "./db/init.sql";

    public static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName(DRIVER);

            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            RunScript.execute(conn, new FileReader(INIT_DB_FILE_PATH));
        } catch (ClassNotFoundException | FileNotFoundException | SQLException e) {
            System.err.println("Problems with create connection " + e.getMessage());
        }

        return conn;
    }

}
