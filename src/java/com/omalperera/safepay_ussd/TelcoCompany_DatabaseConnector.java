/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omalperera.safepay_ussd;


import java.sql.*;

/**
 *
 * @author Omal Perera
 * Date : 11.05.2015
 * 
 * This class is used to verify the user as a REGISTERD USER or UN-REGISTERD USER
 *
 */




public class TelcoCompany_DatabaseConnector {
    
    
    
 /**
  * 
  * @param mobileNum
  * @return registerState - if database has the value -> TRUE
  * @param username = username to access tha database
  * @param password = password to access the database
  * @param jdbc:mysql://localhost/Telco_Company is the URL where database is exist. 
  * 
  */   
    public static boolean Telco_DB(String mobileNum) {
                                                                                //here mobile number will be in a formate of tel:0123654789      
    final String mobileNumber = mobileNum;
    
    Connection conn_Telco = null;                                               // creating connection to the database
    boolean registerState = false;                                              // set default register status to false
    
        try {
            String userName = "root";
            String password = null;
           
            
            String Telco_url = "jdbc:mysql://localhost/Telco_Company";
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn_Telco = DriverManager.getConnection(Telco_url, userName, password);
            
            Statement stmt;
            ResultSet rset;
            
            stmt = conn_Telco.createStatement();
            String queryStatement = "SELECT mobile_number FROM SafePay_RegisterdCus WHERE mobile_number = '"+mobileNumber+"'";
            rset = stmt.executeQuery(queryStatement);
            
            
            while (rset.next()) {                 //if the mobile number is present in the registed customer database, set the state to registed.
                registerState = true;
            }
            
            rset.close();
            conn_Telco.close();
            
            
          
        } catch (Exception e) {
        }
        return registerState;

    } 
}
