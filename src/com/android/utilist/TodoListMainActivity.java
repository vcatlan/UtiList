package com.android.utilist;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import edu.unccUtilistProject.R;
import com.android.adapter.NavDrawerListAdapter;
import com.android.adapter.NavDropDownAdapter;
import com.android.model.History;
import com.android.model.HistoryParser;
import com.android.model.NavDrawerItem;
import com.android.model.NavDropDownHeader;
import com.android.model.NavDropDownItem;
import com.android.model.NavDropDownMenuItem;
import com.android.model.TodoList;
import com.android.model.TodoListParser;
import com.android.utilist.ItemHistoryFragment.ItemSelectDialogToActivity;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class TodoListMainActivity extends Activity implements
		OnNavigationListener, TodoListFragment.ICreateCallToActivity,
		ItemSelectDialogToActivity {
	final static String FRAGMENT_TAG_NAME = "todo_list_fragment";
	final static String TODO_LIST_KEY = "TODO_LIST_KEY";
	final static String CURR_TODO_LIST_KEY = "TODO_LIST_KEY";
	final static int TODO_LIST_REQUEST_COD = 1009;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ActionBar actionBar;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private ArrayList<NavDropDownItem> navDropDownItems;
	// private String allShoppingList = "test";
	private ArrayList<TodoListParser> listParsers;
	private ArrayList<TodoList> lists;
	private History history;
	private int currentTodoList;
	private TodoList currTodoList;
	private boolean isFragmentSetup = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_main);
		// getActionBar().hide();

		// Initialization
		mDrawerLayout = (DrawerLayout) findViewById(R.id.todo_drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.todo_left_drawer);
		actionBar = getActionBar();
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		lists = new ArrayList<TodoList>();

		// actionBar.hide();
		// Create Action Bar
		// ParseUser.getCurrentUser().logOut();
		ParseQuery<HistoryParser> parseHistory = new ParseQuery<HistoryParser>(
				HistoryParser.class);
		parseHistory.whereEqualTo("owner", ParseUser.getCurrentUser());
		ParseQuery<TodoListParser> parseQuery = new ParseQuery<TodoListParser>(
				TodoListParser.class);
		parseQuery.orderByDescending("updatedAt");
		parseQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
		try {
			if (parseHistory.find().isEmpty()) {
				history = new History();
				HistoryParser historyParser = new HistoryParser(history);
				historyParser.save();
				history = new History(historyParser);
			} else {
				history = new History(parseHistory.find().get(0));
			}
			listParsers = new ArrayList<TodoListParser>(parseQuery.find());
			if (listParsers.isEmpty()) {
				TodoListParser listParser = new TodoListParser(new TodoList(
						"To-Do List"));
				// listParser.setTitle();
				// ArrayList<ShoppingListItem> items = new
				// ArrayList<ShoppingListItem>();
				// items.add(new ShoppingListItem("Item1"));
				listParser.setItems("[]");
				listParser.save();
				listParsers = new ArrayList<TodoListParser>(parseQuery.find());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < listParsers.size(); i++) {
			TodoList todoList = new TodoList();
			todoList.setObjectId(listParsers.get(i).getObjectId());
			todoList.setTitle(listParsers.get(i).getTitle());
			todoList.setItems(listParsers.get(i).getItems());
			todoList.setOwnerId(listParsers.get(i).getOwner().getObjectId());
			todoList.setOutstandingItem();
			todoList.setUpdatedDate(listParsers.get(i).getUpdatedAt());
			lists.add(todoList);
		}
		// Log.d("demo", lists.toString());
		// Log.d("demo", lists.toString());
		onCreateActionBar();
		// MainFragment mainFragment = new MainFragment(lists.get(0), history);
		// fragmentTransaction.replace(R.id.content_frame, mainFragment,
		// lists.get(0).getTitle());
		// fragmentTransaction.commit();
		//

	}

	public void onCreateActionBar() {
		// Change Action Bar Background Color
		// actionBar.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#33B5E5")));

		// Change Action Bar Title Text
		// actionBar.setTitle("Action Bar");
		actionBar.setDisplayShowTitleEnabled(false);

		// Change Action Bar Icon
		// actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));

		// Enable Home Button
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Create Action Bar DropDown
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		onCreateDropDown();

		// Create Navigation Drawer Item List
		onCreateNavDrawer();

		/*
		 * Hack to enable Software Setting Button in Action Bar Even if device
		 * has Hardware Setting Button
		 */
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		mDrawerToggle.setDrawerIndicatorEnabled(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		actionBar.show();
	}

	public void onCreateNavDrawer() {
		// Set Adapter to navigation Drawer
		// mDrawerList.setAdapter(new
		// NavigationDrawerAdapter(MainActivity.this));

		ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
		String[] navDrawerItem = getResources().getStringArray(
				R.array.nav_drawer_items);
		String[] navDrawerItemIcon = getResources().getStringArray(
				R.array.nav_drawer_icons);

		navDrawerItem[0] = "Welcome "
				+ ParseUser.getCurrentUser().getString("firstName");
		navDrawerItem[1] = "Shopping List";

		for (int i = 0; i < navDrawerItemIcon.length; i++) {
			navDrawerItems.add(new NavDrawerItem(navDrawerItem[i],
					getResources().getIdentifier(navDrawerItemIcon[i],
							"drawable", getPackageName())));
		}
		NavDrawerListAdapter navDrawerListAdapter = new NavDrawerListAdapter(
				this, navDrawerItems);
		mDrawerList.setAdapter(navDrawerListAdapter);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					// Toast.makeText(TodoListMainActivity.this,
					// "Profile under Construction", Toast.LENGTH_SHORT).show();
				}
				if (position == 1) {
					// Intent intent = new Intent(TodoListMainActivity.this,
					// MainActivity.class);
					// startActivity(intent);
					mDrawerLayout.closeDrawers();
					finish();
				}
				if (position == 2) {
					Intent intent = new Intent(TodoListMainActivity.this,
							FriendsListActivity.class);
					startActivity(intent);
					mDrawerLayout.closeDrawers();
				}
				if (position == 3) {
					Toast.makeText(getApplicationContext(),
							"Settings under Construction", Toast.LENGTH_SHORT)
							.show();
				}
				if (position == 4) {
					Toast.makeText(TodoListMainActivity.this,
							"Settings under Construction", Toast.LENGTH_SHORT)
							.show();
				}
				if (position == 5) {
					Intent intent = new Intent(TodoListMainActivity.this,
							AboutActivity.class);
					startActivity(intent);
					mDrawerLayout.closeDrawers();
				}

			}

		});

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, R.drawable.ic_ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) {
				actionBar.setTitle("Action Bar");
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				actionBar.setTitle("Menu Opened");
				invalidateOptionsMenu();
			}
		};

	}

	public void onCreateDropDown() {
		// Action Bar Dropdown
		navDropDownItems = new ArrayList<NavDropDownItem>();
		navDropDownItems.add(new NavDropDownHeader(100, "RECENT TO-DO LIST"));
		int id = 101;
		for (int i = 0; i < lists.size(); i++) {
			navDropDownItems.add(new NavDropDownMenuItem(id, lists.get(i)
					.getTitle()));
			if (i == 2)
				break;
			id++;
		}
		navDropDownItems.add(new NavDropDownHeader(200, "ACTION"));
		navDropDownItems.add(new NavDropDownMenuItem(201,
				"Show all to-do lists"));
		navDropDownItems.add(new NavDropDownMenuItem(202, "Create new list"));
		NavDropDownAdapter navDropDwonAdapter = new NavDropDownAdapter(
				getActionBar().getThemedContext(), R.layout.navdropdown_item,
				navDropDownItems);
		actionBar.setListNavigationCallbacks(navDropDwonAdapter, this);

		actionBar.setSelectedNavigationItem(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.logOutButton:
			ParseUser.logOut();
			Intent intent = new Intent(getApplicationContext(),
					LogInActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return true;
		// return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Sync the toggle state
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		int showAllTodoList = navDropDownItems.size() - 2;
		int createTodoListCase = navDropDownItems.size() - 1;
		if (itemPosition == 1) {
			if (!isFragmentSetup) {
				currentTodoList = 0;
				TodoListFragment todoListFragment = new TodoListFragment(
						lists.get(0), history, currentTodoList);
				fragmentTransaction.replace(R.id.todo_content_frame,
						todoListFragment, FRAGMENT_TAG_NAME);
				fragmentTransaction.commit();
				isFragmentSetup = true;
				currTodoList = lists.get(0);
			} else {
				currentTodoList = 0;
				TodoListFragment f = (TodoListFragment) getFragmentManager()
						.findFragmentByTag(FRAGMENT_TAG_NAME);
				f.setList(lists.get(0), currentTodoList);
				currTodoList = lists.get(0);
			}
		}
		if (lists.size() >= 2) {
			if (itemPosition == 2) {
				currentTodoList = 1;
				TodoListFragment f = (TodoListFragment) getFragmentManager()
						.findFragmentByTag(FRAGMENT_TAG_NAME);
				f.setList(lists.get(1), currentTodoList);
				currTodoList = lists.get(1);
			}
		}
		if (lists.size() >= 3) {
			if (itemPosition == 3) {
				currentTodoList = 2;
				TodoListFragment f = (TodoListFragment) getFragmentManager()
						.findFragmentByTag(FRAGMENT_TAG_NAME);
				f.setList(lists.get(2), currentTodoList);
				currTodoList = lists.get(2);
			}
		}

		if (showAllTodoList == itemPosition
				|| createTodoListCase == itemPosition) {
			Intent intent = new Intent(TodoListMainActivity.this,
					TodoListActivity.class);
			intent.putExtra(CURR_TODO_LIST_KEY, currTodoList);
			// intent.putExtra(TODO_LIST_KEY, lists);
			startActivityForResult(intent, TODO_LIST_REQUEST_COD);
			currTodoList = null;
		}

		return false;
	}

	@Override
	public void fireIntentForResult(int requestCode, TodoList list, int index,
			String mode, int listIndex, History history) {
		Intent intent = new Intent(this, EditTodoListItemActivity.class);
		intent.putExtra(TodoListFragment.ITEM_NAME_KEY, list);
		intent.putExtra(TodoListFragment.ITEM_INDEX_KEY, index);
		intent.putExtra(TodoListFragment.ITEM_MODE_KEY, mode);
		intent.putExtra(TodoListFragment.ITEM_LIST_IDEX_KEY, listIndex);
		intent.putExtra(TodoListFragment.ITEM_HISTORY_KEY, history);
		startActivityForResult(intent, TodoListFragment.EDIT_REQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		TodoListFragment f = null;
		if (requestCode == TodoListFragment.EDIT_REQUESTCODE
				&& resultCode == Activity.RESULT_OK) {
			f = (TodoListFragment) getFragmentManager().findFragmentByTag(
					FRAGMENT_TAG_NAME);
			if (f != null) {
				int listIndex = data.getIntExtra(
						TodoListFragment.ITEM_LIST_IDEX_KEY, 0);
				history = (History) data
						.getSerializableExtra(TodoListFragment.ITEM_HISTORY_KEY);
				f.setData((TodoList) data
						.getSerializableExtra(TodoListFragment.ITEM_NAME_KEY),
						listIndex, history);
				lists.set(listIndex, (TodoList) data
						.getSerializableExtra(TodoListFragment.ITEM_NAME_KEY));
			}
		}

		if (requestCode == TodoListFragment.SPEECH_REQUESTCODE
				&& resultCode == Activity.RESULT_OK) {
			f = (TodoListFragment) getFragmentManager().findFragmentByTag(
					FRAGMENT_TAG_NAME);
			if (f != null) {
				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				f.setTextViewVoice(text.get(0));
			}
		}

		if (requestCode == TODO_LIST_REQUEST_COD) {
			if (resultCode == Activity.RESULT_OK) {
				lists = (ArrayList<TodoList>) data
						.getSerializableExtra(TODO_LIST_KEY);
				onCreateDropDown();

			}
		}

	}

	@Override
	public void fireIntentForSpeech(int requestCode) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

		try {
			startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException a) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Opps! Your device doesn't support Speech to Text",
					Toast.LENGTH_SHORT);
			t.show();
		}

	}

	@Override
	public void showItemHistory(int itemType, History history) {
		FragmentManager fm = getFragmentManager();
		ItemHistoryFragment editNameDialog = new ItemHistoryFragment(history,
				itemType);
		editNameDialog.show(fm, "fragment_item_history 1");

	}

	@Override
	public void ItemSelectDialogListener(String itemName) {
		TodoListFragment f = (TodoListFragment) getFragmentManager()
				.findFragmentByTag(FRAGMENT_TAG_NAME);
		if (f != null) {
			f.selectItemHistory(itemName);
		}

	}

}
