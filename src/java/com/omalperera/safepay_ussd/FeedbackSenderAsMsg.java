/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omalperera.safepay_ussd;

import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.sms.MoSmsListener;
import hms.kite.samples.api.sms.SmsRequestSender;
import hms.kite.samples.api.sms.messages.MoSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsResp;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 *
 * @author Omal Perera
 */
public class FeedbackSenderAsMsg implements MoSmsListener{

	@Override
	public void init() {
            

	}

	@Override
	public void onReceivedSms(MoSmsReq moSmsReq) {


	}

	public static void sendResponse(String mobileno, String msgContent) {
            //MoSmsReq moSmsReq,
		try {
			MtSmsReq mtSmsReq = new MtSmsReq();
			mtSmsReq.setDestinationAddresses(Arrays.asList(mobileno));
			mtSmsReq.setMessage(msgContent);
			mtSmsReq.setApplicationId("App_0001");
			mtSmsReq.setPassword("password");

			SmsRequestSender requestSender = new SmsRequestSender(new URL("http://localhost:7000/sms/send"));
			MtSmsResp smsResp = requestSender.sendSmsRequest(mtSmsReq);

			System.out.println("Response " + smsResp);

		} catch (SdpException e) {
			System.err.println("Error " + e);
		}

		catch (MalformedURLException e) {
			System.err.println("Error " + e);
		}

	}

    
    
}




