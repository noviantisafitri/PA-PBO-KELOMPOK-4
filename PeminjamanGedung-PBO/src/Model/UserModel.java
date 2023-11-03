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