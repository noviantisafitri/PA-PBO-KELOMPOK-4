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
