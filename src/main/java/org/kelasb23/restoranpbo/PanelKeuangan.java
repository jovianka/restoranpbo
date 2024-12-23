/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.kelasb23.restoranpbo;

import io.github.cdimascio.dotenv.Dotenv;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author jeanjacket
 */
public class PanelKeuangan extends javax.swing.JPanel {
    private Dotenv dotenv = Dotenv.configure().load();
    private DefaultTableModel model_transaksi = null;
    private DefaultTableModel model_pengeluaran = null;
    private int selected_pendapatan_year = 0, selected_pendapatan_month = 0, selected_pengeluaran_year = 0, selected_pengeluaran_month = 0;
    String selected_starting_date = "", selected_ending_date = "";

    public void update_dashboard() {
        int laba_bersih = 0, pendapatan = 0, pengeluaran = 0;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultPieDataset pieDataset = new DefaultPieDataset();

        try {
            Connection c = DBConnection.getConnection();
            try (Statement s = c.createStatement()) {
                String sql = "SELECT nominal, jenis_transaksi FROM transaksi";
                try (ResultSet r = s.executeQuery(sql)) {
                    while (r.next()) {
                        if ("pemasukan".equals(r.getString("jenis_transaksi"))) {
                            laba_bersih += r.getInt("nominal");
                            pendapatan += r.getInt("nominal");
                        } else {
                            laba_bersih -= r.getInt("nominal");
                            pengeluaran += r.getInt("nominal");
                        }
                    }
                }
            }
//            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            String formatted_pendapatan = decimalFormat.format(pendapatan), formatted_pengeluaran = decimalFormat.format(pengeluaran), formatted_laba_bersih = decimalFormat.format(laba_bersih);
            laba_bersih_text_field.setText("Rp. " + String.valueOf(formatted_laba_bersih));
            pendapatan_text_field.setText("Rp. " + String.valueOf(formatted_pendapatan));
            pengeluaran_text_field.setText("Rp. " + String.valueOf(formatted_pengeluaran));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
            System.out.println(e.getMessage());
        }
        try {
            Connection c = DBConnection.getConnection();
            try (Statement s = c.createStatement()) {
                String sqll = """
                    SELECT tanggal,
                        SUM(CASE WHEN jenis_transaksi = 'pemasukan' THEN nominal ELSE 0 END) AS total_pemasukan, 
                        SUM(CASE WHEN jenis_transaksi = 'pengeluaran' THEN nominal ELSE 0 END) AS total_pengeluaran 
                    FROM transaksi
                    GROUP BY tanggal
                    ORDER BY tanggal
                """;
                try (ResultSet rl = s.executeQuery(sqll)) {
                    while (rl.next()) {
                        String tanggal = rl.getString("tanggal");
                        double totalPemasukan = rl.getDouble("total_pemasukan");
                        double totalPengeluaran = rl.getDouble("total_pengeluaran");
                        dataset.setValue(totalPemasukan, "Pendapatan", tanggal);
                        dataset.setValue(totalPengeluaran, "Pengeluaran", tanggal);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
            System.out.println(e.getMessage());
        }
        try {
            Connection c = DBConnection.getConnection();
            try (Statement s = c.createStatement()) {
                String sqlp = """
                    SELECT 
                        CASE 
                            WHEN jenis_transaksi = 'pemasukan' THEN 'Pendapatan'
                            WHEN jenis_transaksi = 'pengeluaran' THEN 'Pengeluaran'
                        END AS category,
                        SUM(nominal) AS total 
                    FROM transaksi 
                    GROUP BY category
                """;
                try (ResultSet rp = s.executeQuery(sqlp)) {
                    while (rp.next()) {
                        String category = rp.getString("category");
                        double total = rp.getDouble("total");
                        pieDataset.setValue(category, total);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
            System.out.println(e.getMessage());
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "", // Title
                "Date", // X-axis label
                "Amount", // Y-axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Chart orientation
                true, // Include legend
                false, // Tooltips
                false // URLs
        );

        JFreeChart pieChart = ChartFactory.createPieChart(
                "", // Title
                pieDataset, // Dataset
                true, // Legend
                false, // Tooltips
                false // URLs
        );

        CategoryPlot lineCategoryPlot = lineChart.getCategoryPlot();
        lineCategoryPlot.setBackgroundPaint(Color.white);
        lineCategoryPlot.setOutlinePaint(null);

        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(Color.white);
        piePlot.setLabelGenerator(null);
        piePlot.setOutlinePaint(null);

        CategoryAxis xAxis = lineCategoryPlot.getDomainAxis();
        xAxis.setTickLabelsVisible(false);
        xAxis.setAxisLineVisible(true);
        xAxis.setLabelPaint(Color.BLACK);

        ValueAxis yAxis = lineCategoryPlot.getRangeAxis();
        yAxis.setTickLabelsVisible(false);
        yAxis.setAxisLineVisible(true);
        yAxis.setLabelPaint(Color.BLACK);

        piePlot.setSectionPaint("Pendapatan", new Color(6, 214, 160));
        piePlot.setSectionPaint("Pengeluaran", new Color(239, 71, 111));

        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();

        lineRenderer.setSeriesPaint(0, new Color(6, 214, 160));
        lineRenderer.setSeriesPaint(1, new Color(239, 71, 111));

        ChartPanel lineChartPanel = new ChartPanel(lineChart);
        lineChartPanel.setPreferredSize(new Dimension(319, 285));
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(157, 41));
        line_chart_dashboard.setLayout(new BorderLayout());
        line_chart_dashboard.removeAll();
        line_chart_dashboard.add(lineChartPanel, BorderLayout.CENTER);
        line_chart_dashboard.validate();
        line_chart_dashboard.revalidate();
        line_chart_dashboard.repaint();
        pie_chart_dashboard.setLayout(new BorderLayout());
        pie_chart_dashboard.removeAll();
        pie_chart_dashboard.add(pieChartPanel, BorderLayout.CENTER);
        pie_chart_dashboard.validate();
        pie_chart_dashboard.revalidate();
        pie_chart_dashboard.repaint();
    }

    public void update_transaksi() {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        model_transaksi.getDataVector().removeAllElements();
        model_transaksi.fireTableDataChanged();

        int selected_month = transaksi_month.getSelectedIndex();
        String y = transaksi_year.getText(), category;
        int selected_year = parseInt(y);
        double total;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selected_year);
        calendar.set(Calendar.MONTH, selected_month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        java.sql.Date startDate = new java.sql.Date(calendar.getTimeInMillis());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        java.sql.Date endDate = new java.sql.Date(calendar.getTimeInMillis());

        String sql = """
                    SELECT id, nominal, tanggal, metode_pembayaran, 
                    CASE 
                        WHEN jenis_transaksi = 'pemasukan' THEN 'Pendapatan' 
                        WHEN jenis_transaksi = 'pengeluaran' THEN 'Pengeluaran' 
                    END AS category
                    FROM transaksi
                    WHERE tanggal BETWEEN ? AND ?
                    ORDER BY tanggal DESC
                     """;

        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setDate(1, startDate);
            s.setDate(2, endDate);

            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    Object[] o = new Object[5];
                    o[0] = r.getString("id");
                    o[1] = r.getString("nominal");
                    o[2] = r.getString("tanggal");
                    o[3] = r.getString("metode_pembayaran");
                    o[4] = r.getString("category");
                    category = r.getString("category");
                    total = r.getDouble("nominal");
                    pieDataset.setValue(category, total);
                    model_transaksi.addRow(o);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
            System.out.println(e.getMessage());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "", // Title
                pieDataset, // Dataset
                true, // Legend
                false, // Tooltips
                false // URLs
        );

        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(Color.white);
        piePlot.setLabelGenerator(null);
        piePlot.setOutlinePaint(null);

        piePlot.setSectionPaint("Pendapatan", new Color(6, 214, 160));
        piePlot.setSectionPaint("Pengeluaran", new Color(239, 71, 111));

        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(157, 41));

        pie_chart_transaksi.setLayout(new BorderLayout());
        pie_chart_transaksi.removeAll();
        pie_chart_transaksi.add(pieChartPanel, BorderLayout.CENTER);
        pie_chart_transaksi.validate();
        pie_chart_transaksi.revalidate();
        pie_chart_transaksi.repaint();
    }

    public void update_payroll() {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        model_pengeluaran.getDataVector().removeAllElements();
        model_pengeluaran.fireTableDataChanged();

        int selected_month = payroll_month.getSelectedIndex();
        String y = payroll_year.getText(), metode;
        int selected_year = parseInt(y);
        double total;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selected_year);
        calendar.set(Calendar.MONTH, selected_month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        java.sql.Date startDate = new java.sql.Date(calendar.getTimeInMillis());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        java.sql.Date endDate = new java.sql.Date(calendar.getTimeInMillis());

        String sql = """
                    SELECT 
                        user.nama AS nama_pegawai, 
                        user.role, 
                        transaksi.nominal, 
                        penggajian.potongan,
                        transaksi.metode_pembayaran
                    FROM 
                        penggajian
                    JOIN 
                        user ON penggajian.id_user = user.id
                    JOIN 
                        transaksi ON penggajian.id_transaksi = transaksi.id
                    WHERE 
                        transaksi.tanggal BETWEEN ? AND ?
                    """;

        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setDate(1, startDate);
            s.setDate(2, endDate);

            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    Object[] o = new Object[5];
                    o[0] = r.getString("nama_pegawai");
                    o[1] = r.getString("role");
                    o[2] = r.getString("nominal");
                    o[3] = r.getString("potongan");
                    o[4] = r.getString("metode_pembayaran");
                    metode = r.getString("metode_pembayaran");
                    total = r.getDouble("nominal");
                    pieDataset.setValue(metode, total);
                    model_pengeluaran.addRow(o);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
            System.out.println(e.getMessage());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "", // Title
                pieDataset, // Dataset
                true, // Legend
                false, // Tooltips
                false // URLs
        );

        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(Color.white);
        piePlot.setLabelGenerator(null);
        piePlot.setOutlinePaint(null);

        piePlot.setSectionPaint("QRIS", new Color(249, 65, 68));
        piePlot.setSectionPaint("Cash", new Color(144, 190, 109));
        piePlot.setSectionPaint("Credit Card", new Color(39, 125, 161));

        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(157, 41));

        pie_chart_payroll.setLayout(new BorderLayout());
        pie_chart_payroll.removeAll();
        pie_chart_payroll.add(pieChartPanel, BorderLayout.CENTER);
        pie_chart_payroll.validate();
        pie_chart_payroll.revalidate();
        pie_chart_payroll.repaint();
    }

    public void generate_report() {
        PDDocument document = new PDDocument();
        int pendapatan = 0, pengeluaran = 0, laba_bersih = 0, jumlah_pendapatan = 0, jumlah_pengeluaran = 0, table_index_pendapatan = 0, table_index_pengeluaran = 0, nominal;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formattedDate = currentDate.format(formatter);

        java.util.Date sdate = laporan_starting_date.getDate();
        if (sdate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.selected_starting_date = sdf.format(sdate);
        }
        java.util.Date edate = laporan_ending_date.getDate();
        if (edate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.selected_ending_date = sdf.format(edate);
        }

        java.sql.Date sqlStartDate = new java.sql.Date(sdate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(edate.getTime());

        if (this.selected_starting_date.compareTo(this.selected_ending_date) > 0) {
            JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
        }

        String sql = "SELECT nominal, jenis_transaksi FROM transaksi WHERE tanggal BETWEEN ? AND ?";

        try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setDate(1, sqlStartDate);
            s.setDate(2, sqlEndDate);

            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    if ("pemasukan".equals(r.getString("jenis_transaksi"))) {
                        laba_bersih += r.getInt("nominal");
                        pendapatan += r.getInt("nominal");
                        jumlah_pendapatan++;
                    } else {
                        laba_bersih -= r.getInt("nominal");
                        pengeluaran += r.getInt("nominal");
                        jumlah_pengeluaran++;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
            System.out.println(e.getMessage());
        }

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        String formatted_pendapatan = decimalFormat.format(pendapatan), formatted_pengeluaran = decimalFormat.format(pengeluaran), formatted_laba_bersih = decimalFormat.format(laba_bersih);

        try {
            PDPage page1 = new PDPage();
            document.addPage(page1);
            final InputStream bold_font_stream = this.getClass().getResourceAsStream("/FreeSerifBold.ttf");
            final InputStream regular_font_stream = this.getClass().getResourceAsStream("/FreeSerif.ttf");

            PDFont bold_font = PDType0Font.load(document, bold_font_stream);
            PDFont regular_font = PDType0Font.load(document, regular_font_stream);

            float page_width = page1.getMediaBox().getWidth(), title_width, centerX, margin = 50, yStart = 700, yPosition = yStart, tableWidth = 500, cellHeight = 20, cellMargin = 5;
            float[] columnWidthsPendapatan = {100, 100, 100, 100, 100};
            float[] columnWidthsPengeluaran = {100, 100, 100, 100, 100};
            String text, formatted_nominal;

            String[][] tableDataPendapatan = new String[jumlah_pengeluaran + jumlah_pendapatan + 1][5];
            tableDataPendapatan[table_index_pendapatan][0] = "ID";
            tableDataPendapatan[table_index_pendapatan][1] = "Nominal";
            tableDataPendapatan[table_index_pendapatan][2] = "Tanggal";
            tableDataPendapatan[table_index_pendapatan][3] = "Metode";
            tableDataPendapatan[table_index_pendapatan][4] = "Jenis";
            table_index_pendapatan++;

            String[][] tableDataPengeluaran = new String[jumlah_pengeluaran + 1][5];
            tableDataPengeluaran[table_index_pengeluaran][0] = "Nama";
            tableDataPengeluaran[table_index_pengeluaran][1] = "Jabatan";
            tableDataPengeluaran[table_index_pengeluaran][2] = "Nominal";
            tableDataPengeluaran[table_index_pengeluaran][3] = "Potongan";
            tableDataPengeluaran[table_index_pengeluaran][4] = "Metode";
            table_index_pengeluaran++;

            sql = "SELECT id, nominal, tanggal, metode_pembayaran, jenis_transaksi FROM transaksi WHERE tanggal BETWEEN ? AND ? ORDER BY tanggal ASC, nominal DESC, jenis_transaksi ASC, metode_pembayaran ASC";

            try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
                s.setDate(1, sqlStartDate);
                s.setDate(2, sqlEndDate);

                try (ResultSet r = s.executeQuery()) {
                    while (r.next()) {
                        nominal = r.getInt("nominal");
                        formatted_nominal = decimalFormat.format(nominal);
                        tableDataPendapatan[table_index_pendapatan][0] = r.getString("id");
                        tableDataPendapatan[table_index_pendapatan][1] = "Rp. " + formatted_nominal;
                        tableDataPendapatan[table_index_pendapatan][2] = r.getString("tanggal");
                        tableDataPendapatan[table_index_pendapatan][3] = r.getString("metode_pembayaran");
                        tableDataPendapatan[table_index_pendapatan][4] = r.getString("jenis_transaksi");
                        table_index_pendapatan++;
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
                System.out.println(e.getMessage());
            }

            sql = """
                    SELECT 
                        user.nama AS nama_pegawai, 
                        user.role, 
                        transaksi.nominal, 
                        penggajian.potongan,
                        transaksi.metode_pembayaran
                    FROM 
                        penggajian
                    JOIN 
                        user ON penggajian.id_user = user.id
                    JOIN 
                        transaksi ON penggajian.id_transaksi = transaksi.id
                    WHERE 
                        transaksi.tanggal BETWEEN ? AND ?
                    ORDER BY user.role DESC, transaksi.nominal DESC, transaksi.metode_pembayaran ASC 
                    """;

            try (Connection c = DBConnection.getConnection(); PreparedStatement s = c.prepareStatement(sql)) {
                s.setDate(1, sqlStartDate);
                s.setDate(2, sqlEndDate);

                try (ResultSet r = s.executeQuery()) {
                    while (r.next()) {
                        nominal = r.getInt("nominal");
                        formatted_nominal = decimalFormat.format(nominal);
                        tableDataPengeluaran[table_index_pengeluaran][0] = r.getString("nama_pegawai");
                        tableDataPengeluaran[table_index_pengeluaran][1] = r.getString("role");
                        tableDataPengeluaran[table_index_pengeluaran][2] = "Rp. " + formatted_nominal;
                        nominal = r.getInt("potongan");
                        formatted_nominal = decimalFormat.format(nominal);
                        tableDataPengeluaran[table_index_pengeluaran][3] = "Rp. " + formatted_nominal;
                        tableDataPengeluaran[table_index_pengeluaran][4] = r.getString("metode_pembayaran");
                        table_index_pengeluaran++;
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "An error occurred while processing the data.");
                System.out.println(e.getMessage());
            }

            try (PDPageContentStream contentStream1 = new PDPageContentStream(document, page1)) {
                // Start the text
                contentStream1.beginText();
                contentStream1.setFont(bold_font, 20);
                text = "PT PENIKMAT BABI GULING";
                title_width = bold_font.getStringWidth(text) / 1000 * 20;
                centerX = (page_width - title_width) / 2;
                contentStream1.newLineAtOffset(centerX, 750);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Jl. Pan Kenzie 15K No. 20, Ngawi Utara";
                title_width = regular_font.getStringWidth(text) / 1000 * 12;
                centerX = (page_width - title_width) / 2;
                contentStream1.newLineAtOffset(centerX, 730);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Telp/Fax: 021 88994433, email: pan_kenzie@enak.banget";
                title_width = regular_font.getStringWidth(text) / 1000 * 12;
                centerX = (page_width - title_width) / 2;
                contentStream1.newLineAtOffset(centerX, 710);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.moveTo(50, 700);
                contentStream1.lineTo(550, 700);
                contentStream1.stroke();
                contentStream1.moveTo(50, 701);
                contentStream1.lineTo(550, 701);
                contentStream1.stroke();
                contentStream1.moveTo(50, 702);
                contentStream1.lineTo(550, 702);
                contentStream1.stroke();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Ngawi Utara, " + formattedDate;
                title_width = regular_font.getStringWidth(text) / 1000 * 12;
                centerX = (page_width - title_width - 65);
                contentStream1.newLineAtOffset(centerX, 680);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "No              : " + this.selected_starting_date + "/" + this.selected_ending_date;
                contentStream1.newLineAtOffset(55, 665);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Lampiran   : 2";
                contentStream1.newLineAtOffset(55, 650);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Hal             : Arus Kas Per Tanggal " + this.selected_starting_date + " Hingga " + this.selected_ending_date;
                contentStream1.newLineAtOffset(55, 635);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Dengan hormat, ";
                contentStream1.newLineAtOffset(55, 550);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Sehubungan dengan kegiatan operasional PT PENIKMAT BABI GULING pada tanggal " + this.selected_starting_date + ",";
                contentStream1.newLineAtOffset(55, 530);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "hingga tanggal " + this.selected_ending_date + ". Menyampaikan laporan keuangan sebagai berikut:";
                contentStream1.newLineAtOffset(55, 510);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(bold_font, 12);
                text = "Pendapatan   : Rp. " + formatted_pendapatan;
                contentStream1.newLineAtOffset(90, 465);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(bold_font, 12);
                text = "Pengeluaran  : Rp. " + formatted_pengeluaran;
                contentStream1.newLineAtOffset(90, 445);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(bold_font, 12);
                text = "———————————————————  -";
                contentStream1.newLineAtOffset(90, 433);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(bold_font, 12);
                text = "Laba Bersih  : Rp. " + formatted_laba_bersih;
                contentStream1.newLineAtOffset(90, 420);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Kami berharap laporan keuangan ini, dapat memberikan gambaran yang jelas tentang kondisi keuangan";
                contentStream1.newLineAtOffset(55, 370);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "perusahaan PT PENIKMAT BABI GULING pada tanggal " + this.selected_starting_date + " hingga " + this.selected_ending_date + ". Adapun";
                contentStream1.newLineAtOffset(55, 350);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "detail rincian transaksi dan penggajian yang dimiliki perusahaan terlampir.";
                contentStream1.newLineAtOffset(55, 330);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Demikian lapooran keuangan ini kami sampaikan. Terima kasih atas perhatian dan kerjasamanya.";
                contentStream1.newLineAtOffset(55, 290);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Hormat kami,";
                title_width = regular_font.getStringWidth(text) / 1000 * 12;
                centerX = (page_width - title_width - 65);
                contentStream1.newLineAtOffset(centerX, 230);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "PT PENIKMAT BABI GULING";
                title_width = regular_font.getStringWidth(text) / 1000 * 12;
                centerX = (page_width - title_width - 65);
                contentStream1.newLineAtOffset(centerX, 210);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.beginText();
                contentStream1.setFont(regular_font, 12);
                text = "Manajemen";
                title_width = regular_font.getStringWidth(text) / 1000 * 12;
                centerX = (page_width - title_width - 65);
                contentStream1.newLineAtOffset(centerX, 110);
                contentStream1.showText(text);
                contentStream1.endText();

                contentStream1.close();
            }

            PDPage page2 = new PDPage();
            document.addPage(page2);

            float pageHeight = page2.getMediaBox().getHeight();
            float maxHeight = pageHeight - 100;

            PDPageContentStream contentStream2 = new PDPageContentStream(document, page2);
            try {
                contentStream2.setFont(bold_font, 12);

                contentStream2.beginText();
                text = "Lampiran 1: Rincian Transaksi";
                contentStream2.newLineAtOffset(55, 740);
                contentStream2.showText(text);
                contentStream2.endText();

                for (int i = 0; i < tableDataPendapatan[0].length; i++) {
                    float xPosition = margin + (i * columnWidthsPendapatan[i]);
                    contentStream2.beginText();
                    contentStream2.newLineAtOffset(xPosition + cellMargin, yPosition);
                    contentStream2.showText(tableDataPendapatan[0][i]);
                    contentStream2.endText();
                }

                yPosition -= cellHeight;
                contentStream2.setFont(regular_font, 12);
                contentStream2.moveTo(50, yPosition - 2);
                contentStream2.lineTo(550, yPosition - 2);
                contentStream2.stroke();
                contentStream2.moveTo(50, yPosition - 3);
                contentStream2.lineTo(550, yPosition - 3);
                contentStream2.stroke();
                yPosition -= cellHeight + 10;

                for (int i = 1; i < tableDataPendapatan.length; i++) {
                    if (yPosition <= 100) {
                        contentStream2.setFont(regular_font, 12);
                        contentStream2.moveTo(50, yPosition - 5);
                        contentStream2.lineTo(550, yPosition - 5);
                        contentStream2.stroke();
                        contentStream2.moveTo(50, yPosition - 6);
                        contentStream2.lineTo(550, yPosition - 6);
                        contentStream2.stroke();
                        PDPage newPage = new PDPage();
                        document.addPage(newPage);
                        contentStream2.close();

                        contentStream2 = new PDPageContentStream(document, newPage);
                        contentStream2.setFont(bold_font, 12);
                        yPosition = pageHeight - 50;

                        for (int j = 0; j < tableDataPendapatan[0].length; j++) {
                            float xPosition = margin + (j * columnWidthsPendapatan[j]);
                            contentStream2.beginText();
                            contentStream2.newLineAtOffset(xPosition + cellMargin, yPosition);
                            contentStream2.showText(tableDataPendapatan[0][j]);
                            contentStream2.endText();
                        }

                        yPosition -= cellHeight;
                        contentStream2.setFont(regular_font, 12);
                        contentStream2.moveTo(50, yPosition - 2);
                        contentStream2.lineTo(550, yPosition - 2);
                        contentStream2.stroke();
                        contentStream2.moveTo(50, yPosition - 3);
                        contentStream2.lineTo(550, yPosition - 3);
                        contentStream2.stroke();
                        yPosition -= cellHeight + 10;
                    }

                    contentStream2.setFont(regular_font, 12);
                    for (int j = 0; j < tableDataPendapatan[i].length; j++) {
                        String cellData = tableDataPendapatan[i][j];
                        if (cellData == null) {
                            cellData = ""; // If null, set to an empty string
                        }
                        float xPosition = margin + (j * columnWidthsPendapatan[j]);
                        contentStream2.beginText();
                        contentStream2.newLineAtOffset(xPosition + cellMargin, yPosition);
                        contentStream2.showText(cellData);
                        contentStream2.endText();
                    }
                    contentStream2.setFont(regular_font, 12);
                    yPosition -= cellHeight;
                }

                contentStream2.setFont(regular_font, 12);
                contentStream2.moveTo(50, yPosition - 2);
                contentStream2.lineTo(550, yPosition - 2);
                contentStream2.stroke();
                contentStream2.moveTo(50, yPosition - 3);
                contentStream2.lineTo(550, yPosition - 3);
                contentStream2.stroke();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    contentStream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            PDPage page3 = new PDPage();
            document.addPage(page3);

            pageHeight = page3.getMediaBox().getHeight();
            maxHeight = pageHeight - 100;

            PDPageContentStream contentStream3 = new PDPageContentStream(document, page3);
            try {
                contentStream3.setFont(bold_font, 12);

                contentStream3.beginText();
                text = "Lampiran 2: Rincian Penggajian";
                contentStream3.newLineAtOffset(55, 740);
                contentStream3.showText(text);
                contentStream3.endText();
                yPosition = 700;

                for (int i = 0; i < tableDataPengeluaran[0].length; i++) {
                    String cellData = tableDataPengeluaran[0][i];
                    if (cellData == null) {
                        cellData = "";
                    }
                    float xPosition = margin + (i * columnWidthsPengeluaran[i]);
                    contentStream3.beginText();
                    contentStream3.newLineAtOffset(xPosition + cellMargin, yPosition);
                    contentStream3.showText(cellData);
                    contentStream3.endText();
                }

                yPosition -= cellHeight;
                contentStream3.setFont(regular_font, 12);
                contentStream3.moveTo(50, yPosition - 2);
                contentStream3.lineTo(550, yPosition - 2);
                contentStream3.stroke();
                contentStream3.moveTo(50, yPosition - 3);
                contentStream3.lineTo(550, yPosition - 3);
                contentStream3.stroke();
                yPosition -= cellHeight + 10;

                for (int i = 1; i < tableDataPengeluaran.length; i++) {
                    if (yPosition <= 100) {
                        contentStream3.setFont(regular_font, 12);
                        contentStream3.moveTo(50, yPosition - 5);
                        contentStream3.lineTo(550, yPosition - 5);
                        contentStream3.stroke();
                        contentStream3.moveTo(50, yPosition - 6);
                        contentStream3.lineTo(550, yPosition - 6);
                        contentStream3.stroke();
                        PDPage newPage = new PDPage();
                        document.addPage(newPage);
                        contentStream3.close();

                        contentStream3 = new PDPageContentStream(document, newPage);
                        contentStream3.setFont(bold_font, 12);
                        yPosition = pageHeight - 50;

                        for (int j = 0; j < tableDataPengeluaran[0].length; j++) {
                            float xPosition = margin + (j * columnWidthsPengeluaran[j]);
                            contentStream3.beginText();
                            contentStream3.newLineAtOffset(xPosition + cellMargin, yPosition);
                            contentStream3.showText(tableDataPengeluaran[0][j]);
                            contentStream3.endText();
                        }

                        yPosition -= cellHeight;
                        contentStream3.setFont(regular_font, 12);
                        contentStream3.moveTo(50, yPosition - 2);
                        contentStream3.lineTo(550, yPosition - 2);
                        contentStream3.stroke();
                        contentStream3.moveTo(50, yPosition - 3);
                        contentStream3.lineTo(550, yPosition - 3);
                        contentStream3.stroke();
                        yPosition -= cellHeight + 10;
                    }

                    contentStream3.setFont(regular_font, 12);
                    for (int j = 0; j < tableDataPengeluaran[i].length; j++) {
                        String cellData = tableDataPengeluaran[i][j];

                        // Check for null or empty values
                        if (cellData == null) {
                            cellData = ""; // If null, set to an empty string
                        }

                        float xPosition = margin + (j * columnWidthsPengeluaran[j]);
                        contentStream3.beginText();
                        contentStream3.newLineAtOffset(xPosition + cellMargin, yPosition);
                        contentStream3.showText(cellData);
                        contentStream3.endText();
                    }
                    contentStream3.setFont(regular_font, 12);
                    yPosition -= cellHeight;
                }

                contentStream3.setFont(regular_font, 12);
                contentStream3.moveTo(50, yPosition - 2);
                contentStream3.lineTo(550, yPosition - 2);
                contentStream3.stroke();
                contentStream3.moveTo(50, yPosition - 3);
                contentStream3.lineTo(550, yPosition - 3);
                contentStream3.stroke();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    contentStream3.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Save the document
            String outputPath = dotenv.get("TARGET_FOLDER") + "laporan_" + this.selected_starting_date + "_" + this.selected_ending_date + ".pdf";
            document.save(outputPath);
            System.out.println("PDF created at: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates new form PanelKeuangan
     */
    public PanelKeuangan() {
        initComponents();

        laba_bersih_text_field.setEditable(false);
        pendapatan_text_field.setEditable(false);
        pengeluaran_text_field.setEditable(false);

        model_transaksi = new DefaultTableModel();
        model_pengeluaran = new DefaultTableModel();

        tabel_keuangan_transaksi.setModel(model_transaksi);
        tabel_keuangan_pengeluaran.setModel(model_pengeluaran);

        TableRowSorter<TableModel> sorter_pendapatan = new TableRowSorter<>(model_transaksi);
        TableRowSorter<TableModel> sorter_pengeluaran = new TableRowSorter<>(model_pengeluaran);
        tabel_keuangan_transaksi.setRowSorter(sorter_pendapatan);
        tabel_keuangan_pengeluaran.setRowSorter(sorter_pengeluaran);

        model_transaksi.addColumn("ID");
        model_transaksi.addColumn("Nominal");
        model_transaksi.addColumn("Tanggal");
        model_transaksi.addColumn("Metode");
        model_transaksi.addColumn("Jenis Pembayaran");

        model_pengeluaran.addColumn("Nama Pegawai");
        model_pengeluaran.addColumn("Role");
        model_pengeluaran.addColumn("Nominal");
        model_pengeluaran.addColumn("Potongan");
        model_pengeluaran.addColumn("Metode");

        sorter_pendapatan.setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(Integer.parseInt(o1), Integer.parseInt(o2));
            }
        });
        sorter_pengeluaran.setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(Integer.parseInt(o1), Integer.parseInt(o2));
            }
        });
        sorter_pendapatan.setComparator(1, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(Integer.parseInt(o1), Integer.parseInt(o2));
            }
        });
        sorter_pengeluaran.setComparator(1, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(Integer.parseInt(o1), Integer.parseInt(o2));
            }
        });

