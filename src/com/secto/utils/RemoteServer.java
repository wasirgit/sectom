package com.secto.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class RemoteServer {

	public synchronized static  String sendHTTPPostRequestToServer(String urlPath,
			boolean readfromServer) throws Exception {
		String data = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost( urlPath);
	
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		BufferedReader reader = null;
		response = httpclient.execute(httppost);
		if (readfromServer) {
			entity = response.getEntity();
			is = entity.getContent();
			reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			data = sb.toString();
		}
		return data.trim();
	}
	
	 public static String sendHTTPPostRequestToServer(String urlPath, List<NameValuePair> postData, boolean readfromServer) throws Exception {
	        String data = "";
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost(urlPath);
	        HttpResponse response = null;
	        HttpEntity entity = null;
	        InputStream is = null;
	        BufferedReader reader = null;
	        httppost.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
	        response = httpclient.execute(httppost);
	        if (readfromServer) {
	            entity = response.getEntity();
	            is = entity.getContent();
	            reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	            is.close();
	            data = sb.toString();
	        }
	        return data.trim();
	    }
}