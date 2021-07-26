package com.fullsail.android.busted.net;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.fullsail.android.busted.R;

public class GetDetailsTask extends AsyncTask<Integer, Void, HashMap<String, String>> {
	
	private static final String API_URL = "https://www.govtrack.us/api/v2/person/";
	
	private static final String NAME = "name";
	private static final String BIRTHDAY = "birthday";
	private static final String GENDER = "gender";
	private static final String TWITTER_ID = "twitter_id";
	private static final String YOUTUBE_ID = "youtubeid";
	private static final String CSPAN_ID = "cspanid";

	final private OnDetailFinished mOnFinishedInterface;

	public GetDetailsTask(OnDetailFinished onFinished) {
		mOnFinishedInterface = onFinished;
	}

	public interface OnDetailFinished{
		void onDetailPre();
		void onDetailPost( HashMap<String, String> _retValues);
	}

	
	@Override
	protected HashMap<String, String> doInBackground(Integer... _params) {
		
		// Add member ID to the end of the URL
		String data = NetworkUtils.getNetworkData(API_URL + _params[0]);
		HashMap<String, String> retValues = new HashMap<>();
		
		try {
			
			JSONObject response = new JSONObject(data);
			
			String birthday = response.getString("birthday");
			retValues.put(BIRTHDAY, birthday);
			
			String youtubeID = response.getString("youtubeid");
			retValues.put(YOUTUBE_ID, "" + youtubeID);
			
			String name = response.getString("name");
			retValues.put(NAME, name);
			
			String gender = response.getString("gender_label");
			retValues.put(GENDER, gender);

			String cspanID = response.getString("cspanid");
			retValues.put(CSPAN_ID, "" + cspanID);
			
			String twitterId = response.getString("twitterid");
			retValues.put(TWITTER_ID, twitterId);
			
			
		} catch(JSONException e) {
			e.printStackTrace();
		}


		
		return retValues;
	}

	@Override
	protected void onPreExecute() {
		mOnFinishedInterface.onDetailPre();
	}


	@Override
	protected void onPostExecute(HashMap<String, String> _result) {
		mOnFinishedInterface.onDetailPost(_result);
	}
}