package com.fullsail.android.busted.net;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.fullsail.android.busted.object.Member;

public class GetMembersTask extends AsyncTask<Void, Void, ArrayList<Member>> {
	
	private static final String API_URL = "https://www.govtrack.us/api/v2/role?current=true";

	final private OnMemberFinished mOnFinishedInterface;

	public GetMembersTask(OnMemberFinished onFinished) {
		mOnFinishedInterface = onFinished;
	}

	public interface OnMemberFinished{
		void onPre();
		void onMemberPost(ArrayList list);
	}
	
	@Override
	protected ArrayList<Member> doInBackground(Void... _params) {
		
		String data = NetworkUtils.getNetworkData(API_URL);
		
		try {
			
			JSONObject response = new JSONObject(data);
			
			JSONArray membersJson = response.getJSONArray("objects");
			
			ArrayList<Member> members = new ArrayList<>();
			
			for(int i = 0; i < membersJson.length(); i++) {
				JSONObject obj = membersJson.getJSONObject(i);
				JSONObject person = obj.getJSONObject("person");

				int id = getIdFromLink(person.getString("link"));
				String name = person.getString("name");
				String party = obj.getString("party");
				
				members.add(new Member(id, name, party));
			}
			return members;
			
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}


	@Override
	protected void onPreExecute() {
		mOnFinishedInterface.onPre();
	}

	@Override
	protected void onPostExecute(ArrayList<Member> _result) {
		mOnFinishedInterface.onMemberPost(_result);
	}

	private int getIdFromLink(String _link) {
		int index = _link.lastIndexOf('/');
		if(index > -1 && (index+1) < _link.length()) {
			try {
				return Integer.parseInt(_link.substring(index+1));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		Log.e("ERROR", "Unable to find ID in string \"" + _link + "\".");
		return 0;
	}
}