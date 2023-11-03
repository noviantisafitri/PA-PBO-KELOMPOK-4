# Sistem Informasi Peminjaman Ruang
    
# Deskripsi Project

"Sistem Informasi Peminjaman Ruang" merupakan aplikasi Java yang dibangun menggunakan paradigma Pemrograman Berorientasi Objek (PBO). Aplikasi ini dirancang untuk memudahkan proses peminjaman ruang dengan menyediakan antarmuka yang interaktif dan mudah digunakan. Aplikasi ini mengikuti arsitektur Model-View-Controller (MVC), yang memisahkan logika aplikasi menjadi tiga komponen utama untuk meningkatkan modularity dan memfasilitasi perawatan kode yang lebih mudah.

Fitur Utama

- Manajemen ruang: Mengizinkan pengguna untuk menambah, mengubah, dan menghapus informasi ruang yang tersedia untuk peminjaman.
- Manajemen Peminjaman: Memungkinkan pengguna untuk membuat, memodifikasi, dan mengelola peminjaman ruang.
- Autentikasi Pengguna: Fitur login untuk membedakan antara berbagai jenis pengguna seperti staff dan mahasiswa.

Teknologi

- Java: Bahasa pemrograman utama yang digunakan untuk membangun aplikasi.
- NetBeans IDE: Lingkungan pengembangan terpadu yang digunakan untuk mengembangkan aplikasi.
- MySQL: Sistem manajemen basis data yang digunakan untuk menyimpan informasi ruang dan peminjaman.

Struktur Proyek

- src: Direktori yang berisi source code Java, termasuk paket Controller, Database, Model, dan View.
- dist: Berisi file JAR yang dapat dijalankan dan library pihak ketiga yang diperlukan untuk menjalankan aplikasi.
- nbproject: Berisi file konfigurasi yang spesifik untuk proyek NetBeans.

Proyek ini juga mencakup file peminjaman_ruang.sql yang menyediakan skrip SQL untuk membuat struktur basis data yang diperlukan oleh aplikasi. Ini mencakup tabel dan relasi yang diperlukan untuk menyimpan data peminjaman ruang.

Cara Menjalankan

- Untuk menjalankan aplikasi ini, Anda dapat menggunakan file JAR yang disediakan di dalam direktori dist. Pastikan bahwa semua library yang terdapat di dist/lib sudah termasuk dalam classpath aplikasi.

# Flowchart

![image](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/454cfc23-8e7f-411e-bf23-f79a03bdbdfe)

# ERD

![Logical](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/126859339/4bb8dbab-c531-4280-b389-d3267c7237fc)
![Relational_1](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/126859339/9001fe4d-111f-4e8f-9d79-7edb36fdde32)

# Hirarki Class

# SourceCode dan Penjelasan

## Package Model
## Model Classes

- GedungModel: Bertanggung jawab atas representasi data gedung, termasuk ID, nama, dan kapasitas. Class ini menyediakan metode untuk CRUD operasi yang memungkinkan aplikasi untuk berinteraksi dengan database gedung, seperti menambahkan gedung baru, mengambil daftar gedung, mengupdate informasi gedung, dan menghapus gedung dari sistem.
```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.Koneksi;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Novi
 */
public class GedungModel extends Koneksi {
    private int idGedung;
    private String namaGedung;
    private int kapasitasGedung;
    private Date tanggalPeminjaman;
    private Date tanggalSelesai;
    private String keperluan;
    private final Map<String, Integer> idGedungMap;

    public GedungModel() {
        this.idGedungMap = new HashMap<>();
    }
    
    public int getIdGedung() {
        return idGedung;
    }

    public void setIdGedung(int idGedung) {
        this.idGedung = idGedung;
    }

    public String getNamaGedung() {
        return namaGedung;
    }

    public Date getTanggalPeminjaman() {
        return tanggalPeminjaman;
    }

    public Date getTanggalSelesai() {
        return tanggalSelesai;
    }

    public String getKeperluan() {
        return keperluan;
    }
    
    public boolean showGedung(int idGedung, String namaGedung, int kapasitasGedung) {
        boolean isOperationSuccess = false;

        try {
            openConnection();

            String query = "SELECT * FROM gedung WHERE id_gedung = ? AND nama = ? AND kapasitas = ?";
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idGedung);
            preparedStatement.setString(2, namaGedung);
            preparedStatement.setInt(3, kapasitasGedung);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                this.idGedung = resultSet.getInt("id_gedung");
                this.namaGedung = resultSet.getString("nama");
                this.kapasitasGedung = resultSet.getInt("kapasitas");
                
                isOperationSuccess = true;
                
            }
        } catch (SQLException ex) {
            GedungModel.displayErrors((SQLException) ex);
        } finally {
            closeConnection();
        }

        return isOperationSuccess;
    }
     
    public DefaultTableModel cariGedung (int idGedung, Date tanggalPeminjaman) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Gedung");
        tableModel.addColumn("Tanggal Awal");
        tableModel.addColumn("Tanggal Akhir");
        tableModel.addColumn("Kegiatan");

        try {
            openConnection();

            String query = "SELECT peminjaman.*, gedung.nama FROM peminjaman JOIN gedung ON peminjaman.gedung_id_gedung = gedung.id_gedung WHERE peminjaman.gedung_id_gedung = ? AND (? BETWEEN peminjaman.tanggal_peminjaman AND peminjaman.tanggal_selesai) AND peminjaman.status = 'Approved'";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idGedung);
            preparedStatement.setDate(2, new java.sql.Date(tanggalPeminjaman.getTime()));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("nama"),
                        resultSet.getDate("tanggal_peminjaman"),
                        resultSet.getDate("tanggal_selesai"),
                        resultSet.getString("keperluan")
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            closeConnection();
        }

        return tableModel;
    }

    public void setJComboBoxModel(JComboBox<String> comboBox) {
        try {
            openConnection();

            String query = "SELECT * FROM gedung";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            comboBox.removeAllItems();

            while (resultSet.next()) {
                this.idGedung = resultSet.getInt("id_gedung");
                this.namaGedung = resultSet.getString("nama");
                this.kapasitasGedung = resultSet.getInt("kapasitas");

                comboBox.addItem(namaGedung);

                idGedungMap.put(namaGedung, idGedung);

            }

            comboBox.addActionListener((var e) -> {
                String selectedNamaGedung = (String) comboBox.getSelectedItem();
                if (selectedNamaGedung != null) {
                    int selectedIdGedung = idGedungMap.get(selectedNamaGedung);
                }
            });

        } catch (SQLException ex) {
            GedungModel.displayErrors(ex);
        } finally {
            closeConnection();
        }
    }
    
    public int getIdGedung(String selectedNamaGedung) {
        return idGedungMap.getOrDefault(selectedNamaGedung, -1);
    }
}
```
- UserModel: Mengatur informasi pengguna aplikasi. Ini termasuk pengelolaan kredensial pengguna, fungsi login, dan mempertahankan status sesi pengguna. Class ini juga dapat menangani peran pengguna (misalnya, mahasiswa atau staff) dan membatasi akses ke fitur tertentu berdasarkan peran tersebut.
```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Database.Koneksi;
import java.sql.SQLException;

/**
 *
 * @author Novi
 */
public class UserModel extends Koneksi{
//    Properti
    private static String idUser;
    private static String nama;
    private static String noTelepon;
    private static String password;
    private static String role;
    private static String jabatan;
    private static String programStudi;
    private static String fakultas;

//    Getter dan Setter
    public static  String getIdUser() {
        return idUser;
    }
    
    public static String getNama() {
        return nama;
    }

    public static void setNama(String namaU) {
        nama = namaU;
    }

    public static String getNoTelepon() {
        return noTelepon;
    }

    public static void setNoTelepon(String noTeleponU) {
        noTelepon = noTeleponU;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String passwordU) {
        password = passwordU;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String roleU) {
        role = roleU;
    }

    public static String getJabatan() {
        return jabatan;
    }

    public static void setJabatan(String jabatanU) {
        jabatan = jabatanU;
    }

    public static String getProgramStudi() {
        return programStudi;
    }

    public static String getFakultas() {
        return fakultas;
    }

//    Method untuk mengecek login
    public static final boolean authenticateUser(String username, 
            String passwordd, String rolee) {
        boolean isOperationSuccess = false;

        try {
            openConnection();

            String query = "SELECT * FROM user WHERE id_user = ? AND "
                    + "password = ? AND role = ?";
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordd);
            preparedStatement.setString(3, rolee);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                idUser = resultSet.getString("id_user");
                password = resultSet.getString("password");
                role = resultSet.getString("role");
                
                isOperationSuccess = true;
                
            }
        } catch (SQLException ex) {
            displayErrors((SQLException) ex);
        } finally {
            closeConnection();
        }

        return isOperationSuccess;
    }
//Method untuk mencari data staff ditabel staff
    public static final boolean findDataUserStaff(String username) {
        boolean isOperationSuccess = false;

        try {
            openConnection();

            String query = "SELECT u.*, s.* FROM user u JOIN staff s ON "
                    + "u.id_user = s.id_user WHERE u.id_user = ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idUser = resultSet.getString("id_user");
                jabatan = resultSet.getString("jabatan");
                nama = resultSet.getString("nama");
                noTelepon = resultSet.getString("no_telepon");

                isOperationSuccess = true;
            }
        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            closeConnection();
        }

        return isOperationSuccess;
    }
//Method untuk mencari data mahasiswa ditabel mahasiswa
    public static final boolean findMahasiswa(String nim) {
        boolean isOperationSuccess = false;

        try {
            openConnection();

            String query = "SELECT u.*, m.* FROM user u JOIN mahasiswa m "
                    + "ON u.id_user = m.id_user WHERE m.id_user = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nim);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                idUser = resultSet.getString("id_user");
                nama = resultSet.getString("nama");
                programStudi = resultSet.getString("program_studi");
                fakultas = resultSet.getString("fakultas");
                noTelepon = resultSet.getString("no_telepon");

                isOperationSuccess = true; 
            }
        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            closeConnection();
        }
        return isOperationSuccess;
    }
}
```

