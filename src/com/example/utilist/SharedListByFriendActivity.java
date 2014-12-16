package com.example.utilist;

import java.util.ArrayList;
import java.util.List;

import com.example.utilist.R;
import com.example.adapter.SharedListItemAdapter;
import com.example.model.SharedList;
import com.example.model.SharedListParser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SharedListByFriendActivity extends Activity {
	private ListView listView;
	ArrayList<SharedList> sharedLists;
	SharedListItemAdapter adapter;
	static final String SHARELIST_KEY = "ShareKey";
	static final String SHARELIST_INDEX_KEY = "ShareIndex";
	static final int SAHRELIST_VIEW_REC_CODE = 1010;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared_list_by_friend);
		listView = (ListView) findViewById(R.id.sharedlist_listView);
		sharedLists = new ArrayList<SharedList>();
		ParseQuery<SharedListParser> parseQuery = new ParseQuery<SharedListParser>(SharedListParser.class);
		parseQuery.whereEqualTo("sharedToObjectId", ParseUser.getCurrentUser().getObjectId());
		parseQuery.orderByDescending("createdAt");
		parseQuery.findInBackground(new FindCallback<SharedListParser>() {
			public void done(List<SharedListParser> objects, ParseException e) {
				if(e == null){
					for(SharedListParser s: objects){
						sharedLists.add(new SharedList(s));
					}
					showList();
				}
				
			}
		});
		
	}
	
	public void showList(){
		adapter = new SharedListItemAdapter(this, R.layout.sharedlist_display_item, sharedLists);
		listView.setAdapter(adapter);
		adapter.setNotifyOnChange(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SharedListByFriendActivity.this, ViewSharedListActivity.class);
				intent.putExtra(SHARELIST_KEY, sharedLists);
				intent.putExtra(SHARELIST_INDEX_KEY, position);
				startActivityForResult(intent, SAHRELIST_VIEW_REC_CODE);
				
			}
			
		});
		if(sharedLists.size() == 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(SharedListByFriendActivity.this);
			builder.setMessage("The Shared List is Empty!");
			builder.create().show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == SAHRELIST_VIEW_REC_CODE){
			if(resultCode == Activity.RESULT_OK){
				sharedLists = (ArrayList<SharedList>) data.getSerializableExtra(SHARELIST_KEY);
				adapter = new SharedListItemAdapter(SharedListByFriendActivity.this, R.layout.sharedlist_display_item, sharedLists);
				listView.setAdapter(adapter);
			}
		}
		//super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
