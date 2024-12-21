/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kelasb23.restoranpbo;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

/**
 *
 * @author jeanjacket
 */

import java.sql.*;
import java.util.*;

public class DBConnection {
    static Connection connection;

    public static Connection getConnection() {
        try {
            Dotenv dotenv = Dotenv.configure().load();
            String user = dotenv.get("DB_USER");
            String pass = dotenv.get("DB_PASSWORD");
            String url = "jdbc:mysql://localhost:3306/restoran_pbo";
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException err) {
            System.out.println("Error Connecting to Database");
            System.out.println(err.getMessage());
        }
        return connection;
    }
}
