package com.android.utilist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import edu.unccUtilistProject.R;
import com.android.adapter.ShoppingListAdapter;
import com.android.model.SharedListParser;
import com.android.model.ShoppingList;
import com.android.model.ShoppingListParser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ShoppingListActivity extends Activity implements
		View.OnClickListener, TextWatcher {
	private EditText et_add_shopping_list;
	private ImageView iv_right;
	private ImageView iv_left;
	private ImageView iv_delete;
	private ImageView iv_edit;
	private ImageView iv_open;
	private ListView lv_shopping_list;
	private String mode = "adding";
	private int editingModeIndex = -1;
	private ShoppingListAdapter shoppingListAdapter;
	private ArrayList<ShoppingList> lists;
	// private ArrayList<ShoppingListParser> listParsers;
	private ShoppingList list;
	private ShoppingListParser listParser;
	private PopupWindow popup;
	private ShoppingList currShoppingList;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);
		getActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		et_add_shopping_list = (EditText) findViewById(R.id.shopping_list_editText_add);
		iv_left = (ImageView) findViewById(R.id.shopping_list_imageViewLeft);
		iv_right = (ImageView) findViewById(R.id.shopping_list_imageViewRight);
		lv_shopping_list = (ListView) findViewById(R.id.shopping_list_listView);
		et_add_shopping_list.addTextChangedListener(this);
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		// ParseQuery<ShoppingListParser> parseQuery = new
		// ParseQuery<ShoppingListParser>(
		// ShoppingListParser.class);
		// parseQuery.whereEqualTo("onwer", ParseUser.getCurrentUser());
		// try {
		// listParsers = new ArrayList<ShoppingListParser>(parseQuery.find());
		// lists = new ArrayList<ShoppingList>();
		//
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// for (int i = 0; i < listParsers.size(); i++) {
		// ShoppingList shoppingList = new ShoppingList();
		// shoppingList.setObjectId(listParsers.get(i).getObjectId());
		// shoppingList.setTitle(listParsers.get(i).getTitle());
		// shoppingList.setItems(listParsers.get(i).getItems());
		// shoppingList.setTotal();
		// shoppingList.setRemaining();
		// shoppingList.setOutstandingItem();
		// lists.add(shoppingList);
		//
		// }
		currShoppingList = (ShoppingList) getIntent().getSerializableExtra(
				MainActivity.CURR_SHOPPING_LIST_KEY);
		lists = new ArrayList<ShoppingList>();
		// lists =
		// (ArrayList<ShoppingList>)getIntent().getSerializableExtra(MainActivity.SHOPPING_LIST_KEY);
		ParseQuery<ShoppingListParser> parseQuery = new ParseQuery<ShoppingListParser>(
				ShoppingListParser.class);
		parseQuery.whereEqualTo("onwer", ParseUser.getCurrentUser());
		parseQuery.orderByDescending("createdAt");
		parseQuery.findInBackground(new FindCallback<ShoppingListParser>() {
			@Override
			public void done(List<ShoppingListParser> objects, ParseException e) {
				if (e == null) {
					for (ShoppingListParser p : objects) {
						lists.add(new ShoppingList(p));
					}
					showShoppingList();
				}
			}
		});

	}

	public void showShoppingList() {
		shoppingListAdapter = new ShoppingListAdapter(this,
				R.layout.activity_shopping_list_item_display, lists);
		lv_shopping_list.setAdapter(shoppingListAdapter);
		lv_shopping_list
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						displayPopupWindow(view, position);
						return false;
					}
				});
		lv_shopping_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				saveBeforeOpen(position);
			}
		});
		shoppingListAdapter.setNotifyOnChange(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shopping_list_imageViewLeft:
			et_add_shopping_list.setText("");
			break;
		case R.id.shopping_list_imageViewRight:
			if (mode.equals("adding") && editingModeIndex == -1) {
				list = new ShoppingList();
				list.setTitle(et_add_shopping_list.getText().toString());
				listParser = new ShoppingListParser(list);
				listParser.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						list.setObjectId(listParser.getObjectId());
						list.setUpdatedDate(listParser.getUpdatedAt());
						list.setCreatedDate(listParser.getCreatedAt());
						shoppingListAdapter.add(list);
						sortByCreatedDate();
					}
				});
			} else if (mode.equals("editing") && editingModeIndex != -1) {
				lists.get(editingModeIndex).setTitle(
						et_add_shopping_list.getText().toString());
				shoppingListAdapter.notifyDataSetChanged();
				listParser = new ShoppingListParser(lists.get(editingModeIndex));
				listParser.saveInBackground();
				mode = "adding";
				editingModeIndex = -1;
			}
			et_add_shopping_list.setText("");
			break;
		default:
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (et_add_shopping_list.getText().toString().length() > 0) {
			iv_left.setVisibility(View.VISIBLE);
			iv_right.setVisibility(View.VISIBLE);
		} else {
			iv_left.setVisibility(View.GONE);
			iv_right.setVisibility(View.GONE);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	public void deleteList(int index) {
		String objectId = lists.get(index).getObjectId();
		listParser = new ShoppingListParser(lists.get(index));
		listParser.deleteInBackground();
		lists.remove(index);
		// sortByCreatedDate();
		shoppingListAdapter.notifyDataSetChanged();
		ParseQuery<SharedListParser> parseQuery = new ParseQuery<SharedListParser>(
				SharedListParser.class);
		parseQuery.whereEqualTo("listObjectId", objectId);
		parseQuery.findInBackground(new FindCallback<SharedListParser>() {
			@Override
			public void done(List<SharedListParser> objects, ParseException e) {
				if (e == null) {
					for (SharedListParser s : objects) {
						try {
							s.delete();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

			}
		});

	}

	public void editListTitle(int index) {
		mode = "editing";
		editingModeIndex = index;
		et_add_shopping_list.setText(lists.get(index).getTitle());
	}

	public void saveBeforeOpen(final int index) {
		pd.show();
		listParser = new ShoppingListParser(lists.get(index));
		listParser.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					lists.set(index, new ShoppingList(listParser));
					sortShoppingListByUpdatedDate();
					openList();
				}
			}
		});
	}

	public void openList() {
		Intent intent = new Intent();
		intent.putExtra(MainActivity.SHOPPING_LIST_KEY, lists);
		setResult(RESULT_OK, intent);
		pd.dismiss();
		finish();
	}

	private void displayPopupWindow(View anchorView, final int index) {
		popup = new PopupWindow(ShoppingListActivity.this);
		View view = getLayoutInflater().inflate(
				R.layout.activity_shopping_list_popup_window, null);
		iv_delete = (ImageView) view
				.findViewById(R.id.shopping_list_imageView_delete);
		iv_edit = (ImageView) view
				.findViewById(R.id.shopping_list_imageView_edit);
		iv_open = (ImageView) view
				.findViewById(R.id.shopping_list_imageView_open);
		if (lists.size() == 1)
			iv_delete.setVisibility(View.GONE);
		iv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteList(index);
				popup.dismiss();

			}
		});
		iv_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editListTitle(index);
				popup.dismiss();

			}
		});
		iv_open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveBeforeOpen(index);
				popup.dismiss();

			}
		});

		popup.setContentView(view);

		// Set content width and height
		popup.setHeight(LayoutParams.WRAP_CONTENT);
		popup.setWidth(LayoutParams.MATCH_PARENT);

		// Closes the popup window when touch outside of it - when looses focus
		popup.setOutsideTouchable(true);
		popup.setFocusable(true);

		// Show anchored to button
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.showAsDropDown(anchorView);
	}

	public void sortShoppingListByUpdatedDate() {
		Collections.sort(lists, new Comparator<ShoppingList>() {

			@Override
			public int compare(ShoppingList lhs, ShoppingList rhs) {
				if (lhs.getUpdatedDate().compareTo(rhs.getUpdatedDate()) > 0)
					return -1;
				if (lhs.getUpdatedDate().compareTo(rhs.getUpdatedDate()) < 0)
					return 1;
				return 0;
			}

		});
	}

	@Override
	public void onBackPressed() {
		if (lists.indexOf(currShoppingList) != -1)
			saveBeforeOpen(lists.indexOf(currShoppingList));
		else
			saveBeforeOpen(0);
	}

	public void sortByCreatedDate() {
		Collections.sort(lists, new Comparator<ShoppingList>() {

			@Override
			public int compare(ShoppingList lhs, ShoppingList rhs) {
				if (lhs.getCreatedDate().compareTo(rhs.getCreatedDate()) > 0)
					return -1;
				if (lhs.getCreatedDate().compareTo(rhs.getCreatedDate()) < 0)
					return 1;
				return 0;
			}

		});
	}

}
