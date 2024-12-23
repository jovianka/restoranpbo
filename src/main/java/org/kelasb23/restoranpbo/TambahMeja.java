/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.kelasb23.restoranpbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author gedenichotp
 */
public class TambahMeja extends javax.swing.JFrame {

    /**
     * Creates new form TambahMeja
     */
    public TambahMeja() {
        initComponents();
        loadTableData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        data_restoran_meja_add_label = new javax.swing.JLabel();
        data_restoran_input_no_meja_label = new javax.swing.JLabel();
        data_restoran_input_no_meja = new javax.swing.JTextField();
        data_restoran_input_kapasitas_meja_label = new javax.swing.JLabel();
        data_restoran_input_kapasitas_meja = new javax.swing.JTextField();
        data_restoran_tambah_meja_button = new javax.swing.JButton();
        data_restoran_meja_list_label = new javax.swing.JLabel();
        container_meja = new javax.swing.JScrollPane();
        data_restoran_tabel_meja = new javax.swing.JTable();
        data_restoran_hapus_meja_button = new javax.swing.JButton();
        data_restoran_ubah_meja_button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        data_restoran_meja_add_label.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        data_restoran_meja_add_label.setText("Tambah Meja");

        data_restoran_input_no_meja_label.setText("No Meja");

        data_restoran_input_no_meja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_input_no_mejaActionPerformed(evt);
            }
        });

        data_restoran_input_kapasitas_meja_label.setText("Kapasitas");

        data_restoran_input_kapasitas_meja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_input_kapasitas_mejaActionPerformed(evt);
            }
        });

        data_restoran_tambah_meja_button.setText("Tambah");
        data_restoran_tambah_meja_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_tambah_meja_buttonActionPerformed(evt);
            }
        });

        data_restoran_meja_list_label.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        data_restoran_meja_list_label.setText("Daftar Meja");

        data_restoran_tabel_meja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        container_meja.setViewportView(data_restoran_tabel_meja);

        data_restoran_hapus_meja_button.setText("Hapus");
        data_restoran_hapus_meja_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_hapus_meja_buttonActionPerformed(evt);
            }
        });

        data_restoran_ubah_meja_button.setText("Ubah");
        data_restoran_ubah_meja_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_ubah_meja_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(data_restoran_meja_add_label)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(data_restoran_input_no_meja_label)
                            .addComponent(data_restoran_input_kapasitas_meja_label))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(data_restoran_input_kapasitas_meja, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(data_restoran_input_no_meja)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(data_restoran_tambah_meja_button, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(data_restoran_hapus_meja_button, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                    .addComponent(data_restoran_ubah_meja_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(data_restoran_meja_list_label)
                    .addComponent(container_meja, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(data_restoran_meja_add_label, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(data_restoran_meja_list_label, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(data_restoran_input_no_meja_label)
                            .addComponent(data_restoran_input_no_meja, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(data_restoran_input_kapasitas_meja_label)
                            .addComponent(data_restoran_input_kapasitas_meja, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(data_restoran_tambah_meja_button)
                            .addComponent(data_restoran_hapus_meja_button))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(data_restoran_ubah_meja_button))
                    .addComponent(container_meja, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void data_restoran_input_no_mejaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_input_no_mejaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_data_restoran_input_no_mejaActionPerformed

    private void data_restoran_input_kapasitas_mejaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_input_kapasitas_mejaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_data_restoran_input_kapasitas_mejaActionPerformed

    private void data_restoran_tambah_meja_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_tambah_meja_buttonActionPerformed
        // ambil value input
        String noMejaStr = data_restoran_input_no_meja.getText();
        String kapasitasStr = data_restoran_input_kapasitas_meja.getText();

        // memastikan input tidak kosong
        if (noMejaStr.isEmpty() || kapasitasStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // memastikan input berupa int
            int noMeja = Integer.parseInt(noMejaStr);
            int kapasitas = Integer.parseInt(kapasitasStr);

            // SQL Query tambah meja
            String sql = "INSERT INTO meja (no_meja, tersedia, kapasitas) VALUES (?, 1, ?)";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, noMeja);
                pstmt.setInt(2, kapasitas);

                pstmt.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(this, "Meja added successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                data_restoran_input_no_meja.setText("");
                data_restoran_input_kapasitas_meja.setText("");

                loadTableData();

            } catch (SQLException e) {
//                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to add Meja!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Input must be a number!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_data_restoran_tambah_meja_buttonActionPerformed

    private void data_restoran_hapus_meja_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_hapus_meja_buttonActionPerformed
        // ambil value row yang diselect
        int selectedRow = data_restoran_tabel_meja.getSelectedRow();

        // memastikan row tidak kosong
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row to delete!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ambil value id dari row
        String idMeja = data_restoran_tabel_meja.getValueAt(selectedRow, 0).toString();

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this Meja?", "Confirm Delete", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // SQL query hapus meja
            String sql = "DELETE FROM meja WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, idMeja);
                pstmt.executeUpdate();

                javax.swing.JOptionPane.showMessageDialog(this, "Meja deleted successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                loadTableData();

            } catch (SQLException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to delete Meja!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_data_restoran_hapus_meja_buttonActionPerformed

    private void data_restoran_ubah_meja_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_ubah_meja_buttonActionPerformed
        int selectedRow = data_restoran_tabel_meja.getSelectedRow();

        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row to edit!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newNoMejaStr = data_restoran_input_no_meja.getText();
        String newKapasitasStr = data_restoran_input_kapasitas_meja.getText();

        if (newNoMejaStr.isEmpty() || newKapasitasStr.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idMeja = data_restoran_tabel_meja.getValueAt(selectedRow, 0).toString();

        try {
            int newNoMeja = Integer.parseInt(newNoMejaStr);
            int newKapasitas = Integer.parseInt(newKapasitasStr);

            // SQL query ubah meja
            String sql = "UPDATE meja SET kapasitas = ?, no_meja = ? WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, newKapasitas);
                pstmt.setInt(2, newNoMeja);
                pstmt.setString(3, idMeja);

                pstmt.executeUpdate();

                javax.swing.JOptionPane.showMessageDialog(this, "Meja updated successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                data_restoran_input_no_meja.setText("");
                data_restoran_input_kapasitas_meja.setText("");

                loadTableData();

            } catch (SQLException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to update Meja!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Input must be a number!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_data_restoran_ubah_meja_buttonActionPerformed

    private void loadTableData() {
        // set kolom tabel
        String[] columnNames = {"ID", "No Meja", "Tersedia", "Kapasitas"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);
        data_restoran_tabel_meja.setModel(model);

        // SQL query tampilkan meja
        String sql = "SELECT * FROM meja";

        try (Connection conn = DBConnection.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("no_meja"),
                    rs.getBoolean("tersedia"),
                    rs.getInt("kapasitas")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TambahMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TambahMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TambahMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TambahMeja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TambahMeja().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane container_meja;
    private javax.swing.JButton data_restoran_hapus_meja_button;
    private javax.swing.JTextField data_restoran_input_kapasitas_meja;
    private javax.swing.JLabel data_restoran_input_kapasitas_meja_label;
    private javax.swing.JTextField data_restoran_input_no_meja;
    private javax.swing.JLabel data_restoran_input_no_meja_label;
    private javax.swing.JLabel data_restoran_meja_add_label;
    private javax.swing.JLabel data_restoran_meja_list_label;
    private javax.swing.JTable data_restoran_tabel_meja;
    private javax.swing.JButton data_restoran_tambah_meja_button;
    private javax.swing.JButton data_restoran_ubah_meja_button;
    // End of variables declaration//GEN-END:variables
}
