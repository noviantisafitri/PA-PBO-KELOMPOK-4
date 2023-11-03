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
