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
- ERD Logical
   
![Logical](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/126859339/4bb8dbab-c531-4280-b389-d3267c7237fc)

- Erd Relational
  
![Relational_1](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/126859339/9001fe4d-111f-4e8f-9d79-7edb36fdde32)


# Hirarki Class

![image](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/0594ed7b-bdfd-4dc7-b584-f0ca47f10b58)

# Struktur Project

<img width="209" alt="image" src="https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/2f30a981-91dc-47d2-86ae-88625095d34a">


# SourceCode dan Penjelasan

## Package Model

- GedungModel: Bertanggung jawab atas representasi data gedung, termasuk ID, nama, dan kapasitas. Kode berikut meerupakan kode yang mengatur logika dari metode yang dapat menampilkan, mencari, dan mengatur model JComboBox dengan data gedung dari database. Pada class ini terdapat penerapan setter dan getter. Code berikut sudah menerapkan pola orm. Pada code ini terdapat penggunaan DefaultTabel dan JcomboBox yang digunakan untuk menampilkan data.
- 
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
- UserModel: Pada class ini berisi kode yang mengatur informasi pengguna aplikasi. Kode tersebut termasuk pengelolaan kredensial pengguna, fungsi login, dan mempertahankan role pengguna. Class ini juga dapat menangani peran pengguna (misalnya, mahasiswa atau staff) dan membatasi akses ke fitur tertentu berdasarkan peran tersebut. Kode berikut adalah kode yang memperluas kelas koneksi database. Ini mencakup properti, getter, setter, dan metode untuk otentikasi pengguna serta pengambilan data dari tabel staff dan mahasiswa. Pada kode berikut juga sudah menerapkan penggunaan final dan static pada method.
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

- PeminjamanModel: Class ini berisi code yang digunakan untuk mengelola data peminjaman gedung, seperti informasi peminjam, tanggal peminjaman, dan keperluan peminjaman. Class ini berinteraksi dengan database untuk menyimpan dan mengambil peminjaman, serta mengelola status dan detail peminjaman gedung. Kode berikut adalah logika untuk mengelola peminjaman gedung. Termasuk metode untuk membuat, memperbarui, menghapus, dan menampilkan data peminjaman berdasarkan status dan peran pengguna.

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

        } catch (SQLException ex) {
            displayErrors(ex);
        } finally {
            closeConnection();
        }
    }
}

```
## Package Controller

- GedungController merupakan class yang berfungsi Sebagai penghubung antara GedungModel dan tampilan yang berkaitan dengan gedung, class ini mengelola logika bisnis untuk operasi gedung. Ini termasuk mengambil input pengguna, memvalidasi data, dan memanggil metode yang relevan pada GedungModel untuk menjalankan operasi database.
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

- PeminjamanController berfungsi sebagai pengontrol untuk operasi peminjaman. Ini memanggil metode dari kelas PeminjamanModel untuk menampilkan, menambah, menghapus, dan memperbarui status peminjaman, serta melakukan validasi tanggal. Pada code ini terdapat beberapa logika yang diatur untuk mengelola peminjaman agar tidak ada jadwal peminjaman yang bertabrakan.
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
- UserController merupakan class yang berfungsi untuk mengontrol operasi terkait pengguna. Ini memanggil metode dari kelas UserModel untuk otentikasi pengguna, mendapatkan informasi pengguna.
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
- StaffController merupakan class yang berfungsi menyimpan code dari atribut staff yang akan ditampilkan diprofile.
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
- MahasiswaController merupakan class yang berfungsi menyimpan code dari atribut mahasiswa yang akan ditampilkan diprofile.
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

## Package Database
Database dan Konfigurasi

- Koneksi: Class penting yang menyediakan koneksi ke database. Ini adalah bagian kunci dari paket Database dan digunakan oleh semua model untuk melakukan transaksi database. Pada class ini menerapkan interface dan class abscstract beserta override.
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
 
![image](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/bdb6bc9b-a03e-47ff-a1b5-52d37a95645d)


Dashboard Utama
merupakan tampilan setelah pengguna berhasil masuk, menampilkan dashboard atau menu utama. Ini bisa menunjukkan berbagai opsi yang tersedia bagi pengguna, seperti melihat daftar gedung yang dapat dipinjam, melakukan pemesanan gedung, atau mengakses riwayat peminjaman.

![image](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/a6b7d25d-56e2-4a72-b851-0cfd16ccd9af)

Formulir Peminjaman Gedung
Gambar ini menampilkan formulir yang harus diisi saat pengguna ingin meminjam gedung. Formulir ini meminta informasi seperti nama gedung, tanggal peminjaman, durasi, dan tujuan peminjaman.

![image](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/7a73293e-2a19-45f3-a656-740dd64374ca)


Manajemen Gedung
Bagi pengguna dengan peran administratif, seperti staff, gambar ini menunjukkan antarmuka untuk mengelola informasi gedung, termasuk menambahkan gedung baru, mengedit informasi gedung, atau menghapus gedung dari sistem.

![image](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/0206e0aa-ba51-47ce-ac2c-8208b8c17b08)


Laporan dan Statistik
Gambar ini menunjukkan history tentang peminjaman gedung. Ini bisa termasuk data seperti jumlah peminjaman per periode waktu, gedung yang paling sering dipinjam, atau tren lain yang berguna untuk analisis administratif.

![image](https://github.com/noviantisafitri/PA-PBO-KELOMPOK-4/assets/121856489/838083a8-bc6e-4bb5-b798-ccb7fd285301)


