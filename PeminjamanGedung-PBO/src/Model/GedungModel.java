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