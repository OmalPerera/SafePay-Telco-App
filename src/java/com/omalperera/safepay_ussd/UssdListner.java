/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omalperera.safepay_ussd;


import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.ussd.MoUssdListener;
import hms.kite.samples.api.ussd.OperationType;
import hms.kite.samples.api.ussd.UssdRequestSender;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdReq;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Omal Perera
 */
public class UssdListner implements MoUssdListener {

    @Override
    public void init() {
    }

    @Override
    public void onReceivedUssd(MoUssdReq moUssdRequest) {
        
        MtUssdReq mtUssdRequest = new MtUssdReq();
        mtUssdRequest.setApplicationId("APP_000001");
        mtUssdRequest.setPassword("password");
        mtUssdRequest.setDestinationAddress(moUssdRequest.getSourceAddress());
        mtUssdRequest.setMessage(SessionManager.getNextUssdScreen(moUssdRequest.getSourceAddress(), moUssdRequest.getMessage(),moUssdRequest.getUssdOperation()));
        mtUssdRequest.setSessionId(moUssdRequest.getSessionId());
        mtUssdRequest.setUssdOperation(OperationType.MT_CONT.getName());
        
        
        
        try {
            UssdRequestSender ussdReqSend = new UssdRequestSender(new URL("http://127.0.0.1:7000/ussd/send"));
            ussdReqSend.sendUssdRequest(mtUssdRequest);
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            
        } catch (SdpException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
