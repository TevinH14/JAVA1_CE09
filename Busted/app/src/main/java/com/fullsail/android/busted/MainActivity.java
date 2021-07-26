package com.fullsail.android.busted;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.fullsail.android.busted.net.GetDetailsTask;
import com.fullsail.android.busted.net.GetDetailsTask.OnDetailFinished;
import com.fullsail.android.busted.net.GetMembersTask;
import com.fullsail.android.busted.net.NetworkUtils;
import com.fullsail.android.busted.object.Member;

public class MainActivity extends Activity implements GetMembersTask.OnMemberFinished, OnDetailFinished{
	
	private View mMembersListScreen;
	private View mMemberDetailsScreen;
	private ListView mListView;
	private ProgressDialog pDialog;
	private ArrayList<Member> membersArrayList = null;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMembersListScreen = findViewById(R.id.members_list_screen);
		mMemberDetailsScreen = findViewById(R.id.member_details_screen);
		mListView = findViewById(R.id.members_list);

		//mListView.

		if(NetworkUtils.isConnected(this)) {
			GetMembersTask task = new GetMembersTask(this);
			task.execute();
		}else{
			noInternetConnection();
		}
	}

	
	private void showMemberDetailsScreen(int _id) {
		mMembersListScreen.setVisibility(View.GONE);
		mMemberDetailsScreen.setVisibility(View.VISIBLE);

		GetDetailsTask task = new GetDetailsTask(this);
		task.execute(_id);
	}
	

	private void populateMemberDetailsScreen(String _name, String _birthday, String _gender,String _twitterId, String _youtubeID, String _cspanID) {
		
		TextView tv = findViewById(R.id.text_name);
		tv.setText(_name);

		tv = findViewById(R.id.text_birthday);
		tv.setText(_birthday);

		tv = findViewById(R.id.text_gender);
		tv.setText(_gender);

		tv = findViewById(R.id.text_twitter_id);
		tv.setText(_twitterId);

		tv = findViewById(R.id.text_youtube_id);
		tv.setText(_youtubeID);

		tv = findViewById(R.id.text_cspan_id);
		tv.setText(_cspanID);
	}
	
	private final OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> _parent, View _view, int _position, long _id) {
			if (NetworkUtils.isConnected(MainActivity.this)) {
				showMemberDetailsScreen(membersArrayList.get(_position).getId());
			} else {
				noInternetConnection();
			}
		}
	};
	
	public void onBackPressed() {
		if(mMemberDetailsScreen.getVisibility() == View.VISIBLE) {
			mMemberDetailsScreen.setVisibility(View.GONE);
			mMembersListScreen.setVisibility(View.VISIBLE);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onPre() {
		loadingDialog();
	}

	@Override
	public void onDetailPre() {
		loadingDialog();
	}

	@Override
	public void onDetailPost(HashMap<String, String> _retValues) {
		// Update the UI
		String name =  _retValues.get("name");
		String birthday = _retValues.get("birthday");
		String gender = _retValues.get("gender");
		String twitterId = _retValues.get("twitter_id");
		String youtubeID = _retValues.get("youtubeid");
		String cspanID = _retValues.get("cspanid");
		populateMemberDetailsScreen(name, birthday, gender, twitterId, youtubeID, cspanID);
		pDialog.cancel();
		pDialog = null;
	}

	@Override
	public void onMemberPost(ArrayList list) {
		if (list != null) {
			membersArrayList = list;
			MembersAdapter membersAdapter = new MembersAdapter(this, list);
			mListView.setOnItemClickListener(mItemClickListener);
			mListView.setAdapter(membersAdapter);
			pDialog.cancel();
			pDialog = null;
		}
	}

	private void noInternetConnection(){
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.not_connected);
			builder.setMessage(R.string.reconnect);
			builder.setIcon(R.drawable.info_icon);
			builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					dialog.cancel();
				}
			});
			builder.show();
	}

	private void loadingDialog(){
		pDialog = ProgressDialog.show(this,getString(R.string.wait),getString(R.string.download_data), true);

	}



}
