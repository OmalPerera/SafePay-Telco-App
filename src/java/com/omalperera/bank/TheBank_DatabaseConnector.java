/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omalperera.bank;




import com.omalperera.safepay_ussd.*;
import com.omalperera.creditcardsimulator.sampleSimulator;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Omal Perera
 */



public class TheBank_DatabaseConnector {
    
    
 
    
    
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ START : Activate/Deactive the card +++++++++++ */
    
    public static boolean Changing_Active_Mode(String mobileNum){
        
        boolean activateStatus = false;
        Connection conn_Bank = null;
        
        // Activates the card 
        try {
            String userName = "root";
            String password = null;
            
            String bank_url = "jdbc:mysql://localhost/thebank";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn_Bank = DriverManager.getConnection(bank_url, userName, password);
            
            Statement stmt_bank;
            
            stmt_bank = conn_Bank.createStatement();
            
            stmt_bank.execute("UPDATE TheBank_SafePay_Customers "
                            + "SET operation_Mode = 'ACTIVATED' "
                            + "WHERE mobile_number = '" + mobileNum + "';"
            );
            
           
            
            
        /************************************************************* checks the operation mode (ACTIVATED OR DEACTIVATED) ******************/
            /**
             * Although in above steps operation is successfully changed,
             * here again checks the operation mode from the original database.
            */
            
            String queryStatement = "SELECT operation_Mode "
                                  + "FROM TheBank_SafePay_Customers "
                                  + "WHERE mobile_number = '"+mobileNum+"'";
            
            ResultSet rset;
            rset = stmt_bank.executeQuery(queryStatement);
            
            
            while (rset.next()) {
                if("ACTIVATED".equals(rset.getString("operation_Mode"))){
                    activateStatus = true;
                }
            }
            conn_Bank.close();
            
            Deactivate_Timer(mobileNum); // call the method to deactivate the card

            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
        

        return activateStatus;
        
        
        
        
    }

/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ END : Activate/Deactive the card +++++++++++ */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ START : Deactive Timer +++++++++++ */
     
    
    /**
     * Deactivate_Timer is used to calculate the time that keeps the card Actively
     * 
     */
 
    private static final ScheduledExecutorService deactivator = Executors.newSingleThreadScheduledExecutor();
    
    public static void Deactivate_Timer(String mobileNum){
    
        
        Runnable task = new Runnable() {

            @Override
            public void run() {
                
            
                Connection conn_Bank = null;
        
                // Activates the card 
                try {
                    String userName = "root";
                    String password = null;
            
                    String bank_url = "jdbc:mysql://localhost/thebank";
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conn_Bank = DriverManager.getConnection(bank_url, userName, password);
            
                    Statement stmt_bank;
            
                    stmt_bank = conn_Bank.createStatement();
            
                    stmt_bank.execute("UPDATE TheBank_SafePay_Customers "
                                    + "SET operation_Mode = 'DEACTIVATED' "
                                    + "WHERE mobile_number = '" + mobileNum + "';"
                    );
            
                    conn_Bank.close();
            
                }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e){}
                
            }
        };
        
        deactivator.schedule(task, 60, TimeUnit.SECONDS);

    }
    
    
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ END : Deactive Timer +++++++++++ */
    
    
    
    
    
    
    
    
    
