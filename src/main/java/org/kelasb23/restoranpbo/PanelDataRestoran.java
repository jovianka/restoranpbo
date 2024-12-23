/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.kelasb23.restoranpbo;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;
import com.google.common.io.Files;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import static java.time.temporal.TemporalAdjusters.*;
import io.github.cdimascio.dotenv.Dotenv;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.kelasb23.restoranpbo.models.AbsensiPegawai;
import org.kelasb23.restoranpbo.models.User;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.CalendarPanel;
import java.sql.Date;


/**
 *
 * @author jeanjacket
 */
public class PanelDataRestoran extends javax.swing.JPanel {
    private DataRestoranMenu panel_menu = new DataRestoranMenu();
    private DataRestoranMeja panel_meja = new DataRestoranMeja();

    
    private Dotenv dotenv;
    private File input_foto_pegawai;
    private boolean edit_pegawai_mode;
    int edit_pegawai_row;
    private DefaultTableModel model_tabel_pegawai;
    private ArrayList<User> loaded_data_pegawai;
    private Integer selected_row = -1;
    
    
    
    private ArrayList<AbsensiPegawai> get_absensi_pegawai(User user) {
        ArrayList<AbsensiPegawai> absensi_pegawai = new ArrayList<>();
        
        Connection db_connection = DBConnection.getConnection();
        try {
            String sql = "SELECT * FROM absensi_pegawai WHERE id_user = ?";
            PreparedStatement stmt = db_connection.prepareStatement(sql);
            stmt.setInt(1, user.id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AbsensiPegawai absensi = new AbsensiPegawai();
                absensi.id = rs.getInt("id");
                absensi.tanggal = rs.getDate("tanggal").toLocalDate();
                absensi.hadir = rs.getString("hadir");
                absensi_pegawai.add(absensi);
            }
            
            stmt.close();
            
            return absensi_pegawai;

        } catch (SQLException err) {
            System.out.println("Error Loading Absensi Pegawai Data");
            System.out.println(err.getMessage());
        }
        return absensi_pegawai;
    }
    
