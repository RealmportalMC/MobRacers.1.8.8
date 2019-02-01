package me.winterguardian.core.util;

import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WebCommunicationUtil
{
	private WebCommunicationUtil(){}
	
	public static String get(String url) throws IOException
	{
		String source = "";
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						new URL(url).openConnection().getInputStream()));
		String inputLine;
		 
		while ((inputLine = in.readLine()) != null)
			source += inputLine;
		in.close();
		return source;
	}
	
	public static String post(String address, Map<String, String> data, HashMap<String, String> properties) throws Exception
	{
		String result = "";
		OutputStreamWriter writer = null;
		BufferedReader reader = null;
		try
		{
			String serializedData = "";
			for(int i = 0; i < data.keySet().size(); i++)
			{
				if (i != 0) 
					serializedData += "&";
				serializedData += URLEncoder.encode((String) data.keySet().toArray()[i], "UTF-8") + "=" + URLEncoder.encode(data.get(data.keySet().toArray()[i]), "UTF-8");
			}
			
			
			URLConnection conn = new URL(address).openConnection();
			conn.setDoOutput(true);
			if(properties != null)
				for(Map.Entry<String, String> entry : properties.entrySet())
					conn.setRequestProperty(entry.getKey(), entry.getValue());

			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(serializedData);
			writer.flush();

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String ligne;
			while ((ligne = reader.readLine()) != null)
			{
				result += ligne;
			}
			writer.close();
			reader.close();
		}
		catch(Exception e)
		{
			try
			{
				writer.close();
			}
			catch(Exception e2) {}
			
			try
			{
				reader.close();
			}
			catch(Exception e3) {}
			
			throw e;
		}

		return result;
	}

	public static String post(String address, Map<String, String> data) throws Exception
	{
		return post(address, data, null);
	}

	public static void sendPostData(String adress, Map<String, String> data) throws Exception
	{
		OutputStreamWriter writer = null;
		try
		{
			String serializedData = "";
			for(int i = 0; i < data.keySet().size(); i++)
			{
				if (i != 0) 
					serializedData += "&";
				serializedData += URLEncoder.encode((String) data.keySet().toArray()[i], "UTF-8") + "=" + URLEncoder.encode(data.get(data.keySet().toArray()[i]), "UTF-8");
			}
			
			
			URLConnection conn = new URL(adress).openConnection();
			conn.setDoOutput(true);

			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(serializedData);
			writer.flush();
			
			writer.close();
		}
		catch(Exception e)
		{
			try
			{
				writer.close();
			}
			catch(Exception e2) {}
			
			throw e;
		}
	}
}