    /**
     * @Author : Omal perera
     *  date   : 21.05.2015
     * 
     * 
     * 
     * HERE I HAVE A QUESTION
     * -----------------------
     * 
     * WHEN A USER REQUEST TO ACTIVATE HIS CARD I CAN OPEN THE DATABASE TO A WRITABLE STATE AND CHANGE THE OPERATION MODE TO "ACTIVE".
     * THEN I CLOSE THE DATABASE CONNECTION.
     * THEN AFTER 5 MINUTES I WILL AUTOMATICALY DEACTIVATES THE CARD. FOR THIS AGAIN I HAVE TO OPEN THE DATABASE TO A WRITABLE CONDITION.
     * 
     * 
     * 
     * SO THERE ARE 2 OPTIONS TO ME.
     * 
     * 1. I CAN KEEP THE DATABASE OPEN UNTIL 5 MINUTES FINISHES AND THEN CHANGES THE OPERATION MODE TO DEACTIVE AND CLOSE IT.
     *      SO IT IS ONLY A ONE TIME I'M OPENING THE DATABASE AND CLOSING THE DATABASE.
     * 
     *      I HOPE IT WILL INCRESES THE EFFICENCY OF THE DATABASE, BECAUSE THERE WILL BE MILLIONS OF CUSTOMERS USING THE BANK
     *      BUT IN THE HAND OF SECURITY ????
     * 
     * 
     * 2. I CAN OPEN THE CONNECTION AND CHANGE IT TO ACTIVATED STATE AS THE USER REQUEST AND CLOSE IT
     *      THEN AGAIN AFTER 5 MINUTES I HAVE TO OPEN THE CONNECTIN AND CHANGE IT TO DEACTIVATED MODE ABD CLOSE IT.
     *      SO I HAVE TO OPEN TWICE AND CLOSE TWICE
     * 
     *      SO DOES IT MAKES AN UNWANTED TRAFFIC IN THE DATABASE ????? WHEN USED BY MILLIONS OF PEOPLE 
     *      IS IT MAKES MORE SECURE THAN ABOVE ????
     * 
     * WHAT IS REALLY A DATABASE CONNECTION ????
     *
     */
                
                
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
                
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ START : Handles the transaction when debiting from the card by a POS +++++++++++ */
     
    
    /**
     * 
     * @param cardNumber - card Number of the credit/debit card
     * @param amount - amount of money that should reduced from the users account.
     * @param sellerID - id given to the pos machine (from this ID bank can get the location where the transaction is 
     *                  happening and who is responsible for that) Ex: Battaramulla Arpico super market has the sellerID as SID0000120
     * 
     */
    
       
    public void Transaction_Handler(String cardNumber, String amount, String sellerID){
        
        Connection conn_Bank = null;
        
        // Activates the card 
        try {
            String userName = "root";
            String password = null;
            
            String bank_url = "jdbc:mysql://localhost/thebank";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn_Bank = DriverManager.getConnection(bank_url, userName, password);
            
            Statement stmt_bank;
            
            stmt_bank = conn_Bank.createStatement();
            
            String queryStatement =   "SELECT operation_Mode, mobile_number "
                                    + "FROM TheBank_SafePay_Customers "
                                    + "WHERE card_number = '" + cardNumber + "'";
           

            
            ResultSet rset;
            rset = stmt_bank.executeQuery(queryStatement);
            
                          
                
            while (rset.next()) {                
                
                if("ACTIVATED".equals(rset.getString("operation_Mode"))) {
                    ProceedTheTransaction();
                
                }else{
                    
                    send_SMS_to_User(rset.getString("mobile_number"));
                    Feedback_to_the_card_simulator();
                }
            }
            conn_Bank.close();
            
            
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        }
        
    }
    
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ START : Handles the transaction when debiting from the card by a POS +++++++++++ */
          
    
    
    
    
    
    
    
                
                
     
    
    /**
     * 
     * sends a text message to the customers phone if the transaction is going to done but card is deactivated
     * 
     * 
     */
    
    private static void send_SMS_to_User(String mobileNumber){
        String msgContent = "Dear Customer, Your Credit / Debit card is going to use at the moment. \nIf that is you, please activate the card by dial #678";
        
        FeedbackSenderAsMsg.sendResponse(mobileNumber, msgContent);
        
    }
                
                
      






    

    private void ProceedTheTransaction(){
        sampleSimulator.FeedBack("Transaction Done Successfully");
    
    }

   private void Feedback_to_the_card_simulator(){
       sampleSimulator.FeedBack("Your card is locked");
       
   }
                
                
                
                
                
                
                
    
    
    
    
    
    
/*        
    public void Bank_DB(){
        
        Connection conn_Bank = null;
        
        try {
            String userName = "root";
            String password = null;
            
            String bank_url = "jdbc:mysql://localhost/thebank";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn_Bank = DriverManager.getConnection(bank_url, userName, password);
            
            Statement stmt_bank;
            ResultSet rset_bank;
            stmt_bank = conn_Bank.createStatement();
            rset_bank = stmt_bank.executeQuery("select * from TheBank_SafePay_Customers");
            
            while (rset_bank.next()) {
                int mobileNo = rset_bank.getInt("mobile_number");
                String card_number = rset_bank.getString("card_number");
                System.out.println(mobileNo +"\t"+ card_number);
                
            }
            rset_bank.close();
            conn_Bank.close();

            
            
            //System.out.println("Database Connection Successfull");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
       
}
*/   
    
    
}
