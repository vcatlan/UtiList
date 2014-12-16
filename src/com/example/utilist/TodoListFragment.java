package com.example.utilist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.utilist.R;
import com.example.adapter.TodoListDisplayItemAdapter;
import com.example.adapter.TodoListDisplayItemAdapter.ICreateCallToTodoListItemClick;
import com.example.model.History;
import com.example.model.HistoryParser;
import com.example.model.TodoList;
import com.example.model.TodoListItem;
import com.example.model.TodoListParser;

public class TodoListFragment extends Fragment implements OnItemClickListener, ICreateCallToTodoListItemClick{
	public static final int EDIT_REQUESTCODE = 1006;
	public static final int SPEECH_REQUESTCODE = 1007;
	public static final int SCAN_REQUESTCODE = 1008;
	public static final int HSITORY_ITEM_TYPE = 2;
	public static final String ITEM_NAME_KEY = "item_name_key";
	public static final String ITEM_INDEX_KEY = "item_index_key";
	public static final String ITEM_MODE_KEY = "item_mode_key";
	public static final String ITEM_LIST_IDEX_KEY = "item_list_index_key";
	public static final String ITEM_HISTORY_KEY = "item_history_key";
	public static final String Speech_KEY = "speech_key";
	
	private ICreateCallToActivity mListener;
	
	private ImageView left, middle, right;
	private EditText additem;
	private ArrayList<TodoListItem> itemList;
	private ListView itemsListView;
	private TodoListDisplayItemAdapter adapter;
	private TodoList list;
	private int listIndex;
	private String mode; 
	private History history;
	public TodoListFragment() {
		
	}
	public TodoListFragment(TodoList list, History history ,int listIndex) {
		this.list = list;
		this.listIndex = listIndex;
		this.history = history;
		itemList = list.getItems();
		if(itemList == null)
			itemList = new ArrayList<TodoListItem>();
		sortItemList();
		list.setOutstandingItem();
		
	}
	
	public void setData(TodoList list, int listIndex, History history){
		this.list = list;
		this.history = history;
		this.listIndex = listIndex;
		itemList = list.getItems();
		if(itemList == null)
			itemList = new ArrayList<TodoListItem>();
		sortItemList();
		list.setOutstandingItem();
		adapter = new TodoListDisplayItemAdapter(getActivity(), R.layout.activity_todo_list_display_item_layout, itemList, TodoListFragment.this);
		itemsListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		itemsListView.setAdapter(adapter);
		
	}
	
	public void setList(TodoList list, int listIndex){
		this.list = list;
		this.listIndex = listIndex;
		itemList = list.getItems();
		if(itemList == null)
			itemList = new ArrayList<TodoListItem>();
		sortItemList();
		list.setOutstandingItem();
		adapter = new TodoListDisplayItemAdapter(getActivity(), R.layout.activity_todo_list_display_item_layout, itemList, TodoListFragment.this);
		itemsListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		saveList();
	}
	
	public void setTextViewVoice(String text){
		additem.setText(text);
	}
	
	public interface ICreateCallToActivity{
		public void fireIntentForResult(int requestCode, TodoList name, int index, String mode, int listIndex, History history);
		public void fireIntentForSpeech(int requestCode);
		public void showItemHistory(int itemType, History history);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		itemsListView = (ListView) getActivity().findViewById(R.id.todo_list_item_listView);
		left = (ImageView) getActivity().findViewById(R.id.todo_imageViewLeft);
		middle = (ImageView) getActivity().findViewById(R.id.todo_imageViewMiddle);
		right = (ImageView) getActivity().findViewById(R.id.todo_imageViewRight);
		additem = (EditText) getActivity().findViewById(R.id.todo_editText_add_item);
		adapter = new TodoListDisplayItemAdapter(getActivity(), R.layout.activity_todo_list_display_item_layout, itemList, TodoListFragment.this);
		itemsListView.setAdapter(adapter);
		itemsListView.setOnItemClickListener(this);
		adapter.setNotifyOnChange(true);
		additem.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (!additem.getText().toString().isEmpty()) {
					left.setImageResource(R.drawable.ic_action_ok);
					middle.setImageResource(R.drawable.ic_action_cancel);
					right.setImageResource(R.drawable.ic_action_settings);
					left.setVisibility(View.VISIBLE);
					middle.setVisibility(View.VISIBLE);

					left.setTag("R.drawable.ic_action_ok");
					middle.setTag("R.drawable.ic_action_cancel");
					right.setTag("R.drawable.ic_action_settings");
				} else {
					left.setVisibility(View.GONE);
					
					middle.setImageResource(R.drawable.ic_clock);
					right.setImageResource(R.drawable.ic_mic);
					middle.setTag("R.drawable.ic_clock");
					right.setTag("R.drawable.ic_mic");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (left.getTag().toString()
						.compareTo("R.drawable.ic_action_ok") == 0) {
					itemList.add(new TodoListItem(additem.getText().toString()));
					sortItemList();
					list.setOutstandingItem();
					history.getItemTodoHistoryList().add(additem.getText().toString());
					//adapter = new DisplayItemAdapter(getActivity(), R.layout.display_item_layout, itemList);
					//itemsListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					saveList();					
					additem.setText("");		
				}
			}
		});

		middle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (middle.getTag().toString()
						.compareTo("R.drawable.ic_action_cancel") == 0) {
					additem.setText("");
				}
				else if(middle.getTag().toString()
						.compareTo("R.drawable.ic_clock") == 0){
					mListener.showItemHistory(HSITORY_ITEM_TYPE, history);
				}
			}
		});

		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (right.getTag().toString()
						.compareTo("R.drawable.ic_action_settings") == 0) {
					itemList.add(new TodoListItem(additem.getText().toString()));
					sortItemList();
					list.setOutstandingItem();
					mode = "adding";
					mListener.fireIntentForResult(EDIT_REQUESTCODE, list, itemList.size()-1, mode, listIndex, history);
					additem.setText("");
					
				} else if (right.getTag().toString()
						.compareTo("R.drawable.ic_mic") == 0) {
					mListener.fireIntentForSpeech(SPEECH_REQUESTCODE);
				}
			}
		});
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_todo_list_main,
				container, false);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mListener = (ICreateCallToActivity) activity;
			
		} catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ICreateCallToActivity");
        }		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mode = "editing";
		mListener.fireIntentForResult(EDIT_REQUESTCODE, list, position, mode, listIndex, history);
		
	}
	
	public void saveList(){
		TodoListParser listParser = new TodoListParser(list);
		HistoryParser historyParser = new HistoryParser(history);
		listParser.saveInBackground();
		historyParser.saveInBackground();
	}
	
	public void sortItemList(){
		Collections.sort(itemList, new Comparator<TodoListItem>() {

			@Override
			public int compare(TodoListItem lhs, TodoListItem rhs) {
				if( lhs.isChecked() && ! rhs.isChecked() ) {
				      return +1;
				   }
				   if( ! lhs.isChecked() && rhs.isChecked() ) {
				      return -1;
				   }
				   return 0;
			}
		});
	}
	public void selectItemHistory(String itemName) {
		additem.setText(itemName);
		
	}
	@Override
	public void todoItemCheckBoxOnClick(View v) {
		itemList.get(Integer.parseInt(v.getTag().toString())).setChecked();
		sortItemList();
		list.setOutstandingItem();
		saveList();
		adapter.notifyDataSetChanged();	
	}	
}
