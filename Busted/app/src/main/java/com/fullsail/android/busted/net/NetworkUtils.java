package com.fullsail.android.busted.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
	public static boolean isConnected(Context _context) {
		
		ConnectivityManager mgr = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if(mgr != null) {
			NetworkInfo info = mgr.getActiveNetworkInfo();

			if(info != null) {
				return info.isConnected();
			}
		}
		return false;
	}
	
	public static String getNetworkData(String _url) {

		if(_url == null){
			return null;
		}

		HttpURLConnection connection = null;
		String data = null;
		URL url;

		try {
			url = new URL(_url);

			connection = (HttpURLConnection)url.openConnection();

			connection.connect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try{
			if(connection != null){
				is = connection.getInputStream();
				data = IOUtils.toString(is,"UTF-8");
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
			if(connection != null){
				if(is != null){
					try{
						is.close();
					}catch(Exception e){
						e.printStackTrace();
					}
					connection.disconnect();

				}
			}
		}
		
		return data;
	}
}