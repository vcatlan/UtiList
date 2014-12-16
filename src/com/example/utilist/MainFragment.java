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
import android.widget.TextView;
import android.widget.Toast;

import com.example.utilist.R;
import com.example.adapter.ShoppingListDisplayItemAdapter;
import com.example.adapter.ShoppingListDisplayItemAdapter.ICreateCallToShoppingListItemClick;
import com.example.model.History;
import com.example.model.HistoryParser;
import com.example.model.ShoppingList;
import com.example.model.ShoppingListItem;
import com.example.model.ShoppingListParser;


public class MainFragment extends Fragment implements OnItemClickListener, ICreateCallToShoppingListItemClick{
	static final int EDIT_REQUESTCODE = 1001;
	static final int SPEECH_REQUESTCODE = 1003;
	static final int SCAN_REQUESTCODE = 1004;
	static final String ITEM_NAME_KEY = "item_name_key";
	static final String ITEM_INDEX_KEY = "item_index_key";
	static final String ITEM_LIST_INDEX_KEY = "item_list_index_key";
	static final String ITEM_MODE_KEY = "item_mode_key";
	static final String HISTORY_KEY = "history_key";
	static final String Speech_KEY = "speech_key";
	
	private ICreateCallToActivity mListener;
	
	private ImageView left, middle, right;
	private EditText additem;
	private ArrayList<ShoppingListItem> itemList;
	private ListView itemsListView;
	private ShoppingListDisplayItemAdapter adapter;
	private ShoppingList list;
	private int listIndex;
	private TextView total;
	private TextView remaining;
	private String mode; 
	private History history;
	private static final int HISTORY_ITEM_TYPE = 1; 
	public MainFragment() {
		
	}
	public MainFragment(ShoppingList list, History history, int listIndex) {
		this.list = list;
		this.listIndex = listIndex;
		this.history = history;
		itemList = list.getItems();
		sortItemList();
	}
	
	public void setData(ShoppingList list, History history, int listIndex){
		this.list = list;
		this.listIndex = listIndex;
		this.history = history;
		itemList = list.getItems();
		sortItemList();
		adapter = new ShoppingListDisplayItemAdapter(getActivity(), R.layout.display_item_layout, itemList, MainFragment.this);
		itemsListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		showTotalRrmaining();
		//itemsListView.setAdapter(adapter);
		
	}
	
	public void setList(ShoppingList list, int listIndex){
		this.list = list;
		this.listIndex = listIndex;
		itemList = list.getItems();
		sortItemList();
		adapter = new ShoppingListDisplayItemAdapter(getActivity(), R.layout.display_item_layout, itemList, MainFragment.this);
		itemsListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		showTotalRrmaining();
		saveList();
	}
	
	public void setTextViewVoice(String text){
		additem.setText(text);
	}
	
