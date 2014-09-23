package com.girnar.explorejaipur.jsondata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class JsonParser {

	public static HttpGet httpGet;
	public static HttpPost httppost;
	/**
	 * This method is used to get JSON feeds for provided URL and return it as a string value.
	 * @param url from where we need to get JSON feeds.
	 * @return JSON data as a string.
	 * @throws PriceDekhoException if there is any problem happened with this process
	 */
	public String getJSONFromUrl(String url) throws MyException {

		 
		InputStream is = null;
		String data = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			 httpGet = new HttpGet(url);
			httpGet.addHeader("X_REST_USERNAME", "admin@restuser");
			httpGet.addHeader("X_REST_PASSWORD", "admin@Access");
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			
			// when there is no data for particular query from server
			if (statusCode == 204) 
				{
					data = null;
				}

			// 200 is OK status code from server
			if (statusCode == 200) 
			{	
				is = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
				StringBuilder sb = new StringBuilder();
				String line="";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				data=sb.toString();
				is.close();
			}
			
			
		} catch (IllegalStateException e) {
			throw new MyException("Exception due to Illegal State Operation. Message is: " + e.getMessage());
		} catch (ClientProtocolException e) {
			throw new MyException(e.getMessage());
		} catch (IOException e) {
			throw new MyException(e.getMessage());
		} 
		catch(IllegalArgumentException e){
			throw new MyException(e.getMessage());
		}
		
		
		return data;
	}
	
	
}
