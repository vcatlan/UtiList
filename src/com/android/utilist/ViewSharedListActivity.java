package com.android.utilist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import edu.unccUtilistProject.R;
import com.android.adapter.ShoppingListDisplayItemAdapter;
import com.android.adapter.TodoListDisplayItemAdapter;
import com.android.model.SharedList;
import com.android.model.SharedListParser;
import com.android.model.ShoppingList;
import com.android.model.ShoppingListParser;
import com.android.model.TodoList;
import com.android.model.TodoListParser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class ViewSharedListActivity extends Activity {
	int index;
	ListView listView;
	ArrayList<SharedList> sharedLists;
	TodoList todoList;
	ShoppingList shoppingList;
	LinearLayout linearLayoutPrice;
	TextView textViewTotal;
	TextView textViewRemaining;
	TextView textViewListTitle;
	TextView textViewNumberOfItems;
	TextView textViewOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_shared_list);
		linearLayoutPrice = (LinearLayout) findViewById(R.id.view_sharedList_linearLayout_Price);
		textViewRemaining = (TextView) findViewById(R.id.view_shared_list_listView_textView_remaining);
		textViewTotal = (TextView) findViewById(R.id.view_shared_list_listView_textView_total);
		textViewListTitle = (TextView) findViewById(R.id.view_shared_list_textView_title);
		textViewNumberOfItems = (TextView) findViewById(R.id.view_shared_list_textView_number_item);
		// textViewOut = (TextView)
		// findViewById(R.id.view_shared_list_textView_out);
		listView = (ListView) findViewById(R.id.view_shared_list_listView_listView);
		index = getIntent().getIntExtra(
				SharedListByFriendActivity.SHARELIST_INDEX_KEY, 0);
		sharedLists = (ArrayList<SharedList>) getIntent().getSerializableExtra(
				SharedListByFriendActivity.SHARELIST_KEY);
		if (sharedLists.get(index).getListType().equals("todoList")) {
			showTodoList();
		} else {
			showShoppingList();
		}
		textViewListTitle.setText("List title: "
				+ sharedLists.get(index).getListTitle());
		sharedLists.get(index).setNewList(false);
		SharedListParser sharedListParser = new SharedListParser(
				sharedLists.get(index));
		sharedListParser.saveInBackground();

	}

	public void showTodoList() {
		ParseQuery<TodoListParser> parseQuery = new ParseQuery<TodoListParser>(
				TodoListParser.class);
		parseQuery.whereEqualTo("objectId", sharedLists.get(index)
				.getListObjectId());
		parseQuery.findInBackground(new FindCallback<TodoListParser>() {
			@Override
			public void done(List<TodoListParser> objects, ParseException e) {
				if (e == null) {
					todoList = new TodoList(objects.get(0));
					showTodoListItem();
				}

			}
		});
	}

	public void showTodoListItem() {
		textViewNumberOfItems.setText("# of items: "
				+ todoList.getItems().size());
		// textViewOut.setText(todoList.getOutstandingItem());
		TodoListDisplayItemAdapter adapter = new TodoListDisplayItemAdapter(
				ViewSharedListActivity.this,
				R.layout.activity_todo_list_display_item_layout,
				todoList.getItems(), null) {
			@Override
			public boolean isEnabled(int position) {
				return false;
			}
		};
		listView.setAdapter(adapter);
	}

	public void showShoppingList() {
		ParseQuery<ShoppingListParser> parseQuery = new ParseQuery<ShoppingListParser>(
				ShoppingListParser.class);
		parseQuery.whereEqualTo("objectId", sharedLists.get(index)
				.getListObjectId());
		parseQuery.findInBackground(new FindCallback<ShoppingListParser>() {
			@Override
			public void done(List<ShoppingListParser> objects, ParseException e) {
				if (e == null) {
					shoppingList = new ShoppingList(objects.get(0));
					showShoppingListItem();
				}

			}
		});
	}

	public void showShoppingListItem() {
		textViewNumberOfItems.setText("# of items: "
				+ shoppingList.getItems().size());
		// textViewOut.setText(shoppingList.getOutstandingItem());
		ShoppingListDisplayItemAdapter adapter = new ShoppingListDisplayItemAdapter(
				ViewSharedListActivity.this, R.layout.display_item_layout,
				shoppingList.getItems(), null) {
			@Override
			public boolean isEnabled(int position) {
				return false;
			}
		};
		listView.setAdapter(adapter);
		if (shoppingList.getTotal() != 0) {
			textViewTotal.setText("Total: " + shoppingList.getTotal());
			textViewRemaining.setText("Remaining: "
					+ shoppingList.getRemaining());
			linearLayoutPrice.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra(SharedListByFriendActivity.SHARELIST_KEY, sharedLists);
		setResult(RESULT_OK, intent);
		finish();
	}

}