        Calendar calendar = Calendar.getInstance();

        int currentMonth = calendar.get(Calendar.MONTH);
        String currentYear = String.format("%d", calendar.get(Calendar.YEAR));

        payroll_month.setSelectedIndex(currentMonth);
        transaksi_month.setSelectedIndex(currentMonth);
        payroll_year.setText(currentYear);
        transaksi_year.setText(currentYear);

        this.update_dashboard();
        this.update_transaksi();
        this.update_payroll();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        side_bar = new javax.swing.JPanel();
        dashboard_button_side = new javax.swing.JButton();
        laporan_button_edit = new javax.swing.JButton();
        transaksi_button_edit = new javax.swing.JButton();
        payroll_button_edit = new javax.swing.JButton();
        Parent = new javax.swing.JPanel();
        main_content_dashboard = new javax.swing.JPanel();
        header_dashboard = new javax.swing.JLabel();
        laba_bersih_panel_dashboard = new javax.swing.JPanel();
        laba_bersih_text_field = new javax.swing.JTextField();
        laba_bersih_label = new javax.swing.JLabel();
        laba_bersih_icon = new javax.swing.JLabel();
        pendapatan_panel_dashboard = new javax.swing.JPanel();
        pendapatan_text_field = new javax.swing.JTextField();
        pendapatan_label = new javax.swing.JLabel();
        pendapatan_icon = new javax.swing.JLabel();
        pengeluaran_panel_dashboard = new javax.swing.JPanel();
        pengeluaran_text_field = new javax.swing.JTextField();
        pengeluaran_label = new javax.swing.JLabel();
        pengeluaran_icon = new javax.swing.JLabel();
        line_chart_dashboard = new javax.swing.JPanel();
        header2 = new javax.swing.JLabel();
        pie_chart_dashboard = new javax.swing.JPanel();
        header1 = new javax.swing.JLabel();
        main_content_laporan = new javax.swing.JPanel();
        header_laporan = new javax.swing.JLabel();
        create_laporan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        laporan_starting_date = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        laporan_ending_date = new com.toedter.calendar.JDateChooser();
        main_content_transaksi = new javax.swing.JPanel();
        header_transaksi = new javax.swing.JLabel();
        scroll_tabel_keuangan_transaksi = new javax.swing.JScrollPane();
        tabel_keuangan_transaksi = new javax.swing.JTable();
        transaksi_month = new javax.swing.JComboBox<>();
        transaksi_year = new javax.swing.JTextField();
        pie_chart_transaksi = new javax.swing.JPanel();
        header3 = new javax.swing.JLabel();
        main_content_payroll = new javax.swing.JPanel();
        header_payroll = new javax.swing.JLabel();
        scroll_tabel_keuangan_payroll = new javax.swing.JScrollPane();
        tabel_keuangan_pengeluaran = new javax.swing.JTable();
        payroll_year = new javax.swing.JTextField();
        payroll_month = new javax.swing.JComboBox<>();
        pie_chart_payroll = new javax.swing.JPanel();
        header4 = new javax.swing.JLabel();