- PeminjamanModel: Mengelola data peminjaman gedung, seperti informasi peminjam, tanggal peminjaman, dan keperluan peminjaman. Class ini berinteraksi dengan database untuk menyimpan dan mengambil peminjaman, serta mengelola status dan detail peminjaman gedung.
```java
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
                    + "(p.status = 'Done' OR p.status = 'Approved') AND m.id_user = ? ORDER BY id_peminjaman";
            
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

```
## Package Controller

- GedungController: Sebagai penghubung antara GedungModel dan tampilan yang berkaitan dengan gedung, class ini mengelola logika bisnis untuk operasi gedung. Ini termasuk mengambil input pengguna, memvalidasi data, dan memanggil metode yang relevan pada GedungModel untuk menjalankan operasi database.
  ```java
  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.GedungModel;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Novi
 */
public class GedungController {
    private final GedungModel gedungModel;
    private int selectedGedungId;

    public GedungController() {
        this.gedungModel = new GedungModel();
    }

    public int getSelectedGedungId() {
        return this.selectedGedungId;
    }

    public void setJComboBoxModel(JComboBox<String> comboBox) {
        gedungModel.setJComboBoxModel(comboBox);

        if (comboBox.getItemCount() > 0) {
            String firstGedungName = (String) comboBox.getItemAt(0);
            this.selectedGedungId = gedungModel.getIdGedung(firstGedungName);
        }
        
        comboBox.addActionListener(e -> {
            String selectedNamaGedung = (String) comboBox.getSelectedItem();
            if (selectedNamaGedung != null) {
                this.selectedGedungId = gedungModel.getIdGedung(selectedNamaGedung);
            }
        });
    }
    
    public String namaGedung(){
        return gedungModel.getNamaGedung();
    }
    
    public Date tanggalSelesai(){
        return gedungModel.getTanggalSelesai();
    }
    
    public String keperluan(){
        return gedungModel.getKeperluan();
    }
    
    public DefaultTableModel cariGedung(int idGedung, Date tanggalPeminjaman) {
        return gedungModel.cariGedung(idGedung, tanggalPeminjaman);
    }

}
  ```

- PeminjamanController
  ```java
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
  ```
- UserController
  ```java
  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.UserModel;

/**
 *
 * @author Novi
 */
public class UserController {

    public static boolean loginUser(String username, String password, String role) {
        return UserModel.authenticateUser(username, password, role);  
    }   
    
    public static String getUserId() {
        return UserModel.getIdUser();
    }
    
    public static String getUserNama() {
        return UserModel.getNama();
    }
    
    public static String getUserNoTelepon() {
        return UserModel.getNoTelepon();
    }
    
    public static boolean getIdMahasiswa(String nim) {
        return UserModel.findMahasiswa(nim);
    }   
}

  ```
- StaffController
  ```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.UserModel;


/**
 *
 * @author Novi
 */
public class StaffController extends UserController{
    
    public static boolean profileStaff(String username) {
        return UserModel.findDataUserStaff(username);
    }
        
    public static String getUserJabatan() {
        return UserModel.getJabatan();
    }
}

  ```
- MahasiswaController
  ```java
  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.UserModel;


/**
 *
 * @author Novi
 */
public class MahasiswaController extends UserController {
    
    
    public static boolean profileMahasiswa(String username) {
        return UserModel.findMahasiswa(username);
    }
        
    public static String getUserProgramStudi () {
        return UserModel.getProgramStudi();
    }
    
    public static String getUserFakultas () {
        return UserModel.getFakultas();
    }
}

  ```

## Package View

- Login: Tampilan untuk autentikasi pengguna. Memiliki elemen UI seperti kotak teks untuk nama pengguna dan kata sandi, serta tombol untuk masuk. Class ini berkomunikasi dengan UserController untuk memverifikasi kredensial pengguna dan memberikan akses ke sistem.
```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.MahasiswaController;
import Controller.PeminjamanController;
import Controller.StaffController;
import Controller.UserController;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JOptionPane;

/**
 *
 * @author Novi
 */
public class Login extends javax.swing.JFrame {
    /**
     * Creates new form L
     */
    public Login() {
        initComponents();
        
        setVisible(true);
        setResizable(false);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        Login = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_Login = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(11, 36, 71));
        setMinimumSize(new java.awt.Dimension(1100, 600));
        setSize(new java.awt.Dimension(1100, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(1100, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setForeground(new java.awt.Color(0, 51, 51));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/loginPict.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        Login.setBackground(new java.awt.Color(255, 255, 255));
        Login.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        Login.setForeground(new java.awt.Color(255, 255, 255));
        Login.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Peminjaman Gedung");
        Login.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, -1, -1));

        btn_Login.setBackground(new java.awt.Color(11, 36, 71));
        btn_Login.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_Login.setForeground(new java.awt.Color(255, 255, 255));
        btn_Login.setText("LOGIN");
        btn_Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LoginActionPerformed(evt);
            }
        });
        Login.add(btn_Login, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 410, 253, 40));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        Login.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 250, 35));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Password");
        Login.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("NIP/NIM");
        Login.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Sistem Informasi");
        Login.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Role", "Staff", "Mahasiswa" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        Login.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, -1, -1));
        Login.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 250, 35));

        jPanel1.add(Login, new org.netbeans.lib.awtextra.AbsoluteConstraints(651, 15, 440, 550));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>                        

    private void btn_LoginActionPerformed(java.awt.event.ActionEvent evt) {                                          
        String username = jTextField1.getText();
        String password = jPasswordField1.getText();
        String role = (String) jComboBox1.getSelectedItem();

        if (!"Pilih Role".equals(role)) {
            boolean success = UserController.loginUser(username, password, role);
            if (success) {
                PeminjamanController.updateStatus();
                if ("Staff".equals(role)) {
                    StaffController.profileStaff(username);
                    String userId = StaffController.getUserId();
                    String userJabatan = StaffController.getUserJabatan();
                    String userNama = StaffController.getUserNama();
                    String userNoTelepon = StaffController.getUserNoTelepon();
                    
                    MenuStaff menuStaff = new MenuStaff();
                    menuStaff.getidUser(userId);
                    menuStaff.displayStaffProfile(userId,userJabatan, userNama, userNoTelepon);
                    menuStaff.setVisible(true);
                    
                    this.dispose();
                    
                } else if ("Mahasiswa".equals(role)) {
                    System.out.println("Selected role: " + role);
                    System.out.println("Username: " + username);
                     
                    MahasiswaController.profileMahasiswa(username);
                    String userId = MahasiswaController.getUserId();
                    String userNama = MahasiswaController.getUserNama();
                    String userProgramstudi = MahasiswaController.getUserProgramStudi();
                    String userFakultas = MahasiswaController.getUserFakultas();
                    String userNoTelepon = MahasiswaController.getUserNoTelepon();

                    MenuMahasiswa menuMahasiswa = new MenuMahasiswa();
                    menuMahasiswa.getidUser(userId);
                    menuMahasiswa.showPeminjamanData(userId);
                    menuMahasiswa.displayMahasiswaProfile(userId, userNama, userProgramstudi, userFakultas, userNoTelepon);
                    menuMahasiswa.showHistoryPeminjamanMahasiswa(userId);
                    menuMahasiswa.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Login Gagal");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih role terlebih dahulu");
        }
    }                                         

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            

    }                                           

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           

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
        FlatLightLaf.setup();
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel Login;
    private javax.swing.JButton btn_Login;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}

```