	public interface ICreateCallToActivity{
		public void fireIntentForResult(int requestCode, ShoppingList name, int index, String mode, History history, int listIndex);
		public void fireIntentForSpeech(int requestCode);
		public void fireIntentForScan(int requestCode);
		public void showItemHistory(int itemType, History history);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		itemsListView = (ListView) getActivity().findViewById(R.id.list_item_listView);
		left = (ImageView) getActivity().findViewById(R.id.imageViewLeft);
		middle = (ImageView) getActivity().findViewById(R.id.imageViewMiddle);
		right = (ImageView) getActivity().findViewById(R.id.imageViewRight);
		additem = (EditText) getActivity().findViewById(R.id.editText_add_item);
		total = (TextView) getActivity().findViewById(R.id.textViewDisplayTotal);
		remaining = (TextView) getActivity().findViewById(R.id.textViewDisplayRemaining);
		adapter = new ShoppingListDisplayItemAdapter(getActivity(),R.layout.display_item_layout, itemList, MainFragment.this);
		itemsListView.setAdapter(adapter);
		itemsListView.setOnItemClickListener(this);
		showTotalRrmaining();
		adapter.setNotifyOnChange(true);
		additem.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (!additem.getText().toString().isEmpty()) {
					left.setImageResource(R.drawable.ic_action_ok);
					middle.setImageResource(R.drawable.ic_action_cancel);
					right.setImageResource(R.drawable.ic_action_settings);

					left.setTag("R.drawable.ic_action_ok");
					middle.setTag("R.drawable.ic_action_cancel");
					right.setTag("R.drawable.ic_action_settings");
				} else {
					left.setImageResource(R.drawable.ic_clock);
					middle.setImageResource(R.drawable.ic_barcode);
					right.setImageResource(R.drawable.ic_mic);

					left.setTag("R.drawable.ic_clock");
					middle.setTag("R.drawable.ic_barcode");
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
					itemList.add(new ShoppingListItem(additem.getText().toString()));
					sortItemList();
					//adapter = new DisplayItemAdapter(getActivity(), R.layout.display_item_layout, itemList);
					//itemsListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					list.setTRO();
					history.getItemNameHistoryList().add(additem.getText().toString());
					saveList();					
					additem.setText("");
					
				} else if (left.getTag().toString()
						.compareTo("R.drawable.ic_clock") == 0) {
					mListener.showItemHistory(HISTORY_ITEM_TYPE, history);
					
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
				} else if (middle.getTag().toString()
						.compareTo("R.drawable.ic_barcode") == 0) {

					mListener.fireIntentForScan(SCAN_REQUESTCODE);
				}

			}
		});

		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (right.getTag().toString()
						.compareTo("R.drawable.ic_action_settings") == 0) {
					itemList.add(new ShoppingListItem(additem.getText().toString()));
					sortItemList();
					list.setTRO();
					mode = "adding";
					mListener.fireIntentForResult(EDIT_REQUESTCODE, list, itemList.size()-1, mode, history, listIndex);
					additem.setText("");
					
				} else if (right.getTag().toString()
						.compareTo("R.drawable.ic_mic") == 0) {
					mListener.fireIntentForSpeech(SPEECH_REQUESTCODE);
				}
			}
		});
		
	}
	
	public void showTotalRrmaining(){
		if(list.getTotal() > 0){
			total.setText("Total: $" + String.format("%.2f", list.getTotal()));
			remaining.setText("Remaining: $" + String.format("%.2f", list.getRemaining()));
			getActivity().findViewById(R.id.linearLayout_price).setVisibility(View.VISIBLE);
			
		}
		else{
			getActivity().findViewById(R.id.linearLayout_price).setVisibility(View.GONE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shopping_list_main,
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
		mListener.fireIntentForResult(EDIT_REQUESTCODE, list, position, mode, history, listIndex);
		
	}
	
	public void saveList(){
		ShoppingListParser listParser = new ShoppingListParser(list);
		HistoryParser historyParser = new HistoryParser(history);
		listParser.saveInBackground();
		historyParser.saveInBackground();
	}
	
	public void sortItemList(){
		Collections.sort(itemList, new Comparator<ShoppingListItem>() {

			@Override
			public int compare(ShoppingListItem lhs, ShoppingListItem rhs) {
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
	
	public void setItemToList(ShoppingListItem item){
		if( item != null){
			if(item.getName() != null){
				itemList.add(item);
				sortItemList();
				//adapter = new DisplayItemAdapter(getActivity(), R.layout.display_item_layout, itemList);
				//itemsListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				list.setTRO();
				history.getItemNameHistoryList().add(item.getName());
				saveList();
				showTotalRrmaining();
				additem.setText("");
			}
			else {
				Toast.makeText(getActivity(), "Could not find that item", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void selectItemHistory(String itemName){
		itemList.add(new ShoppingListItem(itemName));
		sortItemList();
		adapter = new ShoppingListDisplayItemAdapter(getActivity(), R.layout.display_item_layout, itemList, MainFragment.this);
		itemsListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		list.setTRO();
		saveList();					
		additem.setText("");
	}
	@Override
	public void itemCheckBoxOnClick(View v) {
		itemList.get(Integer.parseInt(v.getTag().toString())).setChecked();
		sortItemList();
		list.setTRO();
		saveList();
//		adapter.clear();
//		adapter.addAll(itemList);
		adapter.notifyDataSetChanged();
		showTotalRrmaining();	
	}
	
	
}
