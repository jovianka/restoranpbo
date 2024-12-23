/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.kelasb23.restoranpbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jeanjacket
 */
public class DataRestoranMenu extends javax.swing.JPanel {

    private DefaultTableModel daftar_menu;
    private DefaultTableModel daftar_resep;
    TableRowSorter<DefaultTableModel> sorter;
    private int selected_row;
    boolean select = false;

    /**
     * Creates new form MenuWrapper
     */
    public DataRestoranMenu() {
        initComponents();

        daftar_menu = new DefaultTableModel();
        daftar_resep = new DefaultTableModel(new String[]{"id", "Nama", "Satuan", "Jumlah"}, 0) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                // Allow editing for the third column (index 2) when in edit mode
                if (columnIndex == 3) {
                    return true;
                }
                return false; // Disable editing for all other columns
            }
        };

        sorter = new TableRowSorter<>(daftar_resep);

        main_tabel_menu.setModel(daftar_menu);
        main_tabel_resep.setModel(daftar_resep);

        main_tabel_resep.setRowSorter(sorter);

        daftar_menu.addColumn("Id");
        daftar_menu.addColumn("Nama");
        daftar_menu.addColumn("Harga");
        daftar_menu.addColumn("Deskripsi");

        main_tabel_resep.getColumnModel().getColumn(0).setMaxWidth(0);
        main_tabel_resep.getColumnModel().getColumn(0).setMinWidth(0);
        main_tabel_resep.getColumnModel().getColumn(0).setPreferredWidth(0);
        main_tabel_resep.getColumnModel().getColumn(0).setWidth(0);

        main_tabel_menu.getColumnModel().getColumn(0).setMaxWidth(0);
        main_tabel_menu.getColumnModel().getColumn(0).setMinWidth(0);
        main_tabel_menu.getColumnModel().getColumn(0).setPreferredWidth(0);
        main_tabel_menu.getColumnModel().getColumn(0).setWidth(0);

        loadData();

        hapus_menu.addActionListener(evt -> {

            if (selected_row <= -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this menu?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {

                deleteFromDatabase();
            }
        });

        tambah_menu.addActionListener(evt -> {
            getInput();
        });

        ubah_menu.addActionListener(evt -> {
            ubah_data();
        });

        main_tabel_menu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Cek jika tidak ada baris yang dipilih
                int selectedRow = main_tabel_menu.getSelectedRow();

                if (selectedRow == -1) {
                    return; // Tidak ada baris yang dipilih
                }

                selected_row = selectedRow;

                // Kosongkan field sebelumnya
                clearFields();

                // Nonaktifkan komponen input
                main_tabel_resep.setEnabled(false);
                field_nama_menu.setEditable(false);  // Disables editing without changing appearance
                JSpinner.NumberEditor editor = (JSpinner.NumberEditor) field_harga_menu.getEditor();
                editor.getTextField().setEditable(false);
                area_deskripsi.setEditable(false);

                tambah_menu.setText("Bersihkan");
                tambah_menu.removeActionListener(tambah_menu.getActionListeners()[0]);
                tambah_menu.addActionListener(evt -> tombol_bersihkan());

                // Ambil data dari baris yang dipilih
                String id = (String) daftar_menu.getValueAt(selectedRow, 0);
                String nama = (String) daftar_menu.getValueAt(selectedRow, 1);
                Integer harga = Integer.parseInt((String) daftar_menu.getValueAt(selectedRow, 2));
                String deskripsi = (String) daftar_menu.getValueAt(selectedRow, 3);

                field_nama_menu.setText(nama);
                field_harga_menu.setValue(harga);
                area_deskripsi.setText(deskripsi);

                // Ambil data tambahan berdasarkan id_menu
                try (Connection c = DBConnection.getConnection()) {
                    String sql = "SELECT inventory.nama, inventory.satuan, resep.jumlah, resep.id_inventory "
                            + "FROM inventory JOIN resep ON inventory.id = resep.id_inventory "
                            + "WHERE resep.id_menu = ? ORDER BY inventory.nama ASC";

                    try (PreparedStatement stmt = c.prepareStatement(sql)) {
                        stmt.setInt(1, Integer.parseInt(id));  // Set parameter id_menu
                        try (ResultSet r = stmt.executeQuery()) {
                            while (r.next()) {
                                // Ambil data dari hasil query dan tambahkan ke dalam tabel
                                Object[] o = new Object[4];
                                o[0] = r.getString("id_inventory");
                                o[1] = r.getString("nama");
                                o[2] = r.getString("satuan");
                                o[3] = r.getString("jumlah");

                                // Tambahkan ke model tabel
                                daftar_resep.addRow(o);
                            }

                            r.close();
                        }
                        stmt.close();
                    }

                    c.close();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Gagal memuat data!", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

            }

        });

        daftar_resep.addTableModelListener(e -> {
            if (e.getColumn() == 3) {
                updateRowOrder();
            }
        });
    }

    ;
           
    
    public void clearFields() {
        field_nama_menu.setText("");
        field_harga_menu.setValue(0);
        area_deskripsi.setText("");
        main_tabel_menu.clearSelection();
        daftar_resep.getDataVector().removeAllElements();
        daftar_resep.fireTableDataChanged();
    }

    public void loadData() {
        daftar_menu.getDataVector().removeAllElements();
        daftar_menu.fireTableDataChanged();
        try {
            Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            String sql = "SELECT * FROM menu ORDER BY nama ASC";
            ResultSet r = s.executeQuery(sql);
            while (r.next()) {
                Object[] o = new Object[4];
                o[0] = r.getString("id");
                o[1] = r.getString("nama");
                o[2] = r.getString("harga");
                o[3] = r.getString("deskripsi");
                daftar_menu.addRow(o);
            }

            r.close();
            s.close();
            c.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        try {
            Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            String sql = "SELECT id, nama, satuan FROM inventory WHERE inventory.jenis_inventory = 'Bahan' ORDER BY nama ASC";
            ResultSet r = s.executeQuery(sql);
            while (r.next()) {
                Object[] o = new Object[4];
                o[0] = r.getString("id");
                o[1] = r.getString("nama");
                o[2] = r.getString("satuan");
                o[3] = "";
                daftar_resep.addRow(o);
            }

            r.close();
            s.close();
            c.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void getInput() {
        String nama = field_nama_menu.getText();
        Integer harga = (Integer) field_harga_menu.getValue();
        String deskripsi = area_deskripsi.getText();

        boolean resep_added = false;

        ArrayList<Integer> jumlah = new ArrayList<>();
        ArrayList<Integer> id = new ArrayList<>();

        int rowCount = main_tabel_resep.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object valueJumlah = main_tabel_resep.getValueAt(i, 3);
            Object valueId = main_tabel_resep.getValueAt(i, 0);

            // Pastikan nilai "Jumlah" tidak null atau kosong sebelum parsing
            if (valueJumlah != null && !valueJumlah.toString().trim().isEmpty()) {
                jumlah.add(Integer.parseInt(valueJumlah.toString()));
            } else {
                continue; // Lewati baris ini jika kosong
            }

            // Pastikan nilai "ID" tidak null atau kosong sebelum parsing
            if (valueId != null && !valueId.toString().trim().isEmpty()) {
                id.add(Integer.parseInt(valueId.toString()));
            }
            resep_added = true;
        }

        if (nama.isEmpty() || harga <= 0 || deskripsi.isEmpty() || !resep_added) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        saveToDatabase(nama, harga, deskripsi, jumlah, id);
    }

    private void updateRowOrder() {
        // Mengambil jumlah baris
        int selectedRow = main_tabel_resep.getSelectedRow();
        if (selectedRow == -1) {
            return; // Tidak ada baris yang dipilih
        }

        // Mendapatkan nilai dari kolom jumlah
        Object jumlahValue = main_tabel_resep.getValueAt(selectedRow, 3);  // Kolom 3 untuk jumlah

        // Jika jumlah diisi, pindahkan baris ke atas
        if (jumlahValue != null && !jumlahValue.toString().isEmpty()) {
            // Menyimpan data baris yang akan dipindahkan
            Object[] rowData = new Object[4];
            rowData[0] = main_tabel_resep.getValueAt(selectedRow, 0); // Id
            rowData[1] = main_tabel_resep.getValueAt(selectedRow, 1); // Nama
            rowData[2] = main_tabel_resep.getValueAt(selectedRow, 2); // Satuan
            rowData[3] = jumlahValue; // Jumlah

            // Menghapus baris lama dan menambahkannya di atas
            daftar_resep.removeRow(selectedRow);
            daftar_resep.insertRow(0, rowData);  // Menambahkan baris di atas
        } // Jika jumlah kosong, kembalikan baris ke urutan semula
        else {
            // Pisahkan baris yang memiliki nilai di kolom jumlah dan yang tidak
            List<Object[]> rowsWithValues = new ArrayList<>();
            List<Object[]> rowsWithoutValues = new ArrayList<>();

            for (int i = 0; i < daftar_resep.getRowCount(); i++) {
                Object jumlah = daftar_resep.getValueAt(i, 3); // Kolom jumlah (kolom 3)
                Object[] rowData = new Object[4];
                rowData[0] = daftar_resep.getValueAt(i, 0); // Id
                rowData[1] = daftar_resep.getValueAt(i, 1); // Nama
                rowData[2] = daftar_resep.getValueAt(i, 2); // Satuan
                rowData[3] = jumlah; // Jumlah

                if (jumlah != null && !jumlah.toString().isEmpty()) {
                    rowsWithValues.add(rowData);
                } else {
                    rowsWithoutValues.add(rowData);
                }
            }

            // Gabungkan rows dengan nilai di jumlah dan tanpa nilai, yang pertama yang memiliki nilai
            List<Object[]> allRows = new ArrayList<>();
            allRows.addAll(rowsWithValues); // Baris dengan nilai di jumlah
            allRows.addAll(rowsWithoutValues); // Baris tanpa nilai di jumlah

            // Urutkan berdasarkan nama untuk baris yang tidak memiliki nilai di jumlah
            rowsWithoutValues.sort((row1, row2) -> {
                String name1 = (String) row1[1];
                String name2 = (String) row2[1];
                return name1.compareTo(name2); // Urutkan berdasarkan nama
            });

            // Gabungkan kembali hasilnya
            allRows.clear();
            allRows.addAll(rowsWithValues); // Baris dengan nilai di jumlah
            allRows.addAll(rowsWithoutValues); // Baris tanpa nilai di jumlah

            // Kosongkan model dan tambahkan ulang baris yang sudah diurutkan
            daftar_resep.setRowCount(0);
            for (Object[] row : allRows) {
                daftar_resep.addRow(row);
            }
        }
    }

    public void saveToDatabase(String nama, Integer harga, String deskripsi, ArrayList<Integer> jumlah, ArrayList<Integer> id) {
        try {
            Connection c = DBConnection.getConnection();
            String sql = "INSERT INTO menu (nama, harga, deskripsi) VALUES (?, ?, ?)";
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setString(3, deskripsi);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            sql = "INSERT INTO resep (id_menu, id_inventory, jumlah) VALUES (?, ?, ?)";
            ps = c.prepareStatement(sql);
            for (int i = 0; i < id.size(); i++) {
                if (rs.next()) {
                    ps.setInt(1, rs.getInt(1));  // Ambil id menu yang baru saja dimasukkan
                }
                ps.setInt(2, id.get(i));
                ps.setInt(3, jumlah.get(i));
                ps.executeUpdate();

            }

            ps.close();
            c.close();

            loadData(); // Refresh the table
            clearFields(); // Clear input fields

            JOptionPane.showMessageDialog(this, "Menu berhasil ditambahkan!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        loadData();
    }

    public void deleteFromDatabase() {
        int selectedRow = selected_row;
        Integer id_menu = Integer.parseInt((String) daftar_menu.getValueAt(selectedRow, 0));
        String nama = field_nama_menu.getText();
        Integer harga = (Integer) field_harga_menu.getValue();
        String deskripsi = area_deskripsi.getText();

        boolean resep_added = false;

        ArrayList<Integer> id = new ArrayList<>();

        int rowCount = main_tabel_resep.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object value = main_tabel_resep.getValueAt(i, 3);
            if (main_tabel_resep.getValueAt(i, 3) != "") {
                value = main_tabel_resep.getValueAt(i, 0);
                id.add(Integer.parseInt(value.toString()));

                resep_added = true;
            }
        }
        try {
            Connection c = DBConnection.getConnection();
            String sql = "DELETE FROM menu WHERE menu.id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id_menu);
            ps.executeUpdate();

            sql = "DELETE FROM resep WHERE resep.id_menu = ? AND resep.id_inventory = ?";
            ps = c.prepareStatement(sql);

            ps = c.prepareStatement(sql);
            for (int i = 0; i < id.size(); i++) {
                ps.setInt(1, id_menu);
                ps.setInt(2, id.get(i));
                ps.executeUpdate();

            }

            ps.close();
            c.close();

            clearFields();

            JOptionPane.showMessageDialog(this, "Menu Berhasil!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData(); // Refresh the table
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Hapus Menu, Silahkan Coba Kembali.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        selected_row = -1;
    }

    public void ubah_data() {
        if (selected_row <= -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        field_nama_menu.setEditable(true);
        main_tabel_resep.setEnabled(true);
        main_tabel_menu.setEnabled(false);
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) field_harga_menu.getEditor();
        editor.getTextField().setEditable(true);
        area_deskripsi.setEditable(true);
        // Get current data from the selected row

        Integer id = Integer.parseInt((String) daftar_menu.getValueAt(selected_row, 0));

        int rowCount = main_tabel_resep.getRowCount();
        ArrayList<Integer> id_bahan = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            id_bahan.add(Integer.parseInt((String) main_tabel_resep.getValueAt(i, 3)));
            Object value = main_tabel_resep.getValueAt(i, 3);
        }

        try (Connection c = DBConnection.getConnection()) {

            String sql = "SELECT inventory.nama, inventory.satuan, inventory.id FROM inventory WHERE inventory.id NOT IN (SELECT resep.id_inventory FROM resep WHERE resep.id_menu = ?) ORDER BY inventory.nama ASC";

            try (PreparedStatement stmt = c.prepareStatement(sql)) {
                // Set the id_menu parameter
                stmt.setInt(1, id);

                // Execute the query
                try (ResultSet r = stmt.executeQuery()) {
                    while (r.next()) {
                        Object[] o = new Object[4];
                        o[0] = r.getString("inventory.id");
                        o[1] = r.getString("nama");
                        o[2] = r.getString("satuan");
                        o[3] = "";

                        // Add to table model (update daftar_menu here)
                        daftar_resep.addRow(o);
                    }

                    r.close();
                }

                stmt.close();
            }

            c.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Menampilkan Data!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        main_tabel_menu.setEnabled(false);
        tambah_menu.setText("Perbarui");
        ubah_menu.setText("Kembali");
        hapus_menu.setEnabled(false);
        ubah_menu.removeActionListener(ubah_menu.getActionListeners()[0]);
        tambah_menu.removeActionListener(tambah_menu.getActionListeners()[0]);
        ubah_menu.addActionListener(updateEvent -> kembali());
        tambah_menu.addActionListener(updateEvent -> perbarui_data(id_bahan, id));
    }

    public void kembali() {
        main_tabel_menu.setEnabled(true);
        tambah_menu.setText("Tambah");
        ubah_menu.setText("Edit");
        hapus_menu.setEnabled(true);
        tambah_menu.removeActionListener(tambah_menu.getActionListeners()[0]);
        ubah_menu.removeActionListener(ubah_menu.getActionListeners()[0]);
        ubah_menu.addActionListener(evt -> ubah_data());
        tambah_menu.addActionListener(evt -> getInput());
        selected_row = -1;
        clearFields();
        loadData();
    }

    public void tombol_bersihkan() {
        tambah_menu.setText("Tambah");
        main_tabel_resep.setEnabled(true);
        field_nama_menu.setEditable(true);  // Disables editing without changing appearance
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) field_harga_menu.getEditor();
        editor.getTextField().setEditable(true);
        area_deskripsi.setEditable(true);
        selected_row = -1;
        tambah_menu.removeActionListener(tambah_menu.getActionListeners()[0]);
        tambah_menu.addActionListener(evt -> getInput());
        clearFields();
        loadData();
    }

    public void perbarui_data(ArrayList<Integer> id_bahan, int id_menu) {
        String nama = field_nama_menu.getText();
        Integer harga = (Integer) field_harga_menu.getValue();
        String deskripsi = area_deskripsi.getText();

        boolean resep_added = false;

        ArrayList<Integer> jumlah = new ArrayList<>();
        ArrayList<Integer> id_bahan_baru = new ArrayList<>();

        int rowCount = main_tabel_resep.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object value = main_tabel_resep.getValueAt(i, 3);
            if (value != null && !value.toString().isEmpty()) {  // Pastikan value tidak null dan tidak kosong
                jumlah.add(Integer.parseInt(value.toString()));  // Parse value
                value = main_tabel_resep.getValueAt(i, 0);
                if (value != null && !value.toString().isEmpty()) {
                    id_bahan_baru.add(Integer.parseInt(value.toString()));  // Parse id_bahan_baru
                }
                resep_added = true;
            }
        }

        if (nama.isEmpty() || harga <= 0 || deskripsi.isEmpty() || !resep_added) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Iterator<Integer> iter = id_bahan.iterator();
        while (iter.hasNext()) {
            Integer id = iter.next();
            if (id_bahan_baru.contains(id)) {
                iter.remove();
            }
        }

        update_database(nama, harga, deskripsi, jumlah, id_bahan_baru, id_bahan, id_menu);
    }

    public void update_database(String nama, Integer harga, String deskripsi, ArrayList<Integer> jumlah, ArrayList<Integer> id_bahan_baru, ArrayList<Integer> id_bahan, int id_menu) {
        try {
            Connection c = DBConnection.getConnection();
            String sql = "UPDATE menu SET menu.nama = ?, menu.harga = ?, menu.deskripsi = ? WHERE menu.id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setInt(2, harga);
            ps.setString(3, deskripsi);
            ps.setInt(4, id_menu);

            ps.executeUpdate();

            sql = "DELETE FROM resep WHERE resep.id_menu = ? AND resep.id_inventory =?";
            ps = c.prepareStatement(sql);
            for (int i = 0; i < id_bahan.size(); i++) {
                ps.setInt(1, id_menu);
                ps.setInt(2, id_bahan.get(i));

                ps.executeUpdate();
            }

            sql = "INSERT INTO resep (id_menu, id_inventory, jumlah) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE jumlah = VALUES(jumlah)";
            ps = c.prepareStatement(sql);
            for (int i = 0; i < id_bahan_baru.size(); i++) {
                ps.setInt(1, id_menu);
                ps.setInt(2, id_bahan_baru.get(i));
                ps.setInt(3, jumlah.get(i));

                ps.executeUpdate();

            }

            ps.close();
            c.close();

            main_tabel_menu.setEnabled(true);
            tambah_menu.setText("Tambah");
            ubah_menu.setText("Edit");
            hapus_menu.setEnabled(true);
            tambah_menu.removeActionListener(tambah_menu.getActionListeners()[0]);
            ubah_menu.removeActionListener(ubah_menu.getActionListeners()[0]);
            ubah_menu.addActionListener(evt -> ubah_data());
            tambah_menu.addActionListener(evt -> getInput());
            selected_row = -1;
            JOptionPane.showMessageDialog(this, "Menu berhasil diubah!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menu_container = new javax.swing.JScrollPane();
        main_tabel_menu = new javax.swing.JTable();
        resep_container = new javax.swing.JScrollPane();
        main_tabel_resep = new javax.swing.JTable();
        field_nama_menu = new javax.swing.JTextField();
        container_deskripsi = new javax.swing.JScrollPane();
        area_deskripsi = new javax.swing.JTextArea();
        label_tabel_resep_menu = new javax.swing.JLabel();
        label_tabel_menu = new javax.swing.JLabel();
        tambah_menu = new javax.swing.JButton();
        hapus_menu = new javax.swing.JButton();
        ubah_menu = new javax.swing.JButton();
        label_field_nama = new javax.swing.JLabel();
        label_field_harga = new javax.swing.JLabel();
        label_field_deskripsi = new javax.swing.JLabel();
        label_tabel_menu1 = new javax.swing.JLabel();
        field_harga_menu = new javax.swing.JSpinner();

        setBackground(new java.awt.Color(255, 255, 255));

        main_tabel_menu.setModel(new javax.swing.table.DefaultTableModel(
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
        main_tabel_menu.setDefaultEditor(Object.class, null);
        menu_container.setViewportView(main_tabel_menu);

        main_tabel_resep.setModel(new javax.swing.table.DefaultTableModel(
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
        resep_container.setViewportView(main_tabel_resep);

        field_nama_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_nama_menuActionPerformed(evt);
            }
        });

        area_deskripsi.setColumns(20);
        area_deskripsi.setRows(5);
        container_deskripsi.setViewportView(area_deskripsi);

        label_tabel_resep_menu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        label_tabel_resep_menu.setText("Resep");

        label_tabel_menu.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        label_tabel_menu.setText("Menu");

        tambah_menu.setText("Tambah");
        tambah_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambah_menu_menuActionPerformed(evt);
            }
        });

        hapus_menu.setText("Hapus");
        hapus_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapus_menu_menuActionPerformed(evt);
            }
        });

        ubah_menu.setText("Ubah");

        label_field_nama.setText("Nama");

        label_field_harga.setText("Harga");

        label_field_deskripsi.setText("Deskripsi");

        label_tabel_menu1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        label_tabel_menu1.setText("DAFTAR MENU");

        field_harga_menu.setToolTipText("");
        field_harga_menu.setInheritsPopupMenu(true);
        field_harga_menu.setName(""); // NOI18N
        JFormattedTextField textField = ((JSpinner.DefaultEditor) field_harga_menu.getEditor()).getTextField(); textField.setHorizontalAlignment(JTextField.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label_tabel_resep_menu)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(tambah_menu)
                                .addGap(64, 64, 64)
                                .addComponent(hapus_menu)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                                .addComponent(ubah_menu))
                            .addComponent(label_field_deskripsi)
                            .addComponent(label_tabel_menu1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label_field_nama)
                                    .addComponent(label_field_harga, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(field_nama_menu, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                                    .addComponent(field_harga_menu)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(container_deskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(resep_container, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(menu_container, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_tabel_menu))
                        .addGap(17, 17, 17))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(label_tabel_menu1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(field_nama_menu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_field_nama)
                    .addComponent(label_tabel_menu, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(field_harga_menu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_field_harga))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label_field_deskripsi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(container_deskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_tabel_resep_menu, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(resep_container, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tambah_menu)
                            .addComponent(hapus_menu)
                            .addComponent(ubah_menu)))
                    .addComponent(menu_container, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(60, 60, 60))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void field_nama_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_nama_menuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_nama_menuActionPerformed

    private void tambah_menu_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambah_menu_menuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tambah_menu_menuActionPerformed

    private void hapus_menu_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapus_menu_menuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hapus_menu_menuActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea area_deskripsi;
    private javax.swing.JScrollPane container_deskripsi;
    private javax.swing.JSpinner field_harga_menu;
    private javax.swing.JTextField field_nama_menu;
    private javax.swing.JButton hapus_menu;
    private javax.swing.JLabel label_field_deskripsi;
    private javax.swing.JLabel label_field_harga;
    private javax.swing.JLabel label_field_nama;
    private javax.swing.JLabel label_tabel_menu;
    private javax.swing.JLabel label_tabel_menu1;
    private javax.swing.JLabel label_tabel_resep_menu;
    private javax.swing.JTable main_tabel_menu;
    private javax.swing.JTable main_tabel_resep;
    private javax.swing.JScrollPane menu_container;
    private javax.swing.JScrollPane resep_container;
    private javax.swing.JButton tambah_menu;
    private javax.swing.JButton ubah_menu;
    // End of variables declaration//GEN-END:variables
}