        side_bar.setBackground(new java.awt.Color(89, 131, 146));
        side_bar.setToolTipText("");

        dashboard_button_side.setBackground(new java.awt.Color(239, 246, 224));
        dashboard_button_side.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        dashboard_button_side.setText("DASHBOARD");
        dashboard_button_side.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboard_button_sideActionPerformed(evt);
            }
        });

        laporan_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        laporan_button_edit.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        laporan_button_edit.setText("LAPORAN");
        laporan_button_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laporan_button_editActionPerformed(evt);
            }
        });

        transaksi_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        transaksi_button_edit.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        transaksi_button_edit.setText("TRANSAKSI");
        transaksi_button_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transaksi_button_editActionPerformed(evt);
            }
        });

        payroll_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        payroll_button_edit.setFont(new java.awt.Font("JetBrainsMono NF ExtraBold", 1, 18)); // NOI18N
        payroll_button_edit.setText("PAYROLL");
        payroll_button_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payroll_button_editActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout side_barLayout = new javax.swing.GroupLayout(side_bar);
        side_bar.setLayout(side_barLayout);
        side_barLayout.setHorizontalGroup(
            side_barLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(side_barLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(side_barLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(payroll_button_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transaksi_button_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(laporan_button_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dashboard_button_side, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        side_barLayout.setVerticalGroup(
            side_barLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(side_barLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(dashboard_button_side)
                .addGap(18, 18, 18)
                .addComponent(laporan_button_edit)
                .addGap(18, 18, 18)
                .addComponent(transaksi_button_edit)
                .addGap(18, 18, 18)
                .addComponent(payroll_button_edit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Parent.setPreferredSize(new java.awt.Dimension(1000, 503));
        Parent.setRequestFocusEnabled(false);
        Parent.setLayout(new java.awt.CardLayout());

        main_content_dashboard.setBackground(new java.awt.Color(255, 255, 255));
        main_content_dashboard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(174, 195, 176), 4));

        header_dashboard.setFont(new java.awt.Font("JetBrainsMono NFM", 1, 36)); // NOI18N
        header_dashboard.setText("Dashboard Keuangan");

        laba_bersih_panel_dashboard.setBackground(new java.awt.Color(255, 209, 102));
        laba_bersih_panel_dashboard.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        laba_bersih_text_field.setBackground(new java.awt.Color(255, 209, 102));
        laba_bersih_text_field.setFont(new java.awt.Font("JetBrainsMono NFP ExtraLight", 0, 17)); // NOI18N
        laba_bersih_text_field.setText("Rp. -");
        laba_bersih_text_field.setBorder(null);
        laba_bersih_text_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laba_bersih_text_fieldActionPerformed(evt);
            }
        });

        laba_bersih_label.setFont(new java.awt.Font("JetBrainsMono NFM Thin", 1, 17)); // NOI18N
        laba_bersih_label.setText("Laba-Bersih");

        laba_bersih_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cashflow.png"))); // NOI18N

        javax.swing.GroupLayout laba_bersih_panel_dashboardLayout = new javax.swing.GroupLayout(laba_bersih_panel_dashboard);
        laba_bersih_panel_dashboard.setLayout(laba_bersih_panel_dashboardLayout);
        laba_bersih_panel_dashboardLayout.setHorizontalGroup(
            laba_bersih_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, laba_bersih_panel_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(laba_bersih_icon)
                .addGap(18, 18, 18)
                .addGroup(laba_bersih_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(laba_bersih_panel_dashboardLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(laba_bersih_label))
                    .addComponent(laba_bersih_text_field))
                .addGap(19, 19, 19))
        );
        laba_bersih_panel_dashboardLayout.setVerticalGroup(
            laba_bersih_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(laba_bersih_panel_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(laba_bersih_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(laba_bersih_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, laba_bersih_panel_dashboardLayout.createSequentialGroup()
                        .addComponent(laba_bersih_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(laba_bersih_text_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pendapatan_panel_dashboard.setBackground(new java.awt.Color(6, 214, 160));
        pendapatan_panel_dashboard.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pendapatan_text_field.setBackground(new java.awt.Color(6, 214, 160));
        pendapatan_text_field.setFont(new java.awt.Font("JetBrainsMono NFP ExtraLight", 0, 17)); // NOI18N
        pendapatan_text_field.setText("Rp. -");
        pendapatan_text_field.setBorder(null);

        pendapatan_label.setFont(new java.awt.Font("JetBrainsMono NFM Thin", 1, 17)); // NOI18N
        pendapatan_label.setText("Pendapatan");

        pendapatan_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cashin.png"))); // NOI18N

        javax.swing.GroupLayout pendapatan_panel_dashboardLayout = new javax.swing.GroupLayout(pendapatan_panel_dashboard);
        pendapatan_panel_dashboard.setLayout(pendapatan_panel_dashboardLayout);
        pendapatan_panel_dashboardLayout.setHorizontalGroup(
            pendapatan_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pendapatan_panel_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pendapatan_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pendapatan_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pendapatan_panel_dashboardLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pendapatan_label))
                    .addComponent(pendapatan_text_field))
                .addGap(19, 19, 19))
        );
        pendapatan_panel_dashboardLayout.setVerticalGroup(
            pendapatan_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pendapatan_panel_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pendapatan_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pendapatan_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pendapatan_panel_dashboardLayout.createSequentialGroup()
                        .addComponent(pendapatan_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pendapatan_text_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pengeluaran_panel_dashboard.setBackground(new java.awt.Color(239, 71, 111));
        pengeluaran_panel_dashboard.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pengeluaran_text_field.setBackground(new java.awt.Color(239, 71, 111));
        pengeluaran_text_field.setFont(new java.awt.Font("JetBrainsMono NFP ExtraLight", 0, 17)); // NOI18N
        pengeluaran_text_field.setText("Rp. -");
        pengeluaran_text_field.setBorder(null);

        pengeluaran_label.setFont(new java.awt.Font("JetBrainsMono NFM Thin", 1, 17)); // NOI18N
        pengeluaran_label.setText("Pengeluaran");

        pengeluaran_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cashout.png"))); // NOI18N

        javax.swing.GroupLayout pengeluaran_panel_dashboardLayout = new javax.swing.GroupLayout(pengeluaran_panel_dashboard);
        pengeluaran_panel_dashboard.setLayout(pengeluaran_panel_dashboardLayout);
        pengeluaran_panel_dashboardLayout.setHorizontalGroup(
            pengeluaran_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pengeluaran_panel_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pengeluaran_icon)
                .addGap(18, 18, 18)
                .addGroup(pengeluaran_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pengeluaran_text_field)
                    .addGroup(pengeluaran_panel_dashboardLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pengeluaran_label)))
                .addGap(19, 19, 19))
        );
        pengeluaran_panel_dashboardLayout.setVerticalGroup(
            pengeluaran_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pengeluaran_panel_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pengeluaran_panel_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pengeluaran_panel_dashboardLayout.createSequentialGroup()
                        .addComponent(pengeluaran_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pengeluaran_text_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pengeluaran_icon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        line_chart_dashboard.setBackground(new java.awt.Color(255, 255, 255));
        line_chart_dashboard.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        header2.setFont(new java.awt.Font("JetBrainsMono NFM Light", 0, 18)); // NOI18N
        header2.setText("Grafik Keuangan");

        javax.swing.GroupLayout line_chart_dashboardLayout = new javax.swing.GroupLayout(line_chart_dashboard);
        line_chart_dashboard.setLayout(line_chart_dashboardLayout);
        line_chart_dashboardLayout.setHorizontalGroup(
            line_chart_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(line_chart_dashboardLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(header2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        line_chart_dashboardLayout.setVerticalGroup(
            line_chart_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(line_chart_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header2)
                .addContainerGap(250, Short.MAX_VALUE))
        );

        pie_chart_dashboard.setBackground(new java.awt.Color(255, 255, 255));
        pie_chart_dashboard.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        header1.setFont(new java.awt.Font("JetBrainsMono NFM Light", 0, 18)); // NOI18N
        header1.setText("Diagram");

        javax.swing.GroupLayout pie_chart_dashboardLayout = new javax.swing.GroupLayout(pie_chart_dashboard);
        pie_chart_dashboard.setLayout(pie_chart_dashboardLayout);
        pie_chart_dashboardLayout.setHorizontalGroup(
            pie_chart_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pie_chart_dashboardLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(header1)
                .addGap(70, 70, 70))
        );
        pie_chart_dashboardLayout.setVerticalGroup(
            pie_chart_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pie_chart_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout main_content_dashboardLayout = new javax.swing.GroupLayout(main_content_dashboard);
        main_content_dashboard.setLayout(main_content_dashboardLayout);
        main_content_dashboardLayout.setHorizontalGroup(
            main_content_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_content_dashboardLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(main_content_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_content_dashboardLayout.createSequentialGroup()
                        .addGroup(main_content_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(line_chart_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(main_content_dashboardLayout.createSequentialGroup()
                                .addComponent(laba_bersih_panel_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pendapatan_panel_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15)
                        .addGroup(main_content_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pengeluaran_panel_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pie_chart_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(header_dashboard))
                .addGap(0, 229, Short.MAX_VALUE))
        );
        main_content_dashboardLayout.setVerticalGroup(
            main_content_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_content_dashboardLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(header_dashboard)
                .addGap(18, 18, 18)
                .addGroup(main_content_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_content_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pengeluaran_panel_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pendapatan_panel_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(laba_bersih_panel_dashboard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(main_content_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pie_chart_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(line_chart_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Parent.add(main_content_dashboard, "card5");

        main_content_laporan.setBackground(new java.awt.Color(255, 255, 255));
        main_content_laporan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(174, 195, 176), 4));

        header_laporan.setFont(new java.awt.Font("JetBrainsMono NFM", 1, 36)); // NOI18N
        header_laporan.setText("Laporan Keuangan");

        create_laporan.setFont(new java.awt.Font("JetBrainsMono NFM", 1, 17)); // NOI18N
        create_laporan.setText("Generate .pdf");
        create_laporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_laporanActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("JetBrainsMono NF", 1, 17)); // NOI18N
        jLabel1.setText("Select Starting Date:");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        laporan_starting_date.setBackground(new java.awt.Color(89, 131, 146));
        laporan_starting_date.setDateFormatString("yyyy-MM-dd");

        jLabel2.setFont(new java.awt.Font("JetBrainsMono NF", 1, 17)); // NOI18N
        jLabel2.setText("Select  Ending  Date:");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        laporan_ending_date.setBackground(new java.awt.Color(89, 131, 146));
        laporan_ending_date.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout main_content_laporanLayout = new javax.swing.GroupLayout(main_content_laporan);
        main_content_laporan.setLayout(main_content_laporanLayout);
        main_content_laporanLayout.setHorizontalGroup(
            main_content_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_content_laporanLayout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addGroup(main_content_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(create_laporan)
                    .addGroup(main_content_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(header_laporan)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_content_laporanLayout.createSequentialGroup()
                            .addGroup(main_content_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(69, 69, 69)
                            .addGroup(main_content_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(laporan_starting_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(laporan_ending_date, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        main_content_laporanLayout.setVerticalGroup(
            main_content_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_content_laporanLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(header_laporan)
                .addGap(105, 105, 105)
                .addGroup(main_content_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(laporan_starting_date, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(30, 30, 30)
                .addGroup(main_content_laporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(laporan_ending_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99)
                .addComponent(create_laporan)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        Parent.add(main_content_laporan, "card5");

        main_content_transaksi.setBackground(new java.awt.Color(255, 255, 255));
        main_content_transaksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(174, 195, 176), 4));

        header_transaksi.setFont(new java.awt.Font("JetBrainsMono NFM", 1, 36)); // NOI18N
        header_transaksi.setText("Transaksi Keuangan");

        scroll_tabel_keuangan_transaksi.setBackground(new java.awt.Color(204, 204, 204));
        scroll_tabel_keuangan_transaksi.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabel_keuangan_transaksi.setFont(new java.awt.Font("Helvetica", 0, 17)); // NOI18N
        tabel_keuangan_transaksi.setModel(new javax.swing.table.DefaultTableModel(
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
        scroll_tabel_keuangan_transaksi.setViewportView(tabel_keuangan_transaksi);

        transaksi_month.setFont(new java.awt.Font("JetBrainsMono NFM SemiBold", 0, 14)); // NOI18N
        transaksi_month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));
        transaksi_month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transaksi_monthActionPerformed(evt);
            }
        });

        transaksi_year.setFont(new java.awt.Font("JetBrainsMono NFM SemiBold", 0, 14)); // NOI18N
        transaksi_year.setText("2024");
        transaksi_year.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transaksi_yearActionPerformed(evt);
            }
        });

        pie_chart_transaksi.setBackground(new java.awt.Color(255, 255, 255));
        pie_chart_transaksi.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        header3.setFont(new java.awt.Font("JetBrainsMono NFM Light", 0, 18)); // NOI18N
        header3.setText("Diagram");

        javax.swing.GroupLayout pie_chart_transaksiLayout = new javax.swing.GroupLayout(pie_chart_transaksi);
        pie_chart_transaksi.setLayout(pie_chart_transaksiLayout);
        pie_chart_transaksiLayout.setHorizontalGroup(
            pie_chart_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pie_chart_transaksiLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(header3)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        pie_chart_transaksiLayout.setVerticalGroup(
            pie_chart_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pie_chart_transaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout main_content_transaksiLayout = new javax.swing.GroupLayout(main_content_transaksi);
        main_content_transaksi.setLayout(main_content_transaksiLayout);
        main_content_transaksiLayout.setHorizontalGroup(
            main_content_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_content_transaksiLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(main_content_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_content_transaksiLayout.createSequentialGroup()
                        .addComponent(header_transaksi)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_content_transaksiLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(transaksi_month, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transaksi_year, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_content_transaksiLayout.createSequentialGroup()
                        .addComponent(pie_chart_transaksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(scroll_tabel_keuangan_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(48, 48, 48))
        );
        main_content_transaksiLayout.setVerticalGroup(
            main_content_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_content_transaksiLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(header_transaksi)
                .addGap(26, 26, 26)
                .addGroup(main_content_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(transaksi_month)
                    .addComponent(transaksi_year, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(main_content_transaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pie_chart_transaksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scroll_tabel_keuangan_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        Parent.add(main_content_transaksi, "card4");

        main_content_payroll.setBackground(new java.awt.Color(255, 255, 255));
        main_content_payroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(174, 195, 176), 4));

        header_payroll.setFont(new java.awt.Font("JetBrainsMono NFM", 1, 36)); // NOI18N
        header_payroll.setText("Payroll Keuangan");

        scroll_tabel_keuangan_payroll.setBackground(new java.awt.Color(255, 255, 255));
        scroll_tabel_keuangan_payroll.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tabel_keuangan_pengeluaran.setFont(new java.awt.Font("Helvetica", 0, 17)); // NOI18N
        tabel_keuangan_pengeluaran.setModel(new javax.swing.table.DefaultTableModel(
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
        scroll_tabel_keuangan_payroll.setViewportView(tabel_keuangan_pengeluaran);

        payroll_year.setFont(new java.awt.Font("JetBrainsMono NF SemiBold", 0, 14)); // NOI18N
        payroll_year.setText("2024");
        payroll_year.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payroll_yearActionPerformed(evt);
            }
        });

        payroll_month.setFont(new java.awt.Font("JetBrainsMono NFM SemiBold", 0, 14)); // NOI18N
        payroll_month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));
        payroll_month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payroll_monthActionPerformed(evt);
            }
        });

        pie_chart_payroll.setBackground(new java.awt.Color(255, 255, 255));
        pie_chart_payroll.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        header4.setFont(new java.awt.Font("JetBrainsMono NFM Light", 0, 18)); // NOI18N
        header4.setText("Diagram");

        javax.swing.GroupLayout pie_chart_payrollLayout = new javax.swing.GroupLayout(pie_chart_payroll);
        pie_chart_payroll.setLayout(pie_chart_payrollLayout);
        pie_chart_payrollLayout.setHorizontalGroup(
            pie_chart_payrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pie_chart_payrollLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(header4)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        pie_chart_payrollLayout.setVerticalGroup(
            pie_chart_payrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pie_chart_payrollLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout main_content_payrollLayout = new javax.swing.GroupLayout(main_content_payroll);
        main_content_payroll.setLayout(main_content_payrollLayout);
        main_content_payrollLayout.setHorizontalGroup(
            main_content_payrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_content_payrollLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(main_content_payrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_content_payrollLayout.createSequentialGroup()
                        .addComponent(header_payroll)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(main_content_payrollLayout.createSequentialGroup()
                        .addGroup(main_content_payrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(main_content_payrollLayout.createSequentialGroup()
                                .addComponent(pie_chart_payroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(scroll_tabel_keuangan_payroll, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(main_content_payrollLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(payroll_month, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(payroll_year, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(48, 48, 48))))
        );
        main_content_payrollLayout.setVerticalGroup(
            main_content_payrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_content_payrollLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(header_payroll)
                .addGap(26, 26, 26)
                .addGroup(main_content_payrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(payroll_month)
                    .addComponent(payroll_year, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(main_content_payrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pie_chart_payroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scroll_tabel_keuangan_payroll, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        Parent.add(main_content_payroll, "card5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(side_bar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Parent, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(side_bar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Parent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void dashboard_button_sideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboard_button_sideActionPerformed
        Parent.removeAll();
        Parent.add(main_content_dashboard);
        Parent.repaint();
        Parent.revalidate();
        laporan_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        transaksi_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        payroll_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        dashboard_button_side.setBackground(new java.awt.Color(239, 246, 224));
        this.update_dashboard();
    }//GEN-LAST:event_dashboard_button_sideActionPerformed

    private void laporan_button_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laporan_button_editActionPerformed
        Parent.removeAll();
        Parent.add(main_content_laporan);
        Parent.repaint();
        Parent.revalidate();
        laporan_button_edit.setBackground(new java.awt.Color(239, 246, 224));
        transaksi_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        payroll_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        dashboard_button_side.setBackground(new java.awt.Color(150, 149, 146));
    }//GEN-LAST:event_laporan_button_editActionPerformed

    private void transaksi_button_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transaksi_button_editActionPerformed
        Parent.removeAll();
        Parent.add(main_content_transaksi);
        Parent.repaint();
        Parent.revalidate();
        laporan_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        transaksi_button_edit.setBackground(new java.awt.Color(239, 246, 224));
        payroll_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        dashboard_button_side.setBackground(new java.awt.Color(150, 149, 146));
        this.update_transaksi();
    }//GEN-LAST:event_transaksi_button_editActionPerformed

    private void payroll_button_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payroll_button_editActionPerformed
        Parent.removeAll();
        Parent.add(main_content_payroll);
        Parent.repaint();
        Parent.revalidate();
        laporan_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        transaksi_button_edit.setBackground(new java.awt.Color(150, 149, 146));
        payroll_button_edit.setBackground(new java.awt.Color(239, 246, 224));
        dashboard_button_side.setBackground(new java.awt.Color(150, 149, 146));
        this.update_payroll();
    }//GEN-LAST:event_payroll_button_editActionPerformed

    private void laba_bersih_text_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laba_bersih_text_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_laba_bersih_text_fieldActionPerformed

    private void create_laporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_laporanActionPerformed
        this.generate_report();
    }//GEN-LAST:event_create_laporanActionPerformed

    private void transaksi_monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transaksi_monthActionPerformed
        this.update_transaksi();
    }//GEN-LAST:event_transaksi_monthActionPerformed

    private void transaksi_yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transaksi_yearActionPerformed
        this.update_transaksi();
    }//GEN-LAST:event_transaksi_yearActionPerformed

    private void payroll_yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payroll_yearActionPerformed
        this.update_payroll();
    }//GEN-LAST:event_payroll_yearActionPerformed

    private void payroll_monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payroll_monthActionPerformed
        this.update_payroll();
    }//GEN-LAST:event_payroll_monthActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Parent;
    private javax.swing.JButton create_laporan;
    private javax.swing.JButton dashboard_button_side;
    private javax.swing.JLabel header1;
    private javax.swing.JLabel header2;
    private javax.swing.JLabel header3;
    private javax.swing.JLabel header4;
    private javax.swing.JLabel header_dashboard;
    private javax.swing.JLabel header_laporan;
    private javax.swing.JLabel header_payroll;
    private javax.swing.JLabel header_transaksi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel laba_bersih_icon;
    private javax.swing.JLabel laba_bersih_label;
    private javax.swing.JPanel laba_bersih_panel_dashboard;
    private javax.swing.JTextField laba_bersih_text_field;
    private javax.swing.JButton laporan_button_edit;
    private com.toedter.calendar.JDateChooser laporan_ending_date;
    private com.toedter.calendar.JDateChooser laporan_starting_date;
    private javax.swing.JPanel line_chart_dashboard;
    private javax.swing.JPanel main_content_dashboard;
    private javax.swing.JPanel main_content_laporan;
    private javax.swing.JPanel main_content_payroll;
    private javax.swing.JPanel main_content_transaksi;
    private javax.swing.JButton payroll_button_edit;
    private javax.swing.JComboBox<String> payroll_month;
    private javax.swing.JTextField payroll_year;
    private javax.swing.JLabel pendapatan_icon;
    private javax.swing.JLabel pendapatan_label;
    private javax.swing.JPanel pendapatan_panel_dashboard;
    private javax.swing.JTextField pendapatan_text_field;
    private javax.swing.JLabel pengeluaran_icon;
    private javax.swing.JLabel pengeluaran_label;
    private javax.swing.JPanel pengeluaran_panel_dashboard;
    private javax.swing.JTextField pengeluaran_text_field;
    private javax.swing.JPanel pie_chart_dashboard;
    private javax.swing.JPanel pie_chart_payroll;
    private javax.swing.JPanel pie_chart_transaksi;
    private javax.swing.JScrollPane scroll_tabel_keuangan_payroll;
    private javax.swing.JScrollPane scroll_tabel_keuangan_transaksi;
    private javax.swing.JPanel side_bar;
    private javax.swing.JTable tabel_keuangan_pengeluaran;
    private javax.swing.JTable tabel_keuangan_transaksi;
    private javax.swing.JButton transaksi_button_edit;
    private javax.swing.JComboBox<String> transaksi_month;
    private javax.swing.JTextField transaksi_year;
    // End of variables declaration//GEN-END:variables
}
