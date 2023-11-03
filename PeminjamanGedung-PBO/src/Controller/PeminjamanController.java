/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.PeminjamanModel;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Novi
 */
public class PeminjamanController {
    
    public static void showPeminjaman(DefaultTableModel tableModel) {
        PeminjamanModel.showPeminjaman(tableModel);
    }
    
    public static void showHistory ( DefaultTableModel tableModel) {
        PeminjamanModel.showHistory(tableModel);
    }
    
    public static void showPeminjamanMahasiswa (DefaultTableModel tableModel, String idUser) {
        PeminjamanModel.showPeminjamanMahasiswa(tableModel, idUser);
    }
    
    public static void showHistoryMahasiswa ( DefaultTableModel tableModel, String idUser) {
        PeminjamanModel.showHistoryMahasiswa(tableModel, idUser);
    }
    
    public static boolean addPeminjaman(Date tanggalPeminjaman, Date tanggalSelesai, String keperluan, String status, int gedungId, String staffUserId, String mahasiswaId) {
        java.sql.Date sqlTanggalPeminjaman = new java.sql.Date(tanggalPeminjaman.getTime());
        java.sql.Date sqlTanggalSelesai = new java.sql.Date(tanggalSelesai.getTime());

        if (!isValidDateRange(sqlTanggalPeminjaman, sqlTanggalSelesai)) {    
        }
        
        boolean success = PeminjamanModel.createPeminjaman(sqlTanggalPeminjaman, sqlTanggalSelesai, keperluan, status, gedungId, mahasiswaId, staffUserId);
        if (success) {
            JOptionPane.showMessageDialog(null, "Peminjaman berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Penambahan gagal. Silakan coba lagi", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }

        return success;
    }
        
    private static boolean isValidDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        if (startDate.before(new java.sql.Date(System.currentTimeMillis()))) {
            JOptionPane.showMessageDialog(null, "Tanggal Peminjaman tidak boleh kurang dari hari ini");
            return false;
        }

        if (endDate.before(startDate)) {
            JOptionPane.showMessageDialog(null, "Tanggal Selesai tidak boleh kurang dari hari ini");
            return false;
        }

        return true;
    }
    
    public static boolean updatePeminjamanStatus(String newStatus, int idPeminjaman) {
        return PeminjamanModel.updatePeminjaman(newStatus, idPeminjaman);
    }
    
    public static String getSelectedStatusFromComboBox(JComboBox<String> comboBox) {
        return comboBox.getSelectedItem().toString();
    }
    
    public static boolean deletePeminjaman(int peminjamanId) {
        return PeminjamanModel.deletePeminjaman(peminjamanId);
    }
    
    public static int getIdPeminjaman(){
        return PeminjamanModel.getIdPeminjaman();
    }   
    
    public static void updateStatus() {
        PeminjamanModel.autoUpdateStatusPeminjaman();
    }
}