    private void load_data_pegawai() {
        model_tabel_pegawai.setRowCount(0);

        
        Connection db_connection = DBConnection.getConnection();
        try {
            Statement stmt = db_connection.createStatement();
            String sql = "SELECT * from user;";
            ResultSet rs = stmt.executeQuery(sql);
            this.loaded_data_pegawai.clear();
            
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getString("nama");
                row[1] = rs.getString("gaji_pokok");
                row[2] = rs.getString("no_telp");
                row[3] = rs.getString("role");
                
                User user = new User();
                user.id = rs.getInt("id");
                user.nama = rs.getString("nama");
                user.gaji_pokok = rs.getInt("gaji_pokok");
                user.no_telp = rs.getString("no_telp");
                user.role = rs.getString("role");
                user.foto = rs.getString("foto");
                this.loaded_data_pegawai.add(user);
                
                model_tabel_pegawai.addRow(row);
            }
            model_tabel_pegawai.fireTableDataChanged();
            stmt.close();

        } catch (SQLException err) {
            System.out.println("Error Loading Table Data");
            System.out.println(err.getMessage());
        }
    }
    
    /**
     * Creates new form PanelDataRestoran
     */
    public PanelDataRestoran() {
        initComponents();
        data_restoran_content.add (panel_menu, "menu");   
        data_restoran_content.add(panel_meja, "meja");

        
        dotenv = Dotenv.configure().load();
        input_foto_pegawai = null;
        edit_pegawai_mode = false;
        model_tabel_pegawai = new DefaultTableModel();
        loaded_data_pegawai = new ArrayList<>();
        edit_pegawai_mode = false;
        
        data_restoran_tabel_pegawai.setModel(model_tabel_pegawai);
        this.model_tabel_pegawai.addColumn("Nama");
        this.model_tabel_pegawai.addColumn("Gaji Pokok");
        this.model_tabel_pegawai.addColumn("Nomor Telepon");
        this.model_tabel_pegawai.addColumn("Role");
        
        data_restoran_tabel_pegawai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // CUSTOM LISTENERS
        data_restoran_tabel_pegawai.getSelectionModel().addListSelectionListener( list_selection_event -> {
            int row_index = selected_row = data_restoran_tabel_pegawai.getSelectedRow();
            if (row_index != -1) { // Ensure the user is selecting something
                User pegawai = this.loaded_data_pegawai.get(row_index);

                ArrayList<AbsensiPegawai> absensi_pegawai = this.get_absensi_pegawai(pegawai);
                DatePickerSettings new_calendar_settings = new DatePickerSettings();
                new_calendar_settings.setHighlightPolicy(new HighlightAbsensiPegawai(absensi_pegawai));
                calendar_absensi.setSettings(new_calendar_settings);
                
                String foto_path = pegawai.foto;
                if (foto_path != null && !foto_path.isEmpty()) {
                    ImageIcon foto_pegawai = new ImageIcon(foto_path);
                    Image scaled_image = foto_pegawai.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
                    data_restoran_preview_foto.setIcon(new ImageIcon(scaled_image));
                } else {
                    data_restoran_preview_foto.setIcon(null);
                }
                
                field_nama_pegawai.setText((String)data_restoran_tabel_pegawai.getValueAt(row_index, 0));
                
                
            }
            
            else{
                DatePickerSettings defaultSettings = new DatePickerSettings();
                calendar_absensi.setSettings(defaultSettings); // Reset to default settings
                calendar_absensi.setSelectedDate(null);
                data_restoran_preview_foto.setIcon(null);
            }
            

        });
        
        tambah_penggajian_button.addActionListener(evt->{
            tambah_penggajian();
        });
        
        // END CUSTOM LISTENERS
        
        load_data_pegawai();
    }
    
    public void tambah_penggajian(){
        java.util.Date selected_date = input_tanggal_penggajian.getDate();
        Integer potongan = (Integer) input_potongan_penggajian.getValue();

        
        
        if (selected_row == -1 || potongan < 0 || selected_date == null) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        savePenggajian(selected_date, potongan);
    }
    
    public void clearFieldsPenggajian(){
        field_nama_pegawai.setText("");
        input_tanggal_penggajian.setDate(null);
        input_potongan_penggajian.setValue(0);
        field_metode_penggajian.setSelectedIndex(0);
        data_restoran_tabel_pegawai.clearSelection();
        
    }
    
    public void savePenggajian(java.util.Date selected_date, int potongan){
        try{
            User user = loaded_data_pegawai.get(selected_row);
            Connection c = DBConnection.getConnection();
            ArrayList<AbsensiPegawai> absensi_pegawai = this.get_absensi_pegawai(user);
            // FIRST QUERY
            String sql = """
                         SELECT COUNT(absensi_pegawai.hadir) as jumlah_tidak_hadir
                         FROM absensi_pegawai
                         WHERE absensi_pegawai.hadir NOT LIKE '%Hadir%' AND absensi_pegawai.hadir NOT LIKE "%Sakit%"
                         AND absensi_pegawai.tanggal BETWEEN ? and ?
                         AND absensi_pegawai.id_user = ?
                         """;
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(selected_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with(firstDayOfMonth()))); // FIRST DAY OF SELECTED MONTH
            ps.setDate(2, Date.valueOf(selected_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().with(lastDayOfMonth()))); // LAST DAY OF SELECTED MONTH
            ps.setInt(3, user.id);
            
           
            
            ResultSet rs1 = ps.executeQuery();
            if (rs1.next()) {
                int jumlah_tidak_hadir = rs1.getInt("jumlah_tidak_hadir");
            
                // SECOND QUERY
                sql = "INSERT INTO transaksi (nominal, tanggal, metode_pembayaran, jenis_transaksi) VALUES (?, ?, ?, ?)";
                ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, user.gaji_pokok - potongan - 50000 * jumlah_tidak_hadir);
                ps.setDate(2, new java.sql.Date(selected_date.getTime()));
                ps.setString(3, (String)field_metode_penggajian.getSelectedItem());
                ps.setString(4, "pengeluaran");

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                sql = "INSERT INTO penggajian (potongan, bonus, id_user, id_transaksi) VALUES (?, ?, ?, ?)";
                ps = c.prepareStatement(sql);
                ps.setInt(1, potongan + 50000 * jumlah_tidak_hadir);
                ps.setInt(2, 0);
                ps.setInt(3, user.id);
                if (rs.next()) {
                       ps.setInt(4, rs.getInt(1));  // Ambil id menu yang baru saja dimasukkan
                }
            }
            
            ps.executeUpdate();
            ps.close();
            c.close();
            
             // Refresh the table
            clearFieldsPenggajian(); // Clear input fields
            
            JOptionPane.showMessageDialog(this, "Menu berhasil ditambahkan!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }catch(SQLException e){
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

        data_restoran_content = new javax.swing.JPanel();
        data_restoran_pegawai = new javax.swing.JPanel();
        data_restoran_pegawai_update_label = new javax.swing.JLabel();
        data_restoran_input_nama_pegawai_label = new javax.swing.JLabel();
        data_restoran_input_nama_pegawai = new javax.swing.JTextField();
        data_restoran_input_gaji_pegawai_label = new javax.swing.JLabel();
        data_restoran_input_no_telp_pegawai_label = new javax.swing.JLabel();
        data_restoran_input_no_telp_pegawai = new javax.swing.JTextField();
        data_restoran_input_foto_pegawai = new javax.swing.JButton();
        data_restoran_reset_foto_pegawai = new javax.swing.JButton();
        data_restoran_tambah_pegawai_button = new javax.swing.JButton();
        data_restoran_hapus_pegawai_button = new javax.swing.JButton();
        data_restoran_ubah_pegawai_button = new javax.swing.JButton();
        data_restoran_tabel_pegawai_label = new javax.swing.JLabel();
        container_pegawai = new javax.swing.JScrollPane();
        data_restoran_tabel_pegawai = new javax.swing.JTable();
        data_restoran_input_gaji_pegawai = new javax.swing.JSpinner();
        data_restoran_preview_foto = new javax.swing.JLabel();
        label_catat_penggajian = new javax.swing.JLabel();
        label_foto_pegawai = new javax.swing.JLabel();
        label_absensi = new javax.swing.JLabel();
        field_nama_pegawai = new javax.swing.JTextField();
        label_tanggal_penggajian = new javax.swing.JLabel();
        input_tanggal_penggajian = new com.toedter.calendar.JDateChooser();
        label_nama_pegawai = new javax.swing.JLabel();
        tambah_penggajian_button = new javax.swing.JButton();
        label_potongan_penggajian = new javax.swing.JLabel();
        input_potongan_penggajian = new javax.swing.JSpinner();
        calendar_absensi = new com.github.lgooddatepicker.components.CalendarPanel();
        field_metode_penggajian = new javax.swing.JComboBox<>();
        label_metode_penggajian = new javax.swing.JLabel();
        data_restoran_sidebar = new javax.swing.JPanel();
        data_restoran_nav_menu = new javax.swing.JButton();
        data_restoran_nav_pegawai = new javax.swing.JButton();
        data_restoran_nav_meja = new javax.swing.JButton();

        data_restoran_content.setBackground(new java.awt.Color(255, 255, 255));
        data_restoran_content.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(174, 195, 176), 4));
        data_restoran_content.setPreferredSize(new java.awt.Dimension(1000, 600));
        data_restoran_content.setLayout(new java.awt.CardLayout());

        data_restoran_pegawai.setBackground(new java.awt.Color(255, 255, 255));
        data_restoran_pegawai.setRequestFocusEnabled(false);

        data_restoran_pegawai_update_label.setText("Tambah/Edit Pegawai");
        data_restoran_pegawai_update_label.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        data_restoran_input_nama_pegawai_label.setText("Nama");

        data_restoran_input_nama_pegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_input_nama_pegawaiActionPerformed(evt);
            }
        });

        data_restoran_input_gaji_pegawai_label.setText("Gaji Pokok");

        data_restoran_input_no_telp_pegawai_label.setText("No. Telepon");

        data_restoran_input_no_telp_pegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_input_no_telp_pegawaiActionPerformed(evt);
            }
        });

        data_restoran_input_foto_pegawai.setText("Pilih Foto Pegawai (3x4)");
        data_restoran_input_foto_pegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_input_foto_pegawaiActionPerformed(evt);
            }
        });

        data_restoran_reset_foto_pegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reseticon.png"))); // NOI18N
        data_restoran_reset_foto_pegawai.setBorder(null);
        data_restoran_reset_foto_pegawai.setForeground(new java.awt.Color(255, 255, 255));
        data_restoran_reset_foto_pegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_reset_foto_pegawaiActionPerformed(evt);
            }
        });

        data_restoran_tambah_pegawai_button.setText("Tambah");
        data_restoran_tambah_pegawai_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_tambah_pegawai_buttonActionPerformed(evt);
            }
        });

        data_restoran_hapus_pegawai_button.setText("Hapus");
        data_restoran_hapus_pegawai_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_hapus_pegawai_buttonActionPerformed(evt);
            }
        });

        data_restoran_ubah_pegawai_button.setText("Ubah");
        data_restoran_ubah_pegawai_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_ubah_pegawai_buttonActionPerformed(evt);
            }
        });

        data_restoran_tabel_pegawai_label.setText("Daftar Pegawai");
        data_restoran_tabel_pegawai_label.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        container_pegawai.setToolTipText("");

        data_restoran_tabel_pegawai.setModel(new javax.swing.table.DefaultTableModel(
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
        container_pegawai.setViewportView(data_restoran_tabel_pegawai);

        data_restoran_input_gaji_pegawai.setModel(new javax.swing.SpinnerNumberModel(3000000, 0, 8000000, 100000));

        data_restoran_preview_foto.setForeground(new java.awt.Color(204, 204, 204));
        data_restoran_preview_foto.setLabelFor(data_restoran_input_foto_pegawai);
        data_restoran_preview_foto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        data_restoran_preview_foto.setMaximumSize(new java.awt.Dimension(150, 200));
        data_restoran_preview_foto.setMinimumSize(new java.awt.Dimension(150, 200));
        data_restoran_preview_foto.setPreferredSize(new java.awt.Dimension(150, 200));

        label_catat_penggajian.setText("Catat Penggajian");
        label_catat_penggajian.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        label_foto_pegawai.setText("Foto Pegawai");
        label_foto_pegawai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        label_absensi.setText("Absensi Pegawai");
        label_absensi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        field_nama_pegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_nama_pegawaiActionPerformed(evt);
            }
        });

        label_tanggal_penggajian.setText("Tanggal");

        input_tanggal_penggajian.setIcon(null);

        label_nama_pegawai.setText("Nama");

        tambah_penggajian_button.setText("Catat Penggajian");
        tambah_penggajian_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambah_penggajian_buttonActionPerformed(evt);
            }
        });

        label_potongan_penggajian.setText("Potongan");

        field_metode_penggajian.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Transfer", "Tunai", " " }));

        label_metode_penggajian.setText("Metode");

        javax.swing.GroupLayout data_restoran_pegawaiLayout = new javax.swing.GroupLayout(data_restoran_pegawai);
        data_restoran_pegawai.setLayout(data_restoran_pegawaiLayout);
        data_restoran_pegawaiLayout.setHorizontalGroup(
            data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, data_restoran_pegawaiLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                            .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                                    .addComponent(data_restoran_input_no_telp_pegawai_label)
                                    .addGap(18, 18, 18)
                                    .addComponent(data_restoran_input_no_telp_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                                    .addComponent(data_restoran_input_foto_pegawai)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(data_restoran_reset_foto_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                                    .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(data_restoran_input_gaji_pegawai_label)
                                        .addComponent(data_restoran_input_nama_pegawai_label))
                                    .addGap(27, 27, 27)
                                    .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(data_restoran_input_nama_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(data_restoran_input_gaji_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(data_restoran_tambah_pegawai_button)
                                .addComponent(data_restoran_pegawai_update_label))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                                    .addComponent(label_foto_pegawai)
                                    .addGap(68, 68, 68))
                                .addComponent(data_restoran_preview_foto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(container_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(data_restoran_tabel_pegawai_label)
                    .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                        .addComponent(data_restoran_ubah_pegawai_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(data_restoran_hapus_pegawai_button)))
                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_catat_penggajian))
                    .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_absensi)
                            .addComponent(calendar_absensi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tambah_penggajian_button)))
                    .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_tanggal_penggajian)
                            .addComponent(label_potongan_penggajian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_nama_pegawai)
                            .addComponent(label_metode_penggajian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(field_nama_pegawai)
                            .addComponent(input_tanggal_penggajian, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(input_potongan_penggajian)
                            .addComponent(field_metode_penggajian, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(175, Short.MAX_VALUE))
        );
        data_restoran_pegawaiLayout.setVerticalGroup(
            data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, data_restoran_pegawaiLayout.createSequentialGroup()
                        .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(data_restoran_pegawai_update_label, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_catat_penggajian, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(data_restoran_input_nama_pegawai_label)
                                    .addComponent(data_restoran_input_nama_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(data_restoran_input_gaji_pegawai_label)
                                    .addComponent(data_restoran_input_gaji_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(data_restoran_input_no_telp_pegawai_label)
                                    .addComponent(data_restoran_input_no_telp_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(data_restoran_input_foto_pegawai, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(data_restoran_reset_foto_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(data_restoran_tambah_pegawai_button))
                            .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(field_nama_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label_nama_pegawai))
                                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(label_tanggal_penggajian))
                                    .addGroup(data_restoran_pegawaiLayout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(input_tanggal_penggajian, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(label_potongan_penggajian)
                                    .addComponent(input_potongan_penggajian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(field_metode_penggajian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label_metode_penggajian)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, data_restoran_pegawaiLayout.createSequentialGroup()
                        .addComponent(label_foto_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(data_restoran_preview_foto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tambah_penggajian_button)
                .addGap(18, 18, 18)
                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(data_restoran_tabel_pegawai_label, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_absensi, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(container_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(calendar_absensi, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(data_restoran_pegawaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(data_restoran_ubah_pegawai_button)
                    .addComponent(data_restoran_hapus_pegawai_button))
                .addGap(24, 24, 24))
        );

        data_restoran_content.add(data_restoran_pegawai, "data_restoran_pegawai");

        data_restoran_sidebar.setBackground(new java.awt.Color(89, 131, 146));
        data_restoran_sidebar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        data_restoran_sidebar.setPreferredSize(new java.awt.Dimension(197, 562));
        data_restoran_sidebar.setToolTipText("");

        data_restoran_nav_menu.setText("MENU");
        data_restoran_nav_menu.setBackground(new java.awt.Color(150, 149, 146));
        data_restoran_nav_menu.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        data_restoran_nav_menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_nav_menuActionPerformed(evt);
            }
        });

        data_restoran_nav_pegawai.setText("PEGAWAI");
        data_restoran_nav_pegawai.setBackground(new java.awt.Color(239, 246, 224));
        data_restoran_nav_pegawai.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        data_restoran_nav_pegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_nav_pegawaiActionPerformed(evt);
            }
        });

        data_restoran_nav_meja.setText("MEJA");
        data_restoran_nav_meja.setBackground(new java.awt.Color(150, 149, 146));
        data_restoran_nav_meja.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        data_restoran_nav_meja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                data_restoran_nav_mejaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout data_restoran_sidebarLayout = new javax.swing.GroupLayout(data_restoran_sidebar);
        data_restoran_sidebar.setLayout(data_restoran_sidebarLayout);
        data_restoran_sidebarLayout.setHorizontalGroup(
            data_restoran_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(data_restoran_sidebarLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(data_restoran_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(data_restoran_nav_meja, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(data_restoran_nav_pegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(data_restoran_nav_menu, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        data_restoran_sidebarLayout.setVerticalGroup(
            data_restoran_sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(data_restoran_sidebarLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(data_restoran_nav_pegawai)
                .addGap(18, 18, 18)
                .addComponent(data_restoran_nav_menu)
                .addGap(18, 18, 18)
                .addComponent(data_restoran_nav_meja)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(data_restoran_sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(data_restoran_content, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(data_restoran_sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(data_restoran_content, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    private void data_restoran_nav_menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_nav_menuActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) (data_restoran_content.getLayout());
        cl.show(data_restoran_content, "menu");
        data_restoran_nav_menu.setBackground(new java.awt.Color(239, 246, 224));
        data_restoran_nav_pegawai.setBackground(new java.awt.Color(150, 149, 146));
        data_restoran_nav_meja.setBackground(new java.awt.Color(150, 149, 146));
    }//GEN-LAST:event_data_restoran_nav_menuActionPerformed

    private void data_restoran_nav_pegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_nav_pegawaiActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) (data_restoran_content.getLayout());
        cl.show(data_restoran_content, "data_restoran_pegawai");
        data_restoran_nav_menu.setBackground(new java.awt.Color(150, 149, 146));
        data_restoran_nav_pegawai.setBackground(new java.awt.Color(239, 246, 224));
        data_restoran_nav_meja.setBackground(new java.awt.Color(150, 149, 146));
    }//GEN-LAST:event_data_restoran_nav_pegawaiActionPerformed

    private void data_restoran_nav_mejaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_nav_mejaActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) (data_restoran_content.getLayout());
        cl.show(data_restoran_content, "meja");
        data_restoran_nav_menu.setBackground(new java.awt.Color(150, 149, 146));
        data_restoran_nav_pegawai.setBackground(new java.awt.Color(150, 149, 146));
        data_restoran_nav_meja.setBackground(new java.awt.Color(239, 246, 224));
    }//GEN-LAST:event_data_restoran_nav_mejaActionPerformed

    private void tambah_penggajian_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambah_penggajian_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tambah_penggajian_buttonActionPerformed

    private void field_nama_pegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_nama_pegawaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_nama_pegawaiActionPerformed

    private void data_restoran_ubah_pegawai_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_ubah_pegawai_buttonActionPerformed
        if (data_restoran_tabel_pegawai.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data pegawai yang ingin diubah!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (this.edit_pegawai_mode) {
            data_restoran_ubah_pegawai_button.setText("Ubah");
            try {
                // Update fields
                String input_nama = data_restoran_input_nama_pegawai.getText();
                int input_gaji = (int) data_restoran_input_gaji_pegawai.getValue();
                String input_telp = data_restoran_input_no_telp_pegawai.getText();
                String new_file_path = null;
                User pegawai_being_edited = loaded_data_pegawai.get(this.edit_pegawai_row);

                Connection db_connection = DBConnection.getConnection();
                String sql = "UPDATE user SET nama = ?, gaji_pokok = ?, no_telp = ? WHERE id = ?;";
                PreparedStatement stmt1 = db_connection.prepareStatement(sql);
                stmt1.setString(1, input_nama);
                stmt1.setInt(2, input_gaji);
                stmt1.setString(3, input_telp);
                stmt1.setInt(4, pegawai_being_edited.id);

                stmt1.executeUpdate();
                stmt1.close();

                // Update foto
                db_connection = DBConnection.getConnection();
                sql = "UPDATE user SET foto = ? WHERE id = ?;";
                PreparedStatement stmt2 = db_connection.prepareStatement(sql);
                if (this.input_foto_pegawai != null && this.input_foto_pegawai.getAbsolutePath() != pegawai_being_edited.foto) {
                    try {
                        new_file_path = dotenv.get("TARGET_FOLDER") + UUID.randomUUID().toString() + "." + Files.getFileExtension(input_foto_pegawai.getAbsolutePath());
                        File new_file = new File(new_file_path);
                        Files.copy(input_foto_pegawai, new_file);

                        // Delete foto
                        File foto_to_delete = new File(pegawai_being_edited.foto);
                        foto_to_delete.delete();

                        stmt2.setString(1, new_file_path);
                    } catch (IOException ex) {
                        Logger.getLogger(PanelDataRestoran.class.getName()).log(Level.SEVERE, "ERROR UPLOADING FILE", ex);
                    }
                } else if (this.input_foto_pegawai == null) {
                    File foto_to_delete = new File(pegawai_being_edited.foto);
                    foto_to_delete.delete();
                    stmt2.setNull(1, java.sql.Types.VARCHAR);
                } else if (this.input_foto_pegawai.getAbsolutePath() == pegawai_being_edited.foto) {
                    stmt2.setString(1, pegawai_being_edited.foto);
                }

                stmt2.setInt(2, pegawai_being_edited.id);
                stmt2.executeUpdate();
                stmt2.close();

            } catch (SQLException err) {
                System.out.println("Error updating pegawai");
                System.out.println(err.getMessage());
            } finally {
                this.input_foto_pegawai = null;
                data_restoran_preview_foto.setIcon(null);
                data_restoran_input_foto_pegawai.setText("Pilih Foto Pegawai (3x4)");
                data_restoran_input_nama_pegawai.setText(null);
                data_restoran_input_no_telp_pegawai.setText(null);
                load_data_pegawai();
            }

        } else {
            data_restoran_ubah_pegawai_button.setText("Apply");
            this.edit_pegawai_row = data_restoran_tabel_pegawai.getSelectedRow();

            User pegawai_being_edited = this.loaded_data_pegawai.get(edit_pegawai_row);

            String foto_path = pegawai_being_edited.foto;
            this.input_foto_pegawai = new File(foto_path);
            data_restoran_input_foto_pegawai.setText("previous_image");
            ImageIcon foto_pegawai = new ImageIcon(foto_path);
            Image scaled_image = foto_pegawai.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            data_restoran_preview_foto.setIcon( new ImageIcon(scaled_image));

            data_restoran_input_nama_pegawai.setText(pegawai_being_edited.nama);
            data_restoran_input_no_telp_pegawai.setText(pegawai_being_edited.no_telp);
        }

        data_restoran_tabel_pegawai.setEnabled(!data_restoran_tabel_pegawai.isEnabled());
        data_restoran_hapus_pegawai_button.setEnabled(!data_restoran_hapus_pegawai_button.isEnabled());
        data_restoran_tambah_pegawai_button.setEnabled(!data_restoran_tambah_pegawai_button.isEnabled());
        this.edit_pegawai_mode = !this.edit_pegawai_mode;
    }//GEN-LAST:event_data_restoran_ubah_pegawai_buttonActionPerformed

    private void data_restoran_hapus_pegawai_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_hapus_pegawai_buttonActionPerformed
        // TODO add your handling code here:
        int row_index = data_restoran_tabel_pegawai.getSelectedRow();
        if (row_index == -1) {
            JOptionPane.showMessageDialog(this, "Pilih entry pegawai yang ingin dihapus pada tabel!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmation = JOptionPane.showConfirmDialog(this, "Apakah yakin ingin menghapus pegawai ini?", "Konfirmasi Hapus Pegawai", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            String foto_path = this.loaded_data_pegawai.get(row_index).foto;
            File foto_to_delete = new File(foto_path);
            String no_telp = (String) data_restoran_tabel_pegawai.getValueAt(row_index, 2);

            Connection db_connection = DBConnection.getConnection();
            try {
                String sql = "DELETE FROM user WHERE no_telp = ?;";
                PreparedStatement stmt = db_connection.prepareStatement(sql);
                stmt.setString(1, no_telp);
                stmt.executeUpdate();
                stmt.close();
                if (foto_to_delete.exists() && foto_path != null && !foto_path.isEmpty()) {
                    foto_to_delete.delete();
                }
            } catch (SQLException err) {
                System.out.println("Error Deleting Table Data");
                System.out.println(err.getMessage());
            }

            load_data_pegawai();
        }
    }//GEN-LAST:event_data_restoran_hapus_pegawai_buttonActionPerformed

    private void data_restoran_tambah_pegawai_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_tambah_pegawai_buttonActionPerformed

        // TAMBAH USER (PEGAWAI)
        String input_nama = data_restoran_input_nama_pegawai.getText();
        int input_gaji = (int) data_restoran_input_gaji_pegawai.getValue();
        String input_telp = data_restoran_input_no_telp_pegawai.getText();
        String file_path = null;

        if (input_nama.isEmpty() || input_gaji == 0 || input_telp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon isi semua input!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection db_connection = DBConnection.getConnection();
        try {
            String sql = "INSERT INTO user (nama, gaji_pokok, no_telp, foto, role) VALUES (?, ?, ?, ?, 'pegawai');";
            PreparedStatement stmt = db_connection.prepareStatement(sql);
            stmt.setString(1, input_nama);
            stmt.setInt(2, input_gaji);
            stmt.setString(3, input_telp);
            if (this.input_foto_pegawai != null) {
                try {
                    file_path = dotenv.get("TARGET_FOLDER") + UUID.randomUUID().toString() + "." + Files.getFileExtension(input_foto_pegawai.getAbsolutePath());
                    File new_file = new File(file_path);
                    Files.copy(input_foto_pegawai, new_file);
                    stmt.setString(4, file_path);
                } catch (IOException ex) {
                    Logger.getLogger(PanelDataRestoran.class.getName()).log(Level.SEVERE, "ERROR UPLOADING FILE", ex);
                }
            } else {
                stmt.setString(4, "");
            }
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException err) {
            System.out.println("Error Inserting Table Data");
            System.out.println(err.getMessage());
            JOptionPane.showMessageDialog(this, err.getMessage(), "Error Inserting Data Pegawai", JOptionPane.WARNING_MESSAGE);
        } finally {
            this.input_foto_pegawai = null;
            data_restoran_preview_foto.setIcon(null);
            data_restoran_input_foto_pegawai.setText("Pilih Foto Pegawai (3x4)");
            data_restoran_input_nama_pegawai.setText(null);
            data_restoran_input_no_telp_pegawai.setText(null);
            load_data_pegawai();
        }

    }//GEN-LAST:event_data_restoran_tambah_pegawai_buttonActionPerformed

    private void data_restoran_reset_foto_pegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_reset_foto_pegawaiActionPerformed
        // TODO add your handling code here:
        data_restoran_preview_foto.setIcon(null);
        data_restoran_input_foto_pegawai.setText("Pilih Foto Pegawai (3x4)");
        this.input_foto_pegawai = null;
    }//GEN-LAST:event_data_restoran_reset_foto_pegawaiActionPerformed

    private void data_restoran_input_foto_pegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_input_foto_pegawaiActionPerformed
        // AMBIL FOTO PEGAWAI
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
        int fc_result = fc.showOpenDialog(this);

        if (fc.getSelectedFile() != null && fc_result == JFileChooser.APPROVE_OPTION) {
            this.input_foto_pegawai = fc.getSelectedFile();
            data_restoran_input_foto_pegawai.setText(this.input_foto_pegawai.getName());
            ImageIcon foto_pegawai = new ImageIcon(this.input_foto_pegawai.getAbsolutePath());
            Image scaled_image = foto_pegawai.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            data_restoran_preview_foto.setIcon(new ImageIcon(scaled_image));
        }

        if (fc_result == JFileChooser.CANCEL_OPTION) {
            this.input_foto_pegawai = null;
        }

    }//GEN-LAST:event_data_restoran_input_foto_pegawaiActionPerformed

    private void data_restoran_input_no_telp_pegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_input_no_telp_pegawaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_data_restoran_input_no_telp_pegawaiActionPerformed

    private void data_restoran_input_nama_pegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_data_restoran_input_nama_pegawaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_data_restoran_input_nama_pegawaiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.github.lgooddatepicker.components.CalendarPanel calendar_absensi;
    private javax.swing.JScrollPane container_pegawai;
    private javax.swing.JPanel data_restoran_content;
    private javax.swing.JButton data_restoran_hapus_pegawai_button;
    private javax.swing.JButton data_restoran_input_foto_pegawai;
    private javax.swing.JSpinner data_restoran_input_gaji_pegawai;
    private javax.swing.JLabel data_restoran_input_gaji_pegawai_label;
    private javax.swing.JTextField data_restoran_input_nama_pegawai;
    private javax.swing.JLabel data_restoran_input_nama_pegawai_label;
    private javax.swing.JTextField data_restoran_input_no_telp_pegawai;
    private javax.swing.JLabel data_restoran_input_no_telp_pegawai_label;
    private javax.swing.JButton data_restoran_nav_meja;
    private javax.swing.JButton data_restoran_nav_menu;
    private javax.swing.JButton data_restoran_nav_pegawai;
    private javax.swing.JPanel data_restoran_pegawai;
    private javax.swing.JLabel data_restoran_pegawai_update_label;
    private javax.swing.JLabel data_restoran_preview_foto;
    private javax.swing.JButton data_restoran_reset_foto_pegawai;
    private javax.swing.JPanel data_restoran_sidebar;
    private javax.swing.JTable data_restoran_tabel_pegawai;
    private javax.swing.JLabel data_restoran_tabel_pegawai_label;
    private javax.swing.JButton data_restoran_tambah_pegawai_button;
    private javax.swing.JButton data_restoran_ubah_pegawai_button;
    private javax.swing.JComboBox<String> field_metode_penggajian;
    private javax.swing.JTextField field_nama_pegawai;
    private javax.swing.JSpinner input_potongan_penggajian;
    private com.toedter.calendar.JDateChooser input_tanggal_penggajian;
    private javax.swing.JLabel label_absensi;
    private javax.swing.JLabel label_catat_penggajian;
    private javax.swing.JLabel label_foto_pegawai;
    private javax.swing.JLabel label_metode_penggajian;
    private javax.swing.JLabel label_nama_pegawai;
    private javax.swing.JLabel label_potongan_penggajian;
    private javax.swing.JLabel label_tanggal_penggajian;
    private javax.swing.JButton tambah_penggajian_button;
    // End of variables declaration//GEN-END:variables
}