- MenuMahasiswa
```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.GedungController;
import Controller.PeminjamanController;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import java.util.*;


/**
 *
 * @author Novi
 */
public class MenuMahasiswa extends javax.swing.JFrame {
    GedungController gedungController = new GedungController();
    
    private String idUser;
    private int idPeminjaman;

    /**
     * Creates new form Home
     */
    public MenuMahasiswa() {
        initComponents();
        
        setExtendedState(JFrame.MAXIMIZED_HORIZ);
        setVisible(true);
        setResizable(false);
       
        showPeminjamanData(idUser);
        gedungController.setJComboBoxModel(jComboBoxCariGedung);
        
    }
    
    public String getidUser(String idUser){
        return this.idUser = idUser;
    }
    
    public final void showPeminjamanData(String idUser) {
        DefaultTableModel tableModel = (DefaultTableModel) TabelPeminjaman.getModel();
        tableModel.setRowCount(0);
        PeminjamanController.showPeminjamanMahasiswa(tableModel, idUser);
    }
     
    public void showHistoryPeminjamanMahasiswa(String idUser) {
        DefaultTableModel tableModel = (DefaultTableModel) TabelHistory.getModel();
        tableModel.setRowCount(0);
        PeminjamanController.showHistoryMahasiswa(tableModel, idUser);
    }
    
    public void displayMahasiswaProfile(String idUser, String userNama, String userProgramStudi,String userFakultas, String userNoTelepon) {
        NamaUser.setText(userNama);
        jLabelNIM.setText(idUser);
        jLabelNama.setText(userNama);
        jLabelProgramStudi.setText(userProgramStudi);
        jLabelFakultas.setText(userFakultas);
        jLabelNoTelepon5.setText(userNoTelepon);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        allMenu = new javax.swing.JPanel();
        HomePanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        NIP = new javax.swing.JLabel();
        NoTelepon = new javax.swing.JLabel();
        Nama = new javax.swing.JLabel();
        jLabelNama = new javax.swing.JLabel();
        Jabatan = new javax.swing.JLabel();
        jLabelProgramStudi = new javax.swing.JLabel();
        jLabelFakultas = new javax.swing.JLabel();
        jLabelNIM = new javax.swing.JLabel();
        jLabelNoTelepon1 = new javax.swing.JLabel();
        jLabelNoTelepon2 = new javax.swing.JLabel();
        jLabelNoTelepon3 = new javax.swing.JLabel();
        jLabelNoTelepon4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        NoTelepon1 = new javax.swing.JLabel();
        jLabelNoTelepon5 = new javax.swing.JLabel();
        jLabelNoTelepon8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateTAwal = new com.toedter.calendar.JDateChooser();
        jComboBoxCariGedung = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableHasilCari = new javax.swing.JTable();
        jButtonCari = new javax.swing.JButton();
        PeminjamanPanel = new javax.swing.JPanel();
        jButtonBatal = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jButtonAdd = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jDateTanggalPeminjaman = new com.toedter.calendar.JDateChooser();
        jDateTanggalSelesai = new com.toedter.calendar.JDateChooser();
        jComboBoxGedung = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelPeminjaman = new javax.swing.JTable();
        jComboBoxKegiatan = new javax.swing.JComboBox<>();
        HistoryPeminjaman = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelHistory = new javax.swing.JTable();
        sidepanel = new javax.swing.JPanel();
        NamaUser = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        menuPeminjaman = new javax.swing.JButton();
        menuHistory = new javax.swing.JButton();
        menuHome = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 0));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(23, 107, 135));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 100));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Heavy", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Sistem Informasi Peminjaman Gedung");

        jButton1.setText("Log Out");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(56, 56, 56))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jLabel9))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        allMenu.setMinimumSize(new java.awt.Dimension(800, 500));
        allMenu.setPreferredSize(new java.awt.Dimension(800, 500));
        allMenu.setLayout(new java.awt.CardLayout());

        HomePanel.setBackground(new java.awt.Color(255, 255, 255));
        HomePanel.setPreferredSize(new java.awt.Dimension(900, 477));
        HomePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setForeground(new java.awt.Color(0, 51, 51));

        NIP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NIP.setText("NIM");

        NoTelepon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NoTelepon.setText("Fakultas");

        Nama.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Nama.setText("Nama");

        jLabelNama.setAutoscrolls(true);
        jLabelNama.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabelNamaAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                jLabelNamaAncestorRemoved(evt);
            }
        });

        Jabatan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Jabatan.setText("Program Studi");

        jLabelProgramStudi.setAutoscrolls(true);

        jLabelFakultas.setAutoscrolls(true);

        jLabelNIM.setAutoscrolls(true);
        jLabelNIM.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabelNIMAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                jLabelNIMAncestorRemoved(evt);
            }
        });

        jLabelNoTelepon1.setText(": ");
        jLabelNoTelepon1.setAutoscrolls(true);

        jLabelNoTelepon2.setText(": ");
        jLabelNoTelepon2.setAutoscrolls(true);

        jLabelNoTelepon3.setText(": ");
        jLabelNoTelepon3.setAutoscrolls(true);

        jLabelNoTelepon4.setText(": ");
        jLabelNoTelepon4.setAutoscrolls(true);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/user_6994648.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("PROFIL MAHASISWA");

        NoTelepon1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NoTelepon1.setText("No Telepon");

        jLabelNoTelepon5.setAutoscrolls(true);

        jLabelNoTelepon8.setText(": ");
        jLabelNoTelepon8.setAutoscrolls(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 57, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NIP)
                            .addComponent(Jabatan)
                            .addComponent(NoTelepon)
                            .addComponent(Nama)
                            .addComponent(NoTelepon1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(11, 11, 11)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel7))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabelNoTelepon4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelProgramStudi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabelNoTelepon2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabelNoTelepon1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelNIM, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelNoTelepon3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelNoTelepon8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelNoTelepon5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelFakultas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(53, 53, 53))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel7)))
                .addGap(44, 44, 44)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelNIM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NIP, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNoTelepon1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Nama, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(jLabelNoTelepon2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelProgramStudi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jabatan, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(jLabelNoTelepon4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelFakultas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NoTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNoTelepon3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(NoTelepon1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelNoTelepon8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelNoTelepon5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        HomePanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 410, 360));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("SISTEM INFORMASI JADWAL GEDUNG");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("UNIVERSITAS MULAWARMAN");

        jDateTAwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateTAwalMouseClicked(evt);
            }
        });

        jComboBoxCariGedung.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxCariGedung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCariGedungActionPerformed(evt);
            }
        });

        jTableHasilCari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableHasilCari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Gedung", "Tanggal Awal", "Tanggal Akhir", "Kegiatan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableHasilCari.setEnabled(false);
        jTableHasilCari.setShowGrid(true);
        jTableHasilCari.getTableHeader().setResizingAllowed(false);
        jTableHasilCari.getTableHeader().setReorderingAllowed(false);
        jTableHasilCari.setUpdateSelectionOnSort(false);
        jTableHasilCari.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(jTableHasilCari);

        jButtonCari.setText("Cek Jadwal");
        jButtonCari.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButtonCariFocusGained(evt);
            }
        });
        jButtonCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateTAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxCariGedung, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonCari))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jLabel4)))
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(58, 58, 58))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jDateTAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxCariGedung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonCari, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        HomePanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 70, 450, 360));

        allMenu.add(HomePanel, "card4");

        PeminjamanPanel.setBackground(new java.awt.Color(255, 255, 255));
        PeminjamanPanel.setMaximumSize(new java.awt.Dimension(1000, 600));
        PeminjamanPanel.setMinimumSize(new java.awt.Dimension(1000, 600));
        PeminjamanPanel.setPreferredSize(new java.awt.Dimension(1000, 600));

        jButtonBatal.setText("Batal");
        jButtonBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBatalActionPerformed(evt);
            }
        });

        jLabel22.setText("Gedung");

        jButtonAdd.setText("Tambah");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jLabel23.setText("Tanggal Peminjaman");

        jLabel24.setText("Tanggal Selesai");

        jLabel25.setText("Keperluan");

        jDateTanggalSelesai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateTanggalSelesaiMouseClicked(evt);
            }
        });

        jComboBoxGedung.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxGedung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGedungActionPerformed(evt);
            }
        });

        TabelPeminjaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Peminjaman", "NIM", "Gedung", "Tanggal Peminjaman", "Tanggal Selesai", "Keperluan", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelPeminjaman.getTableHeader().setResizingAllowed(false);
        TabelPeminjaman.getTableHeader().setReorderingAllowed(false);
        TabelPeminjaman.setUpdateSelectionOnSort(false);
        TabelPeminjaman.setVerifyInputWhenFocusTarget(false);
        TabelPeminjaman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelPeminjamanMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(TabelPeminjaman);

        jComboBoxKegiatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seminar", "Lomba", "Rapat", "Kuliah Umum" }));

        javax.swing.GroupLayout PeminjamanPanelLayout = new javax.swing.GroupLayout(PeminjamanPanel);
        PeminjamanPanel.setLayout(PeminjamanPanelLayout);
        PeminjamanPanelLayout.setHorizontalGroup(
            PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel22))
                        .addGap(103, 103, 103)
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBoxKegiatan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxGedung, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addGap(45, 45, 45)
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateTanggalSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateTanggalPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                        .addComponent(jButtonAdd)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonBatal)))
                .addGap(0, 612, Short.MAX_VALUE))
            .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 885, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PeminjamanPanelLayout.setVerticalGroup(
            PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jComboBoxGedung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jComboBoxKegiatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateTanggalPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(18, 18, 18)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateTanggalSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(30, 30, 30)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBatal)
                    .addComponent(jButtonAdd))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 117, Short.MAX_VALUE))
        );

        allMenu.add(PeminjamanPanel, "card3");

        HistoryPeminjaman.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        TabelHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Peminjaman", "NIM", "Nama Gedung", "Tanggal Peminjaman", "Tanggal Selesai", "Keperluan", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelHistory.setEnabled(false);
        TabelHistory.setShowGrid(true);
        TabelHistory.getTableHeader().setResizingAllowed(false);
        TabelHistory.getTableHeader().setReorderingAllowed(false);
        TabelHistory.setUpdateSelectionOnSort(false);
        TabelHistory.setVerifyInputWhenFocusTarget(false);
        jScrollPane6.setViewportView(TabelHistory);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 859, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 419, Short.MAX_VALUE)
                    .addComponent(jLabel39)
                    .addGap(0, 419, Short.MAX_VALUE)))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 419, Short.MAX_VALUE)
                    .addComponent(jLabel40)
                    .addGap(0, 419, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 240, Short.MAX_VALUE)
                    .addComponent(jLabel39)
                    .addGap(0, 240, Short.MAX_VALUE)))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 240, Short.MAX_VALUE)
                    .addComponent(jLabel40)
                    .addGap(0, 240, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout HistoryPeminjamanLayout = new javax.swing.GroupLayout(HistoryPeminjaman);
        HistoryPeminjaman.setLayout(HistoryPeminjamanLayout);
        HistoryPeminjamanLayout.setHorizontalGroup(
            HistoryPeminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoryPeminjamanLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        HistoryPeminjamanLayout.setVerticalGroup(
            HistoryPeminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        allMenu.add(HistoryPeminjaman, "card4");

        sidepanel.setBackground(new java.awt.Color(11, 36, 71));
        sidepanel.setForeground(new java.awt.Color(255, 255, 255));
        sidepanel.setPreferredSize(new java.awt.Dimension(200, 600));

        NamaUser.setForeground(new java.awt.Color(255, 255, 255));
        NamaUser.setText("Nama Mahasiswa");

        jLabel10.setForeground(new java.awt.Color(102, 204, 255));
        jLabel10.setText("Mahasiswa");

        menuPeminjaman.setText("Peminjaman");
        menuPeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPeminjamanActionPerformed(evt);
            }
        });

        menuHistory.setText("History");
        menuHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHistoryActionPerformed(evt);
            }
        });

        menuHome.setText("Home");
        menuHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHomeActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/user_6994648.png"))); // NOI18N

        javax.swing.GroupLayout sidepanelLayout = new javax.swing.GroupLayout(sidepanel);
        sidepanel.setLayout(sidepanelLayout);
        sidepanelLayout.setHorizontalGroup(
            sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidepanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(NamaUser)
                    .addGroup(sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(menuHome, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(menuPeminjaman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(menuHistory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(sidepanelLayout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addGroup(sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(sidepanelLayout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel10))))))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        sidepanelLayout.setVerticalGroup(
            sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidepanelLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(NamaUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(33, 33, 33)
                .addComponent(menuHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(menuPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(menuHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidepanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                    .addComponent(allMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(allMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 494, Short.MAX_VALUE))
                    .addComponent(sidepanel, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        Login login = new Login();
        
        this.dispose();
        login.setVisible(true);
    }                                        

    private void menuPeminjamanActionPerformed(java.awt.event.ActionEvent evt) {                                               
        allMenu.removeAll();
        allMenu.repaint();
        allMenu.revalidate();

        allMenu.add(PeminjamanPanel);
        allMenu.repaint();
        allMenu.revalidate();

        gedungController.setJComboBoxModel(jComboBoxGedung);
    }                                              

    private void menuHistoryActionPerformed(java.awt.event.ActionEvent evt) {                                            
        allMenu.removeAll();
        allMenu.repaint();
        allMenu.revalidate();

        allMenu.add(HistoryPeminjaman);
        allMenu.repaint();
        allMenu.revalidate();

        showHistoryPeminjamanMahasiswa(idUser);
    }                                           

    private void menuHomeActionPerformed(java.awt.event.ActionEvent evt) {                                         
        allMenu.removeAll();
        allMenu.repaint();
        allMenu.revalidate();

        allMenu.add(HomePanel);
        allMenu.repaint();
        allMenu.revalidate();
    }                                        

    private void jLabelNamaAncestorAdded(javax.swing.event.AncestorEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void jLabelNamaAncestorRemoved(javax.swing.event.AncestorEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void jLabelNIMAncestorAdded(javax.swing.event.AncestorEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void jLabelNIMAncestorRemoved(javax.swing.event.AncestorEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void jDateTAwalMouseClicked(java.awt.event.MouseEvent evt) {                                        

    }                                       

    private void jComboBoxCariGedungActionPerformed(java.awt.event.ActionEvent evt) {                                                    

    String selectedGedung = (String) jComboBoxCariGedung.getSelectedItem();

    // Perform some action based on the selected item
    if (selectedGedung != null) {
        // Add your logic here, for example:
        System.out.println("Selected Gedung: " + selectedGedung);
    }

    }                                                   

    private void jButtonCariFocusGained(java.awt.event.FocusEvent evt) {                                        

    }                                       

    private void jButtonCariActionPerformed(java.awt.event.ActionEvent evt) {                                            

        int idGedung = gedungController.getSelectedGedungId();
        Date tanggalPeminjaman = jDateTAwal.getDate();

        DefaultTableModel tableModel = gedungController.cariGedung(idGedung, tanggalPeminjaman);
        jTableHasilCari.setModel(tableModel);

    }                                           

    private void TabelPeminjamanMouseClicked(java.awt.event.MouseEvent evt) {                                             
        jButtonAdd.setEnabled(false);

        DefaultTableModel tblModel = (DefaultTableModel)TabelPeminjaman.getModel();
        int idPeminjaman1 = (int) tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 0);
        this.idPeminjaman = idPeminjaman1;

        System.out.println(idPeminjaman);
        String tabelNIM = tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 1).toString();
        String tabelGedung = tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 2).toString();
        Date tabelTanggalP = (Date) tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 3);
        Date tabelTanggalS = (Date) tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 4);
        String tabelKeperluan = tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 5).toString();
        String tabelStatus = tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 6).toString();

        jComboBoxGedung.setSelectedItem(tabelGedung);
        jDateTanggalPeminjaman.setDate(tabelTanggalP);
        jDateTanggalSelesai.setDate(tabelTanggalS);
        jComboBoxKegiatan.setSelectedItem(tabelKeperluan);

        jComboBoxGedung.setEnabled(false);
        jDateTanggalPeminjaman.setEnabled(false);
        jDateTanggalSelesai.setEnabled(false);
        jComboBoxKegiatan.setEnabled(false);
    }                                            

    private void jComboBoxGedungActionPerformed(java.awt.event.ActionEvent evt) {                                                

    }                                               

    private void jDateTanggalSelesaiMouseClicked(java.awt.event.MouseEvent evt) {                                                 
        jDateTanggalSelesai.setEnabled(false);
    }                                                

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {                                           

        try {
            String idUserMahasiswa = idUser;
            Date tanggalPeminjaman = jDateTanggalPeminjaman.getDate();
            Date tanggalSelesai = jDateTanggalSelesai.getDate();
            String keperluan = jComboBoxKegiatan.getSelectedItem().toString();
            String status = "Pending";
            int gedungId = gedungController.getSelectedGedungId();
            String staffId = "12345";
            java.sql.Date sqlDatePeminjaman = new java.sql.Date(tanggalPeminjaman.getTime());
            java.sql.Date sqlDateSelesai = new java.sql.Date(tanggalSelesai.getTime());
            PeminjamanController.addPeminjaman(sqlDatePeminjaman, sqlDateSelesai, keperluan, status, gedungId, idUserMahasiswa, staffId);

            showPeminjamanData(idUser);

        } catch (Exception ex) {
        }
    }                                          

    private void jButtonBatalActionPerformed(java.awt.event.ActionEvent evt) {                                             

        jComboBoxGedung.setSelectedIndex(0);
        jDateTanggalPeminjaman.setDate(null);
        jDateTanggalSelesai.setDate(null);
        jComboBoxKegiatan.setSelectedIndex(0);

        jButtonAdd.setEnabled(true);
        jComboBoxGedung.setEnabled(true);
        jDateTanggalPeminjaman.setEnabled(true);
        jDateTanggalSelesai.setEnabled(true);
        jComboBoxKegiatan.setEnabled(true);
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
        FlatLightLaf.setup();
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(MenuStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(MenuStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(MenuStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MenuStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MenuMahasiswa().setVisible(true);
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel HistoryPeminjaman;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel Jabatan;
    private javax.swing.JLabel NIP;
    private javax.swing.JLabel Nama;
    private javax.swing.JLabel NamaUser;
    private javax.swing.JLabel NoTelepon;
    private javax.swing.JLabel NoTelepon1;
    private javax.swing.JPanel PeminjamanPanel;
    private javax.swing.JTable TabelHistory;
    private javax.swing.JTable TabelPeminjaman;
    private javax.swing.JPanel allMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonBatal;
    private javax.swing.JButton jButtonCari;
    private javax.swing.JComboBox<String> jComboBoxCariGedung;
    private javax.swing.JComboBox<String> jComboBoxGedung;
    private javax.swing.JComboBox<String> jComboBoxKegiatan;
    private com.toedter.calendar.JDateChooser jDateTAwal;
    private com.toedter.calendar.JDateChooser jDateTanggalPeminjaman;
    private com.toedter.calendar.JDateChooser jDateTanggalSelesai;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFakultas;
    private javax.swing.JLabel jLabelNIM;
    private javax.swing.JLabel jLabelNama;
    private javax.swing.JLabel jLabelNoTelepon1;
    private javax.swing.JLabel jLabelNoTelepon2;
    private javax.swing.JLabel jLabelNoTelepon3;
    private javax.swing.JLabel jLabelNoTelepon4;
    private javax.swing.JLabel jLabelNoTelepon5;
    private javax.swing.JLabel jLabelNoTelepon8;
    private javax.swing.JLabel jLabelProgramStudi;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTableHasilCari;
    private javax.swing.JButton menuHistory;
    private javax.swing.JButton menuHome;
    private javax.swing.JButton menuPeminjaman;
    private javax.swing.JPanel sidepanel;
    // End of variables declaration                   
}

```
- MenuStaff: Ini adalah tampilan yang berbeda untuk mahasiswa dan staff, menyediakan antarmuka pengguna yang disesuaikan dengan peran mereka di dalam sistem. Menu ini mungkin menampilkan opsi yang berbeda, seperti pemesanan gedung bagi mahasiswa dan manajemen gedung untuk staff.
```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.GedungController;
import Controller.PeminjamanController;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import javax.swing.JOptionPane;


/**
 *
 * @author Novi
 */
public class MenuStaff extends javax.swing.JFrame {
    GedungController gedungController = new GedungController();
    
    private String idUser;
    private int idPeminjaman;

    /**
     * Creates new form Home
     */
    public MenuStaff() {
        initComponents();
        
        setExtendedState(JFrame.MAXIMIZED_HORIZ);
        setVisible(true);
        setResizable(false);
       
        showPeminjamanData();
        displayStaffProfile(idUser, idUser, idUser, idUser);
        gedungController.setJComboBoxModel(jComboBoxCariGedung);
        
        
    }
    
    public String getidUser(String idUser){
        return this.idUser = idUser;
    }
    
    private void showPeminjamanData() {
        DefaultTableModel tableModel = (DefaultTableModel) TabelPeminjaman.getModel();
        tableModel.setRowCount(0);
        PeminjamanController.showPeminjaman(tableModel);
    }
     
    private void showHistoryPeminjaman() {
        DefaultTableModel tableModel = (DefaultTableModel) TabelHistory.getModel();
        tableModel.setRowCount(0);
        PeminjamanController.showHistory(tableModel);
    }
    
    
    public final void displayStaffProfile(String idUser, String jabatan, String userNama, String userNoTelepon) {
        NamaUser.setText(userNama);
        jLabelNIP.setText(idUser);
        jLabelJabatan.setText(jabatan);
        jLabelNama.setText(userNama);
        jLabelNoTelepon.setText(userNoTelepon);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        sidepanel = new javax.swing.JPanel();
        NamaUser = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        menuPeminjaman = new javax.swing.JButton();
        menuHistory = new javax.swing.JButton();
        menuHome = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        allMenu = new javax.swing.JPanel();
        HomePanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        NIP = new javax.swing.JLabel();
        NoTelepon = new javax.swing.JLabel();
        Nama = new javax.swing.JLabel();
        jLabelNama = new javax.swing.JLabel();
        Jabatan = new javax.swing.JLabel();
        jLabelJabatan = new javax.swing.JLabel();
        jLabelNoTelepon = new javax.swing.JLabel();
        jLabelNIP = new javax.swing.JLabel();
        jLabelNoTelepon1 = new javax.swing.JLabel();
        jLabelNoTelepon2 = new javax.swing.JLabel();
        jLabelNoTelepon3 = new javax.swing.JLabel();
        jLabelNoTelepon4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateTAwal = new com.toedter.calendar.JDateChooser();
        jComboBoxCariGedung = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableHasilCari = new javax.swing.JTable();
        jButtonCari = new javax.swing.JButton();
        PeminjamanPanel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jButtonDelete = new javax.swing.JButton();
        jTextFieldNIMMahasiswa = new javax.swing.JTextField();
        jButtonBatal = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jButtonAdd = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jDateTanggalPeminjaman = new com.toedter.calendar.JDateChooser();
        jDateTanggalSelesai = new com.toedter.calendar.JDateChooser();
        jComboBoxGedung = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jComboBoxStatus = new javax.swing.JComboBox<>();
        jButtonUpdate = new javax.swing.JButton();
        jComboBoxKegiatan = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelPeminjaman = new javax.swing.JTable();
        HistoryPeminjaman = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelHistory = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(900, 0));
        setResizable(false);

        sidepanel.setBackground(new java.awt.Color(11, 36, 71));
        sidepanel.setForeground(new java.awt.Color(255, 255, 255));
        sidepanel.setPreferredSize(new java.awt.Dimension(200, 600));

        NamaUser.setForeground(new java.awt.Color(255, 255, 255));
        NamaUser.setText("Nama Staff");

        jLabel10.setForeground(new java.awt.Color(102, 204, 255));
        jLabel10.setText("Staff");

        menuPeminjaman.setText("Peminjaman");
        menuPeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPeminjamanActionPerformed(evt);
            }
        });

        menuHistory.setText("History");
        menuHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHistoryActionPerformed(evt);
            }
        });

        menuHome.setText("Home");
        menuHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuHomeActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/user_6994648.png"))); // NOI18N

        javax.swing.GroupLayout sidepanelLayout = new javax.swing.GroupLayout(sidepanel);
        sidepanel.setLayout(sidepanelLayout);
        sidepanelLayout.setHorizontalGroup(
            sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidepanelLayout.createSequentialGroup()
                .addGroup(sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sidepanelLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(menuHome, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(menuPeminjaman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(menuHistory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(sidepanelLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(sidepanelLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel10))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(sidepanelLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(NamaUser)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        sidepanelLayout.setVerticalGroup(
            sidepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidepanelLayout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(NamaUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addGap(27, 27, 27)
                .addComponent(menuHome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(menuPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(menuHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(153, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(23, 107, 135));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 100));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Heavy", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Sistem Informasi Peminjaman Gedung");

        jButton1.setText("Log Out");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(58, 58, 58))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jButton1))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        allMenu.setMinimumSize(new java.awt.Dimension(800, 500));
        allMenu.setPreferredSize(new java.awt.Dimension(800, 500));
        allMenu.setLayout(new java.awt.CardLayout());

        HomePanel.setBackground(new java.awt.Color(255, 255, 255));
        HomePanel.setPreferredSize(new java.awt.Dimension(900, 477));
        HomePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setForeground(new java.awt.Color(0, 51, 51));

        NIP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NIP.setText("NIP");

        NoTelepon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NoTelepon.setText("No Telepon");

        Nama.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Nama.setText("Nama");

        jLabelNama.setAutoscrolls(true);
        jLabelNama.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabelNamaAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                jLabelNamaAncestorRemoved(evt);
            }
        });

        Jabatan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Jabatan.setText("Jabatan");

        jLabelJabatan.setAutoscrolls(true);

        jLabelNoTelepon.setAutoscrolls(true);

        jLabelNIP.setAutoscrolls(true);
        jLabelNIP.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabelNIPAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                jLabelNIPAncestorRemoved(evt);
            }
        });

        jLabelNoTelepon1.setText(": ");
        jLabelNoTelepon1.setAutoscrolls(true);

        jLabelNoTelepon2.setText(": ");
        jLabelNoTelepon2.setAutoscrolls(true);

        jLabelNoTelepon3.setText(": ");
        jLabelNoTelepon3.setAutoscrolls(true);

        jLabelNoTelepon4.setText(": ");
        jLabelNoTelepon4.setAutoscrolls(true);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/user_6994648.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("PROFIL STAFF");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 72, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NIP)
                    .addComponent(Jabatan)
                    .addComponent(NoTelepon)
                    .addComponent(Nama)
                    .addComponent(jLabel6))
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabelNoTelepon3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelNoTelepon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabelNoTelepon4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelJabatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabelNoTelepon2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabelNoTelepon1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelNIP, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(53, 53, 53))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel7)))
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelNIP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NIP, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNoTelepon1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelNama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Nama, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(jLabelNoTelepon2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelJabatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Jabatan, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addComponent(jLabelNoTelepon4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelNoTelepon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NoTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNoTelepon3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(75, Short.MAX_VALUE))
        );

        HomePanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 410, 360));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("SISTEM INFORMASI JADWAL GEDUNG");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("UNIVERSITAS MULAWARMAN");

        jDateTAwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateTAwalMouseClicked(evt);
            }
        });

        jComboBoxCariGedung.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxCariGedung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCariGedungActionPerformed(evt);
            }
        });

        jTableHasilCari.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableHasilCari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Gedung", "Tanggal Awal", "Tanggal Akhir", "Kegiatan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableHasilCari.setEnabled(false);
        jTableHasilCari.setRowSelectionAllowed(false);
        jTableHasilCari.setShowGrid(true);
        jTableHasilCari.getTableHeader().setResizingAllowed(false);
        jTableHasilCari.getTableHeader().setReorderingAllowed(false);
        jTableHasilCari.setUpdateSelectionOnSort(false);
        jTableHasilCari.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(jTableHasilCari);

        jButtonCari.setText("Cek Jadwal");
        jButtonCari.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButtonCariFocusGained(evt);
            }
        });
        jButtonCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateTAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxCariGedung, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonCari))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jLabel4)))
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(58, 58, 58))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jDateTAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBoxCariGedung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonCari, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        HomePanel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 70, 450, 360));

        allMenu.add(HomePanel, "card4");

        PeminjamanPanel.setBackground(new java.awt.Color(255, 255, 255));
        PeminjamanPanel.setMaximumSize(new java.awt.Dimension(1000, 600));
        PeminjamanPanel.setMinimumSize(new java.awt.Dimension(1000, 600));
        PeminjamanPanel.setPreferredSize(new java.awt.Dimension(1000, 600));

        jLabel21.setText("NIM Mahasiswa");

        jButtonDelete.setText("Hapus");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jTextFieldNIMMahasiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNIMMahasiswaActionPerformed(evt);
            }
        });

        jButtonBatal.setText("Batal");
        jButtonBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBatalActionPerformed(evt);
            }
        });

        jLabel22.setText("Gedung");

        jButtonAdd.setText("Tambah");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jLabel23.setText("Tanggal Peminjaman");

        jLabel24.setText("Tanggal Selesai");

        jLabel25.setText("Keperluan");

        jDateTanggalSelesai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateTanggalSelesaiMouseClicked(evt);
            }
        });

        jComboBoxGedung.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxGedung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGedungActionPerformed(evt);
            }
        });

        jLabel26.setText("Status");

        jComboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Approved", "Rejected" }));
        jComboBoxStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxStatusMouseClicked(evt);
            }
        });
        jComboBoxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxStatusActionPerformed(evt);
            }
        });

        jButtonUpdate.setText("Ubah");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jComboBoxKegiatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seminar", "Lomba", "Rapat", "Kuliah Umum" }));

        TabelPeminjaman.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Peminjaman", "NIM", "Gedung", "Tanggal Peminjaman", "Tanggal Selesai", "Keperluan", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelPeminjaman.getTableHeader().setResizingAllowed(false);
        TabelPeminjaman.getTableHeader().setReorderingAllowed(false);
        TabelPeminjaman.setUpdateSelectionOnSort(false);
        TabelPeminjaman.setVerifyInputWhenFocusTarget(false);
        TabelPeminjaman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelPeminjamanMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(TabelPeminjaman);

        javax.swing.GroupLayout PeminjamanPanelLayout = new javax.swing.GroupLayout(PeminjamanPanel);
        PeminjamanPanel.setLayout(PeminjamanPanelLayout);
        PeminjamanPanelLayout.setHorizontalGroup(
            PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(49, 49, 49)
                                .addComponent(jComboBoxKegiatan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22))
                                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldNIMMahasiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PeminjamanPanelLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBoxGedung, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(230, 230, 230)
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel26)
                            .addComponent(jLabel23))
                        .addGap(36, 36, 36)
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jDateTanggalPeminjaman, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                            .addComponent(jDateTanggalSelesai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                            .addComponent(jButtonAdd)
                            .addGap(18, 18, 18)
                            .addComponent(jButtonUpdate)
                            .addGap(18, 18, 18)
                            .addComponent(jButtonDelete)
                            .addGap(18, 18, 18)
                            .addComponent(jButtonBatal))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 59, Short.MAX_VALUE))
        );
        PeminjamanPanelLayout.setVerticalGroup(
            PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                .addContainerGap(153, Short.MAX_VALUE)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                        .addComponent(jDateTanggalPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateTanggalSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(30, 30, 30)
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jComboBoxGedung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)))
                    .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldNIMMahasiswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)))
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PeminjamanPanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel25))
                    .addGroup(PeminjamanPanelLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel26))
                            .addComponent(jComboBoxKegiatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(43, 43, 43)
                .addGroup(PeminjamanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdd)
                    .addComponent(jButtonUpdate)
                    .addComponent(jButtonDelete)
                    .addComponent(jButtonBatal))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        allMenu.add(PeminjamanPanel, "card3");

        HistoryPeminjaman.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        TabelHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Peminjaman", "NIM", "Nama Gedung", "Tanggal Peminjaman", "Tanggal Selesai", "Keperluan", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelHistory.setEnabled(false);
        TabelHistory.getTableHeader().setResizingAllowed(false);
        TabelHistory.getTableHeader().setReorderingAllowed(false);
        TabelHistory.setUpdateSelectionOnSort(false);
        TabelHistory.setVerifyInputWhenFocusTarget(false);
        jScrollPane6.setViewportView(TabelHistory);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 875, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 419, Short.MAX_VALUE)
                    .addComponent(jLabel39)
                    .addGap(0, 419, Short.MAX_VALUE)))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 419, Short.MAX_VALUE)
                    .addComponent(jLabel40)
                    .addGap(0, 419, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 240, Short.MAX_VALUE)
                    .addComponent(jLabel39)
                    .addGap(0, 240, Short.MAX_VALUE)))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(0, 240, Short.MAX_VALUE)
                    .addComponent(jLabel40)
                    .addGap(0, 240, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout HistoryPeminjamanLayout = new javax.swing.GroupLayout(HistoryPeminjaman);
        HistoryPeminjaman.setLayout(HistoryPeminjamanLayout);
        HistoryPeminjamanLayout.setHorizontalGroup(
            HistoryPeminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoryPeminjamanLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        HistoryPeminjamanLayout.setVerticalGroup(
            HistoryPeminjamanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        allMenu.add(HistoryPeminjaman, "card4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(sidepanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                    .addComponent(allMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sidepanel, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(allMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void menuHomeActionPerformed(java.awt.event.ActionEvent evt) {                                         
        allMenu.removeAll();
        allMenu.repaint();
        allMenu.revalidate();
        
        allMenu.add(HomePanel);
        allMenu.repaint();
        allMenu.revalidate();
    }                                        

    private void menuPeminjamanActionPerformed(java.awt.event.ActionEvent evt) {                                               
        allMenu.removeAll();
        allMenu.repaint();
        allMenu.revalidate();
        
        allMenu.add(PeminjamanPanel);
        allMenu.repaint();
        allMenu.revalidate();
        
        gedungController.setJComboBoxModel(jComboBoxGedung);
        jButtonUpdate.setEnabled(false);
        jButtonDelete.setEnabled(false);
        jComboBoxStatus.setEnabled(false);
    }                                              

    private void menuHistoryActionPerformed(java.awt.event.ActionEvent evt) {                                            
        allMenu.removeAll();
        allMenu.repaint();
        allMenu.revalidate();
        
        allMenu.add(HistoryPeminjaman);
        allMenu.repaint();
        allMenu.revalidate();
        
        showHistoryPeminjaman();
    }                                           

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if (TabelPeminjaman.getSelectedRow() != -1) { // Memeriksa apakah ada baris yang dipilih
                if (jButtonUpdate.getText().equals("Simpan")) {
                    jButtonUpdate.setText("Ubah");
                    if (this.idPeminjaman != -1) {
                        String selectedStatus = PeminjamanController.getSelectedStatusFromComboBox(jComboBoxStatus);

                        boolean success = PeminjamanController.updatePeminjamanStatus(selectedStatus, idPeminjaman);

                        if (success) {
                            showPeminjamanData();
                        }
                    }
                } else {
                    jComboBoxStatus.setEnabled(true);
                    jButtonUpdate.setText("Simpan");
                }
            } else {
                // Tambahkan pesan atau tindakan lain jika tidak ada baris yang dipilih
                JOptionPane.showMessageDialog(this, "Pilih baris dalam tabel untuk melakukan perubahan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
    }                                             

    private void TabelPeminjamanMouseClicked(java.awt.event.MouseEvent evt) {                                             
        jButtonAdd.setEnabled(false);
        jButtonUpdate.setEnabled(true);
        jButtonDelete.setEnabled(true);

        DefaultTableModel tblModel = (DefaultTableModel)TabelPeminjaman.getModel();
        int idPeminjaman1 = (int) tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 0);
        this.idPeminjaman = idPeminjaman1;

        System.out.println(idPeminjaman);
        String tabelNIM = tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 1).toString();
        String tabelGedung = tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 2).toString();
        Date tabelTanggalP = (Date) tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 3);
        Date tabelTanggalS = (Date) tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 4);
        String tabelKeperluan = tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 5).toString();
        String tabelStatus = tblModel.getValueAt(TabelPeminjaman.getSelectedRow(), 6).toString();

        jTextFieldNIMMahasiswa.setText(tabelNIM);
        jComboBoxGedung.setSelectedItem(tabelGedung);
        jDateTanggalPeminjaman.setDate(tabelTanggalP);
        jDateTanggalSelesai.setDate(tabelTanggalS);
        jComboBoxKegiatan.setSelectedItem(tabelKeperluan);
        jComboBoxStatus.setSelectedItem(tabelStatus);

        jTextFieldNIMMahasiswa.setEnabled(false);
        jComboBoxGedung.setEnabled(false);
        jDateTanggalPeminjaman.setEnabled(false);
        jDateTanggalSelesai.setEnabled(false);
        jComboBoxKegiatan.setEnabled(false);
        jComboBoxStatus.setEnabled(false);
    }                                            

    private void jComboBoxGedungActionPerformed(java.awt.event.ActionEvent evt) {                                                

    }                                               

    private void jDateTanggalSelesaiMouseClicked(java.awt.event.MouseEvent evt) {                                                 
        jDateTanggalSelesai.setEnabled(false);
    }                                                

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {                                           

        try {
            String idUserMahasiswa = jTextFieldNIMMahasiswa.getText();
            Date tanggalPeminjaman = jDateTanggalPeminjaman.getDate();
            Date tanggalSelesai = jDateTanggalSelesai.getDate();
            String keperluan = jComboBoxKegiatan.getSelectedItem().toString();
            String status = "Pending";
            int gedungId = gedungController.getSelectedGedungId();
            String staffId = idUser;
            java.sql.Date sqlDatePeminjaman = new java.sql.Date(tanggalPeminjaman.getTime());
            java.sql.Date sqlDateSelesai = new java.sql.Date(tanggalSelesai.getTime());
            PeminjamanController.addPeminjaman(sqlDatePeminjaman, sqlDateSelesai, keperluan, status, gedungId, idUserMahasiswa, staffId);

            showPeminjamanData();
            
        } catch (Exception ex) {
        }
    }                                          

    private void jButtonBatalActionPerformed(java.awt.event.ActionEvent evt) {                                             
        jTextFieldNIMMahasiswa.setText("");
        jComboBoxGedung.setSelectedIndex(0);
        jDateTanggalPeminjaman.setDate(null);
        jDateTanggalSelesai.setDate(null);
        jComboBoxKegiatan.setSelectedIndex(0);
        jComboBoxStatus.setSelectedIndex(0);

        jButtonAdd.setEnabled(true);
        jTextFieldNIMMahasiswa.setEnabled(true);
        jComboBoxGedung.setEnabled(true);
        jDateTanggalPeminjaman.setEnabled(true);
        jDateTanggalSelesai.setEnabled(true);
        jComboBoxKegiatan.setEnabled(true);
        jComboBoxStatus.setEnabled(true);
        
        jButtonUpdate.setText("Ubah");
        TabelPeminjaman.clearSelection();
    }                                            

    private void jTextFieldNIMMahasiswaActionPerformed(java.awt.event.ActionEvent evt) {                                                       

    }                                                      

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {                                              
        int selectedRowIndex = TabelPeminjaman.getSelectedRow();

        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih data yang ingin dihapus");
        } else {
            int result = JOptionPane.showConfirmDialog(this, "Apakah anda yakin ingin menghapus data?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                PeminjamanController.deletePeminjaman(idPeminjaman);
                showPeminjamanData();
            }
        }
    }                                             

    private void jLabelNIPAncestorRemoved(javax.swing.event.AncestorEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void jLabelNIPAncestorAdded(javax.swing.event.AncestorEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void jLabelNamaAncestorRemoved(javax.swing.event.AncestorEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void jLabelNamaAncestorAdded(javax.swing.event.AncestorEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void jComboBoxCariGedungActionPerformed(java.awt.event.ActionEvent evt) {                                                    
        // TODO add your handling code here:
    }                                                   

    private void jDateTAwalMouseClicked(java.awt.event.MouseEvent evt) {                                        

    }                                       

    private void jButtonCariActionPerformed(java.awt.event.ActionEvent evt) {                                            

        int idGedung = gedungController.getSelectedGedungId();
        Date tanggalPeminjaman = jDateTAwal.getDate();

        DefaultTableModel tableModel = gedungController.cariGedung(idGedung, tanggalPeminjaman);
        jTableHasilCari.setModel(tableModel);

    }                                           

    private void jButtonCariFocusGained(java.awt.event.FocusEvent evt) {                                        

    }                                       

    private void jComboBoxStatusMouseClicked(java.awt.event.MouseEvent evt) {                                             

    }                                            

    private void jComboBoxStatusActionPerformed(java.awt.event.ActionEvent evt) {                                                
       
    }                                               

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        Login login = new Login();
        
        this.dispose();
        login.setVisible(true);
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
        FlatLightLaf.setup();
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(MenuStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(MenuStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(MenuStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MenuStaff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MenuStaff().setVisible(true);
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JPanel HistoryPeminjaman;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel Jabatan;
    private javax.swing.JLabel NIP;
    private javax.swing.JLabel Nama;
    private javax.swing.JLabel NamaUser;
    private javax.swing.JLabel NoTelepon;
    private javax.swing.JPanel PeminjamanPanel;
    private javax.swing.JTable TabelHistory;
    private javax.swing.JTable TabelPeminjaman;
    private javax.swing.JPanel allMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonBatal;
    private javax.swing.JButton jButtonCari;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JComboBox<String> jComboBoxCariGedung;
    private javax.swing.JComboBox<String> jComboBoxGedung;
    private javax.swing.JComboBox<String> jComboBoxKegiatan;
    private javax.swing.JComboBox<String> jComboBoxStatus;
    private com.toedter.calendar.JDateChooser jDateTAwal;
    private com.toedter.calendar.JDateChooser jDateTanggalPeminjaman;
    private com.toedter.calendar.JDateChooser jDateTanggalSelesai;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelJabatan;
    private javax.swing.JLabel jLabelNIP;
    private javax.swing.JLabel jLabelNama;
    private javax.swing.JLabel jLabelNoTelepon;
    private javax.swing.JLabel jLabelNoTelepon1;
    private javax.swing.JLabel jLabelNoTelepon2;
    private javax.swing.JLabel jLabelNoTelepon3;
    private javax.swing.JLabel jLabelNoTelepon4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable jTableHasilCari;
    private javax.swing.JTextField jTextFieldNIMMahasiswa;
    private javax.swing.JButton menuHistory;
    private javax.swing.JButton menuHome;
    private javax.swing.JButton menuPeminjaman;
    private javax.swing.JPanel sidepanel;
    // End of variables declaration                   
}

```
## Package Database
Database dan Konfigurasi

