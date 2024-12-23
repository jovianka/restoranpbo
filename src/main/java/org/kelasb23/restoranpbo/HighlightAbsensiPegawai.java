/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kelasb23.restoranpbo;

import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.kelasb23.restoranpbo.models.AbsensiPegawai;
import org.kelasb23.restoranpbo.models.User;

/**
 *
 * @author jeanjacket
 */
public class HighlightAbsensiPegawai implements DateHighlightPolicy{
    
    ArrayList<AbsensiPegawai> absensi;
    
    public HighlightAbsensiPegawai(ArrayList<AbsensiPegawai> absensi) {
        this.absensi = absensi;
    }
    
    @Override
    public HighlightInformation getHighlightInformationOrNull(LocalDate date) {        
        for (AbsensiPegawai absen: this.absensi) {
            System.out.println(absen.tanggal);
            if (absen.tanggal.isEqual(date)) {
                if (absen.hadir.equals("hadir")) {
                    return new HighlightInformation(Color.BLUE, Color.WHITE);
                }
                if (absen.hadir.equals("sakit")) {
                    return new HighlightInformation(Color.YELLOW, Color.BLACK);
                }
                if (absen.hadir.equals("absen")) {
                    return new HighlightInformation(Color.RED, Color.WHITE);
                }
            }
        }

 
        return null;
    }
    
}
