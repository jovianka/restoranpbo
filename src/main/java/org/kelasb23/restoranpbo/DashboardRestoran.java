/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.kelasb23.restoranpbo;


/**
 *
 * @author jeanjacket
 */
public class DashboardRestoran extends javax.swing.JFrame {


    /**
     * Creates new form DashboardRestoran
     */
    public DashboardRestoran() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main_panel = new javax.swing.JPanel();
        main_navbar = new javax.swing.JPanel();
        main_home_button = new javax.swing.JButton();
        main_keuangan_button = new javax.swing.JButton();
        main_booking_button = new javax.swing.JButton();
        main_cashier_button = new javax.swing.JButton();
        main_content_panel = new javax.swing.JPanel();
        main_dashboard_panel = new javax.swing.JPanel();
        main_dashboard_sidebar = new javax.swing.JPanel();
        main_tambah_menu_button = new javax.swing.JButton();
        main_tambah_user_button = new javax.swing.JButton();
        main_tambah_transaksi_button = new javax.swing.JButton();
        main_tambah_meja_button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        main_panel.setBackground(new java.awt.Color(0, 51, 0));

        main_navbar.setBackground(new java.awt.Color(0, 51, 0));

        main_home_button.setBackground(new java.awt.Color(239, 246, 224));
        main_home_button.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        main_home_button.setText("HOME");
        main_home_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main_home_buttonActionPerformed(evt);
            }
        });

        main_keuangan_button.setBackground(new java.awt.Color(239, 246, 224));
        main_keuangan_button.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        main_keuangan_button.setText("KEUANGAN");
        main_keuangan_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main_keuangan_buttonActionPerformed(evt);
            }
        });

        main_booking_button.setBackground(new java.awt.Color(239, 246, 224));
        main_booking_button.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        main_booking_button.setText("BOOKING");
        main_booking_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main_booking_buttonActionPerformed(evt);
            }
        });

        main_cashier_button.setBackground(new java.awt.Color(239, 246, 224));
        main_cashier_button.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        main_cashier_button.setText("CASHIER");
        main_cashier_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main_cashier_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout main_navbarLayout = new javax.swing.GroupLayout(main_navbar);
        main_navbar.setLayout(main_navbarLayout);
        main_navbarLayout.setHorizontalGroup(
            main_navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_navbarLayout.createSequentialGroup()
                .addComponent(main_home_button, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(main_keuangan_button, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(main_booking_button, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(main_cashier_button, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        main_navbarLayout.setVerticalGroup(
            main_navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(main_home_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(main_keuangan_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(main_booking_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(main_cashier_button, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        main_content_panel.setLayout(new java.awt.CardLayout());

        main_dashboard_sidebar.setBackground(new java.awt.Color(89, 131, 146));
        main_dashboard_sidebar.setToolTipText("");

        main_tambah_menu_button.setBackground(new java.awt.Color(239, 246, 224));
        main_tambah_menu_button.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        main_tambah_menu_button.setText("MENU");
        main_tambah_menu_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main_tambah_menu_buttonActionPerformed(evt);
            }
        });

        main_tambah_user_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_user_button.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        main_tambah_user_button.setText("PEGAWAI");
        main_tambah_user_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main_tambah_user_buttonActionPerformed(evt);
            }
        });

        main_tambah_transaksi_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_transaksi_button.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        main_tambah_transaksi_button.setText("PENGGAJIAN");
        main_tambah_transaksi_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main_tambah_transaksi_buttonActionPerformed(evt);
            }
        });

        main_tambah_meja_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_meja_button.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        main_tambah_meja_button.setText("MEJA");
        main_tambah_meja_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main_tambah_meja_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout main_dashboard_sidebarLayout = new javax.swing.GroupLayout(main_dashboard_sidebar);
        main_dashboard_sidebar.setLayout(main_dashboard_sidebarLayout);
        main_dashboard_sidebarLayout.setHorizontalGroup(
            main_dashboard_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_dashboard_sidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_dashboard_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(main_tambah_meja_button, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(main_tambah_transaksi_button, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(main_tambah_user_button, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(main_tambah_menu_button, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        main_dashboard_sidebarLayout.setVerticalGroup(
            main_dashboard_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_dashboard_sidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(main_tambah_menu_button)
                .addGap(18, 18, 18)
                .addComponent(main_tambah_user_button)
                .addGap(18, 18, 18)
                .addComponent(main_tambah_transaksi_button)
                .addGap(18, 18, 18)
                .addComponent(main_tambah_meja_button)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout main_dashboard_panelLayout = new javax.swing.GroupLayout(main_dashboard_panel);
        main_dashboard_panel.setLayout(main_dashboard_panelLayout);
        main_dashboard_panelLayout.setHorizontalGroup(
            main_dashboard_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_dashboard_panelLayout.createSequentialGroup()
                .addComponent(main_dashboard_sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 824, Short.MAX_VALUE))
        );
        main_dashboard_panelLayout.setVerticalGroup(
            main_dashboard_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_dashboard_sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        main_content_panel.add(main_dashboard_panel, "card2");

        javax.swing.GroupLayout main_panelLayout = new javax.swing.GroupLayout(main_panel);
        main_panel.setLayout(main_panelLayout);
        main_panelLayout.setHorizontalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(main_navbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(main_content_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        main_panelLayout.setVerticalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(main_navbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(main_content_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE))
        );

        main_content_panel.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(main_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(main_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void main_home_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main_home_buttonActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_main_home_buttonActionPerformed

    private void main_keuangan_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main_keuangan_buttonActionPerformed
        PanelKeuangan panel_keuangan = new PanelKeuangan();
        main_content_panel.add(panel_keuangan);
        pack();
        setVisible(true);
    }//GEN-LAST:event_main_keuangan_buttonActionPerformed

    private void main_booking_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main_booking_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_main_booking_buttonActionPerformed

    private void main_cashier_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main_cashier_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_main_cashier_buttonActionPerformed

    private void main_tambah_menu_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main_tambah_menu_buttonActionPerformed

        main_tambah_user_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_transaksi_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_meja_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_menu_button.setBackground(new java.awt.Color(239, 246, 224));

    }//GEN-LAST:event_main_tambah_menu_buttonActionPerformed

    private void main_tambah_user_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main_tambah_user_buttonActionPerformed

        main_tambah_user_button.setBackground(new java.awt.Color(239, 246, 224));
        main_tambah_transaksi_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_meja_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_menu_button.setBackground(new java.awt.Color(150, 149, 146));
    }//GEN-LAST:event_main_tambah_user_buttonActionPerformed

    private void main_tambah_transaksi_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main_tambah_transaksi_buttonActionPerformed

        main_tambah_user_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_transaksi_button.setBackground(new java.awt.Color(239, 246, 224));
        main_tambah_meja_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_menu_button.setBackground(new java.awt.Color(150, 149, 146));

    }//GEN-LAST:event_main_tambah_transaksi_buttonActionPerformed

    private void main_tambah_meja_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_main_tambah_meja_buttonActionPerformed

        main_tambah_user_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_transaksi_button.setBackground(new java.awt.Color(150, 149, 146));
        main_tambah_meja_button.setBackground(new java.awt.Color(239, 246, 224));
        main_tambah_menu_button.setBackground(new java.awt.Color(150, 149, 146));

    }//GEN-LAST:event_main_tambah_meja_buttonActionPerformed

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
            java.util.logging.Logger.getLogger(DashboardRestoran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashboardRestoran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashboardRestoran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashboardRestoran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashboardRestoran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton main_booking_button;
    private javax.swing.JButton main_cashier_button;
    private javax.swing.JPanel main_content_panel;
    private javax.swing.JPanel main_dashboard_panel;
    private javax.swing.JPanel main_dashboard_sidebar;
    private javax.swing.JButton main_home_button;
    private javax.swing.JButton main_keuangan_button;
    private javax.swing.JPanel main_navbar;
    private javax.swing.JPanel main_panel;
    private javax.swing.JButton main_tambah_meja_button;
    private javax.swing.JButton main_tambah_menu_button;
    private javax.swing.JButton main_tambah_transaksi_button;
    private javax.swing.JButton main_tambah_user_button;
    // End of variables declaration//GEN-END:variables
}