- Koneksi: Class penting yang menyediakan koneksi ke database. Ini adalah bagian kunci dari paket Database dan digunakan oleh semua model untuk melakukan transaksi database.
```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Database;

/**
 *
 * @author Novi
 */
public interface Database {
    public abstract void setQuery(String sql);
    public abstract String getQuery(); 
}

```

```java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Database;

import java.sql.*;

/**
 *
 * @author Novi
 */

public abstract class Koneksi implements Database {
    protected static Connection connection = null;
    protected static Statement statement;
    protected static ResultSet resultSet;
    protected static PreparedStatement preparedStatement;
    private static String query;
    
    public Koneksi() {
        openConnection();
    }
    
    protected static final void openConnection() {
        try {
            final String url = "jdbc:mysql://localhost/peminjaman_gedung?user=root&password=";
            
            connection = DriverManager.getConnection(url);
            
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }
    
    protected static final void closeConnection() {
        try {
            if  (resultSet != null) resultSet.close();
            if  (statement != null) statement.close();
            if  (preparedStatement != null) preparedStatement.close();
            if  (connection != null) connection.close();
            
            resultSet = null;
            statement = null;
            preparedStatement = null;
            connection = null;
        } catch (SQLException ex) {}
    }
    
    protected static final void displayErrors(SQLException ex){
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    
    protected static final Connection getConnection() {
        openConnection();
        return connection;
    }

    protected static final Statement getStatement() {
        return statement;
    }

    protected static final ResultSet getResultSet() {
        return resultSet;
    }

    protected static final PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    protected static final void setStatement(Statement stm) {
        statement = stm;
    }
    
    @Override
    public void setQuery(String sql) {
        query = sql;
    }

    @Override
    public String getQuery() {
        return query;
    }
     
}
```

