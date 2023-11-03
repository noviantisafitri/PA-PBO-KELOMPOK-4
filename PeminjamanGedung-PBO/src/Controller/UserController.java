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
public class UserController extends UserModel {

    public static boolean loginUser(String username, String password, String role) {
        return authenticateUser(username, password, role);  
    }   
    
    public static String getUserId() {
        return getIdUser();
    }
    
    public static String getUserNama() {
        return getNama();
    }
    
    public static String getUserNoTelepon() {
        return getNoTelepon();
    }
    
    public static boolean getIdMahasiswa(String nim) {
        return findMahasiswa(nim);
    }   
}
