/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kelasb23.restoranpbo;

/**
 *
 * @author jeanjacket
 */

import java.sql.*;
import java.util.Map;
        

public class DBConnection {
    static Connection connection;

    public static Connection getConnection() {
        try {
            Map<String, String> env_variables = System.getenv();
            String url = "jdbc:mysql://localhost:3306/restoran_pbo?user=inikasihnamausernya&password=inikasihpasswordnya";
            connection = DriverManager.getConnection(url);
        } catch (SQLException err) {
            System.out.println("Error Connecting to Database");
            System.out.println(err.getMessage());
        }
        return connection;
    }
}