Konfigurasi NetBeans: File seperti project.xml dan build-impl.xml mengonfigurasi cara proyek dibangun dan dijalankan di dalam NetBeans IDE. Ini mencakup pengaturan path, library yang digunakan, dan konfigurasi khusus proyek.

Ekstra dan Library

- Library Eksternal: Aplikasi ini menggunakan beberapa library Java seperti jcalendar untuk memilih tanggal, dan mysql-connector-java untuk koneksi database. Ini memperluas fungsionalitas dasar Java dan menyediakan komponen UI yang lebih canggih dan pengelolaan database yang efisien.

File Eksekusi: File JAR dalam direktori dist adalah versi yang dapat dijalankan dari aplikasi, yang telah dikompilasi dan siap untuk didistribusikan atau dijalankan oleh pengguna akhir.

Aplikasi ini dirancang dengan pemisahan tanggung jawab yang jelas, memungkinkan perawatan dan pengembangan yang teratur. Dengan menggunakan pola MVC, aplikasi ini memastikan bahwa antarmuka pengguna, logika bisnis, dan manipulasi data terpisah, yang memudahkan pengujian dan pengembangan lebih lanjut.

Setiap komponen bekerja sama untuk menyediakan pengalaman pengguna yang lancar, dari login ke sistem, pengelolaan gedung, hingga proses peminjaman. Arsitektur aplikasi ini mendukung skalabilitas, memungkinkan penambahan fitur baru atau penyesuaian dengan relatif mudah.

