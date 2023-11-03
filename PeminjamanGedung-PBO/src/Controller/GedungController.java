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