/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.Koneksi;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.time.LocalDate;
import javax.swing.JOptionPane;

/**
 *
 * @author Novi
 */
public class PeminjamanModel extends Koneksi {
    private static int idPeminjaman;
    private static Date tanggalPeminjaman;
    private static Date tanggalSelesai;
    private static String keperluan;
    private static String status;
    private static int idGedung;
    private static String idStaff;
    private static String idMahasiswa;
    private static String nim;
    private static String namaGedung;

    public static int getIdPeminjaman() {
        return idPeminjaman;
    }
 
    public static final boolean createPeminjaman(java.sql.Date tanggalPeminjamann, java.sql.Date tanggalSelesaii, String keperluann, String statuss, int gedungIdd, String staffUserIdd, String mahasiswaIdd) {
        boolean isOperationSuccess = false;

        try {
            openConnection();

            if (!isBuildingAvailable(gedungIdd, tanggalPeminjamann, tanggalSelesaii)) {
                System.out.println("Gedung sedang dilakukan peminjaman");
                return false;
            }

            String query = "INSERT INTO peminjaman (tanggal_peminjaman, tanggal_selesai, keperluan, status, gedung_id_gedung, staff_id_user, mahasiswa_id_user) VALUES (?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, tanggalPeminjamann);
            preparedStatement.setDate(2, tanggalSelesaii);
            preparedStatement.setString(3, keperluann);
            preparedStatement.setString(4, statuss);
            preparedStatement.setInt(5, gedungIdd);
            preparedStatement.setString(6, staffUserIdd);
            preparedStatement.setString(7, mahasiswaIdd);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                tanggalPeminjaman = tanggalPeminjamann;
                tanggalSelesai = tanggalSelesaii;
                keperluan = keperluann;
                status = statuss;
                idGedung = gedungIdd;
                idStaff = staffUserIdd;
                idMahasiswa = mahasiswaIdd;

                isOperationSuccess = true;
            } else {
                System.out.println("Insertion failed");
            }
        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            closeConnection();
        }
        return isOperationSuccess;
    }

    private static boolean isBuildingAvailable(int gedungId, java.sql.Date startDate, java.sql.Date endDate) throws SQLException {
        String query = "SELECT * FROM peminjaman WHERE gedung_id_gedung = ? AND status = 'Approved' AND ((tanggal_peminjaman <= ? AND tanggal_selesai >= ?) OR (tanggal_peminjaman <= ? AND tanggal_selesai >= ?))";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, gedungId);
        preparedStatement.setDate(2, endDate);
        preparedStatement.setDate(3, startDate);
        preparedStatement.setDate(4, startDate);
        preparedStatement.setDate(5, endDate);

        ResultSet resultSet = preparedStatement.executeQuery();

        return !resultSet.next();
    }
    
    public static final boolean updatePeminjaman(String newStatus, int peminjamanId) {
        boolean isOperationSuccess = false;

        try {
            openConnection();
            
            if (("Approved".equals(newStatus) || "Done".equals(newStatus)) && cekPeminjaman(peminjamanId)) {
                System.out.println("Gedung sedang dilakukan peminjaman");
                JOptionPane.showMessageDialog(null, "Gedung sedang dilakukan peminjaman", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            String query = "UPDATE peminjaman SET status = ? WHERE id_peminjaman = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, peminjamanId);
            
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                isOperationSuccess = true;
            }

        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            closeConnection();
        }

        return isOperationSuccess;
    }
    
    private static boolean cekPeminjaman(int peminjamanId) throws SQLException {
        String query = "SELECT * FROM peminjaman p1, peminjaman p2 WHERE p1.id_peminjaman = ? AND p1.gedung_id_gedung = p2.gedung_id_gedung AND p1.tanggal_peminjaman = p2.tanggal_peminjaman AND p1.tanggal_selesai = p2.tanggal_selesai AND p2.status = 'Approved'";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, peminjamanId);

        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next(); // Return true if there is an overlapping reservation
    }
    
    public static final boolean deletePeminjaman(int peminjamanId) {
        boolean isOperationSuccess = false;

        try {
            openConnection();

            String query = "DELETE FROM peminjaman WHERE id_peminjaman = ?";
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, peminjamanId);
            
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                isOperationSuccess = true;
            } else {
                System.out.println("Deletion failed. No rows affected.");
            }
        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            closeConnection();
        }

        return isOperationSuccess;
    }  
    
    public static final void showPeminjaman(DefaultTableModel tableModel) {
        try {
            openConnection();
            
            String query = "SELECT p.*, m.id_user, g.nama FROM peminjaman p JOIN mahasiswa m ON "
                    + "p.mahasiswa_id_user = m.id_user JOIN gedung g ON p.gedung_id_gedung = g.id_gedung "
                    + "WHERE (p.status = 'Pending' OR p.status = 'Rejected') ORDER BY id_peminjaman";
            
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                idPeminjaman = resultSet.getInt("id_peminjaman");
                nim = resultSet.getString("id_user");
                namaGedung = resultSet.getString("nama");
                tanggalPeminjaman = resultSet.getDate("tanggal_peminjaman");
                tanggalSelesai = resultSet.getDate("tanggal_selesai");
                keperluan = resultSet.getString("keperluan");
                status = resultSet.getString("status");

                Object[] rowData = {idPeminjaman, nim, namaGedung, tanggalPeminjaman, tanggalSelesai, keperluan, status};
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            displayErrors((SQLException) ex);
        } finally {
            closeConnection();
        }
    }
    
    public static final void showHistory(DefaultTableModel tableModel) {
        try {
            openConnection();
            
            String query = "SELECT p.*, m.id_user, g.nama FROM peminjaman p JOIN mahasiswa m ON "
                    + "p.mahasiswa_id_user = m.id_user JOIN gedung g ON p.gedung_id_gedung = g.id_gedung WHERE"
                    + "(p.status = 'Done' OR p.status = 'Approved') ORDER BY id_peminjaman";
            
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                idPeminjaman = resultSet.getInt("id_peminjaman");
                nim = resultSet.getString("id_user");
                namaGedung = resultSet.getString("nama");
                tanggalPeminjaman = resultSet.getDate("tanggal_peminjaman");
                tanggalSelesai = resultSet.getDate("tanggal_selesai");
                keperluan = resultSet.getString("keperluan");
                status = resultSet.getString("status");

                Object[] rowData = {idPeminjaman, nim, namaGedung, tanggalPeminjaman, tanggalSelesai, keperluan, status};
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            displayErrors((SQLException) ex);
        } finally {
            closeConnection();
        }
    }
    
    public static final void showPeminjamanMahasiswa(DefaultTableModel tableModel, String idUser) {
        try {
            openConnection();
            
            String query = "SELECT p.*, m.id_user, g.nama FROM peminjaman p JOIN mahasiswa m ON "
                    + "p.mahasiswa_id_user = m.id_user JOIN gedung g ON p.gedung_id_gedung = g.id_gedung "
                    + "WHERE (p.status = 'Pending' OR p.status = 'Rejected') AND m.id_user = ? ORDER BY id_peminjaman";
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, idUser); 
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                idPeminjaman = resultSet.getInt("id_peminjaman");
                nim = resultSet.getString("id_user");
                namaGedung = resultSet.getString("nama");
                tanggalPeminjaman = resultSet.getDate("tanggal_peminjaman");
                tanggalSelesai = resultSet.getDate("tanggal_selesai");
                keperluan = resultSet.getString("keperluan");
                status = resultSet.getString("status");

                Object[] rowData = {idPeminjaman, nim, namaGedung, tanggalPeminjaman, tanggalSelesai, keperluan, status};
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            displayErrors((SQLException) ex);
        } finally {
            closeConnection();
        }
    }
    
    public static final void showHistoryMahasiswa(DefaultTableModel tableModel, String idUser) {
        try {
            openConnection();
            
            String query = "SELECT p.*, m.id_user, g.nama FROM peminjaman p JOIN mahasiswa m ON "
                    + "p.mahasiswa_id_user = m.id_user JOIN gedung g ON p.gedung_id_gedung = g.id_gedung WHERE"
                    + "(p.status = 'Done' OR p.status = 'Approved') AND m.id_user = ? ORDER BY id_peminjaman                                                                                                                                                                                                                                                                                                                                                           ";
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, idUser);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                idPeminjaman = resultSet.getInt("id_peminjaman");
                nim = resultSet.getString("id_user");
                namaGedung = resultSet.getString("nama");
                tanggalPeminjaman = resultSet.getDate("tanggal_peminjaman");
                tanggalSelesai = resultSet.getDate("tanggal_selesai");
                keperluan = resultSet.getString("keperluan");
                status = resultSet.getString("status");

                Object[] rowData = {idPeminjaman, nim, namaGedung, tanggalPeminjaman, tanggalSelesai, keperluan, status};
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            displayErrors((SQLException) ex);
        } finally {
            closeConnection();
        }
    }
    
    public static void autoUpdateStatusPeminjaman() {
        try {
            openConnection();

            String query = "UPDATE peminjaman SET status = 'Done' WHERE status = 'Approved' AND tanggal_selesai < ?";
            preparedStatement = connection.prepareStatement(query);

            java.sql.Date currentDate = java.sql.Date.valueOf(LocalDate.now());

            preparedStatement.setDate(1, currentDate);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Status updated to 'Done' for reservations that have ended.");
            } else {
                System.out.println("No reservations to update.");
            }

        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            closeConnection();
        }
    }
}
