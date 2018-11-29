package com.marsai.facecheck;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

public class AITest {
	public static void main(String[] args) throws Exception {;
		String access_token_url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials" +
				"&client_id="+APIConstants.API_KEY
			   +"&client_secret="+ APIConstants.SERCET_KEY;
		HttpResponse response = HttpUtils.doPostBD(access_token_url,new HashMap<String, String>(),new HashMap<String, String>());
		System.out.println(EntityUtils.toString(response.getEntity()));
		
	}
}
