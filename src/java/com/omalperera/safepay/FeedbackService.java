/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omalperera.safepay;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Omal Perera
 */
public class FeedbackService {
    
    public static final Map<String, String> subscriberFeedback = new HashMap<String, String>();

	public static void addFeedback(String subscriberId, String message) {
		subscriberFeedback.put(subscriberId, message);
	}

	public static Map<String, String> getFeedback() {

		return subscriberFeedback;
	}

    
    
}