# Output

Tampilan Login
Sebagaimana dijelaskan sebelumnya, gambar ini menunjukkan tampilan login yang meminta pengguna untuk memasukkan nama pengguna dan kata sandi mereka. Hal ini untuk memastikan bahwa hanya pengguna terotorisasi yang dapat mengakses fitur aplikasi.

Dashboard Utama
merupakan tampilan setelah pengguna berhasil masuk, menampilkan dashboard atau menu utama. Ini bisa menunjukkan berbagai opsi yang tersedia bagi pengguna, seperti melihat daftar gedung yang dapat dipinjam, melakukan pemesanan gedung, atau mengakses riwayat peminjaman.

Formulir Peminjaman Gedung
Gambar ini menampilkan formulir yang harus diisi saat pengguna ingin meminjam gedung. Formulir ini meminta informasi seperti nama gedung, tanggal peminjaman, durasi, dan tujuan peminjaman.

Kalender Peminjaman
Menunjukkan kalender yang memungkinan pengguna untuk melihat tanggal yang tersedia untuk peminjaman gedung. Pengguna dapat memilih tanggal dan melihat gedung yang tersedia atau sudah dipesan pada tanggal tersebut.

Konfirmasi Peminjaman
Gambar ini menampilkan ringkasan atau konfirmasi detail peminjaman sebelum pengguna menyelesaikan proses peminjaman. Ini memastikan bahwa semua informasi yang dimasukkan benar dan memungkinkan pengguna untuk melakukan perubahan jika diperlukan.

Manajemen Gedung
Bagi pengguna dengan peran administratif, seperti staff, gambar ini menunjukkan antarmuka untuk mengelola informasi gedung, termasuk menambahkan gedung baru, mengedit informasi gedung, atau menghapus gedung dari sistem.

Laporan dan Statistik
Gambar ini menunjukkan laporan atau statistik tentang peminjaman gedung. Ini bisa termasuk data seperti jumlah peminjaman per periode waktu, gedung yang paling sering dipinjam, atau tren lain yang berguna untuk analisis administratif.

Pengaturan Akun Pengguna
Merupakan tampilan pengaturan akun pengguna, memungkinkan pengguna untuk mengubah informasi pribadi mereka, kata sandi, atau preferensi lain yang terkait dengan akun mereka di aplikasi.
