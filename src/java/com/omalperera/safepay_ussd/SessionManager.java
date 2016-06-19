/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omalperera.safepay_ussd;


import java.util.concurrent.ConcurrentHashMap;
import com.omalperera.bank.TheBank_DatabaseConnector;

/**
 *
 * @author Omal Perera
 */
public class SessionManager {
    
    private static final ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<>();
    
    private static final String welcomeScreenRegisterd = "Welcome to\nSafePay\n\n1. Activate my\n   Card\n2. Exit";
    private static final String notRegisterd = "Please get register to SafePay first.\nYou will recieve a message shortly with instructions, how to register.\n\n1.Back";
    private static final String msgHowToRegister = "Please ask your Bank for SafePay Service.";
    private static final String cardActivatingFeedBackUSSD = "Your request is being processing.\nYou will get a message shortly";
    private static final String successfully_Activated_SMS = "Your Card has been succesfuly activated for 5 minutes";
    private static final String unable_to_Activat_SMS = "Dear Customer,\nBank was unable to activate your card";
    
    private static boolean registerState = false; // true- registerd  and false- non registerd 
    private static boolean activatedState = false; //true- card has been activated by the bank and false- opposite of...
    
    
    
    
    
    
    
    
    
    public static String getNextUssdScreen(String mobileNum, String input, String operation){
        
        if ("mo-init".equals(operation)) {
            sessions.put(mobileNum, "weLcomE");
            
            try {
                registerState = TelcoCompany_DatabaseConnector.Telco_DB(mobileNum); //here mobile number will be in a formate of tel:0123654789
                                
                    if(registerState==true) { //already registerd customers
                        sessions.put(mobileNum, "registerd");
                        return welcomeScreenRegisterd;
                                                
                    } else {
                        
                        sessions.put(mobileNum, "non_registerd");
                        FeedbackSenderAsMsg.sendResponse(mobileNum, msgHowToRegister); // send the msg how to register
                        return notRegisterd;
						
                    }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            
            
            
            
        }else if("mo-cont".equals(operation)){
           
            if ("registerd".equals(sessions.get(mobileNum))) {    //msg after a registerd user requst to activate hs card
                if("1".equals(input)){
                
                    activatedState = TheBank_DatabaseConnector.Changing_Active_Mode(mobileNum);
                
                    if(activatedState == true){
                        FeedbackSenderAsMsg.sendResponse(mobileNum, successfully_Activated_SMS);
                    }else{
                        FeedbackSenderAsMsg.sendResponse(mobileNum, unable_to_Activat_SMS);
                    }
                
                    return cardActivatingFeedBackUSSD;  
                }
            
                
            }else if ("non_registerd".equals(sessions.get(mobileNum))) {    // A non registerd user exit from the USSD
                if("1".equals(input)){

                    return "0";  
                }
            }
        
        }
        return "Error occoured while processing the request";
        
    }
    
    
}
