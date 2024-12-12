/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.kelasb23.restoranpbo;

/**
 *
 * @author jeanjacket
 */


import java.sql.*;


public class Restoranpbo {

    public static void main(String[] args) {
        Connection db_connection = DBConnection.getConnection();
        try {
            String sql = "SELECT * FROM jenis_inventory";
            PreparedStatement stmt = db_connection.prepareStatement(sql);
            stmt.executeQuery();
            System.out.println("test!!");
            stmt.close();

        } catch (SQLException err) {
            System.out.println("Error");
            System.out.println(err.getMessage());
        }
    }
}
