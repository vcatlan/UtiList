package com.example.utilist;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.utilist.R;
import com.example.adapter.NavDrawerListAdapter;
import com.example.adapter.NavDropDownAdapter;
import com.example.model.History;
import com.example.model.HistoryParser;
import com.example.model.NavDrawerItem;
import com.example.model.NavDropDownHeader;
import com.example.model.NavDropDownItem;
import com.example.model.NavDropDownMenuItem;
import com.example.model.SharedListParser;
import com.example.model.ShoppingList;
import com.example.model.ShoppingListItem;
import com.example.model.ShoppingListParser;
import com.example.utilist.ItemHistoryFragment.ItemSelectDialogToActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class MainActivity extends Activity implements OnNavigationListener, MainFragment.ICreateCallToActivity,ItemSelectDialogToActivity{
	final static String FRAGMENT_TAG_NAME = "list_fragment";
	final static String SHOPPING_LIST_KEY = "SHOOPING_LIST_KEY";
	final static String CURR_SHOPPING_LIST_KEY = "CURR_SHOOPING_LIST_KEY";
	final static int SHOPPING_LIST_REQUEST_COD = 1002;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ActionBar actionBar;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private ArrayList<NavDropDownItem> navDropDownItems;
	//private String allShoppingList = "test";
	private ArrayList<ShoppingListParser> listParsers;
	private ArrayList<ShoppingList> lists;
	private History history;
	private int currentShoppingList;
	private ShoppingList currShoppingList;
	private boolean isFragmentSetup = false; 
	private int newSharedlistCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//getActionBar().hide();
		
		
		// Initialization
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		actionBar = getActionBar();
		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		lists = new ArrayList<ShoppingList>();
		
		//actionBar.hide();
		// Create Action Bar
		//ParseUser.getCurrentUser().logOut();
		ParseQuery<HistoryParser> parseHistory = new ParseQuery<HistoryParser>(HistoryParser.class);
		parseHistory.whereEqualTo("owner", ParseUser.getCurrentUser());
		ParseQuery<ShoppingListParser> parseQuery = new ParseQuery<ShoppingListParser>(ShoppingListParser.class);
		parseQuery.orderByDescending("updatedAt");
		parseQuery.whereEqualTo("onwer", ParseUser.getCurrentUser());
		try {
			if(parseHistory.find().isEmpty()){
				history = new History();
				HistoryParser historyParser = new HistoryParser(history);
				historyParser.save();
				history = new History(historyParser);
			}
			else{
				history = new History(parseHistory.find().get(0));
			}
			listParsers = new ArrayList<ShoppingListParser>(parseQuery.find());
			if(listParsers.isEmpty()){
				ShoppingListParser listParser = new ShoppingListParser(new ShoppingList("Shopping List"));
				//listParser.setTitle();
				//ArrayList<ShoppingListItem> items = new ArrayList<ShoppingListItem>();
				//items.add(new ShoppingListItem("Item1"));
				//listParser.setItems("[]");
				listParser.save();
				listParsers = new ArrayList<ShoppingListParser>(parseQuery.find());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < listParsers.size(); i++) {
			ShoppingList shoppingList = new ShoppingList();
			shoppingList.setObjectId(listParsers.get(i).getObjectId());
			shoppingList.setTitle(listParsers.get(i).getTitle());
			shoppingList.setItems(listParsers.get(i).getItems());
			shoppingList.setTotal();
			shoppingList.setRemaining();
			shoppingList.setOutstandingItem();
			shoppingList.setUpdatedDate(listParsers.get(i).getUpdatedAt());
			shoppingList.setCreatedDate(listParsers.get(i).getCreatedAt());
			lists.add(shoppingList);
		}
		//Log.d("demo", lists.toString());
		onCreateActionBar();
//		MainFragment mainFragment = new MainFragment(lists.get(0), history);
//		fragmentTransaction.replace(R.id.content_frame, mainFragment, lists.get(0).getTitle());
//		fragmentTransaction.commit();
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

		//Create Action Bar DropDown
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
		
		NewSharedList();
	}

	public void onCreateNavDrawer() {
		// Set Adapter to navigation Drawer
		// mDrawerList.setAdapter(new
		// NavigationDrawerAdapter(MainActivity.this));
		
		
		ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
		String [] navDrawerItem = getResources().getStringArray(R.array.nav_drawer_items);
		String [] navDrawerItemIcon = getResources().getStringArray(R.array.nav_drawer_icons);
		
		navDrawerItem[0] = "Welcome " + ParseUser.getCurrentUser().getString("firstName");
		
		for (int i = 0; i < navDrawerItemIcon.length; i++) {
			navDrawerItems.add(new NavDrawerItem(navDrawerItem[i], getResources().getIdentifier(navDrawerItemIcon[i], "drawable", getPackageName())));
		}
		NavDrawerListAdapter navDrawerListAdapter = new NavDrawerListAdapter(
				this, navDrawerItems);
		mDrawerList.setAdapter(navDrawerListAdapter);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){
					//Toast.makeText(MainActivity.this, "Profile under Construction", Toast.LENGTH_SHORT).show();
				}
				if(position == 1){
					Intent intent = new Intent(MainActivity.this, TodoListMainActivity.class);
					startActivity(intent);
					mDrawerLayout.closeDrawers();
				}
				if(position == 2){
					Intent intent = new Intent(MainActivity.this, FriendsListActivity.class);
					startActivity(intent);
					mDrawerLayout.closeDrawers();
				}
				if(position == 3){
					Intent intent = new Intent(MainActivity.this, SharedListByFriendActivity.class);
					startActivity(intent);
					mDrawerLayout.closeDrawers();
				}
				if(position == 4){
					Toast.makeText(MainActivity.this, "Settings under Construction", Toast.LENGTH_SHORT).show();
				}
				if(position == 5){
					Intent intent = new Intent(MainActivity.this, AboutActivity.class);
					startActivity(intent);
					mDrawerLayout.closeDrawers();
				}
				
			}
			
		});
		
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, R.drawable.ic_ic_drawer, R.string.drawer_open,
						R.string.drawer_close) {
					public void onDrawerClosed(View view) {
						actionBar.setTitle("Action Bar");
						invalidateOptionsMenu();
					}

					public void onDrawerOpened(View drawerView) {
						actionBar.setTitle("Menu Opened");
						invalidateOptionsMenu();
					}
				};

	}

	public void onCreateDropDown() {
		// Action Bar Dropdown
		navDropDownItems = new ArrayList<NavDropDownItem>();
		navDropDownItems.add(new NavDropDownHeader( 100, "RECENT SHOPPING LIST"));
		int id = 101;
		for (int i = 0; i < lists.size(); i++) {
			navDropDownItems.add(new NavDropDownMenuItem(id,lists.get(i).getTitle()));
			if(i == 2)
				break;
			id++;
		}
		navDropDownItems.add(new NavDropDownHeader( 200, "ACTION"));
		navDropDownItems.add(new NavDropDownMenuItem(201,"Show all shopping lists"));
		navDropDownItems.add(new NavDropDownMenuItem(202,"Create new list"));
		NavDropDownAdapter navDropDwonAdapter = new NavDropDownAdapter(getActionBar().getThemedContext(),R.layout.navdropdown_item, navDropDownItems);
		//navDropDwonAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
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
			Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
			startActivity(intent);
			finish();
			
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Sync the toggle state
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		//ShoppingList currlist = null;
		int showAllShoppingList = navDropDownItems.size() - 2;
		int createShoppingListCase = navDropDownItems.size() - 1;
		if(itemPosition == 1){
			if(!isFragmentSetup){
				currentShoppingList = 0;
				currShoppingList = lists.get(0);
				MainFragment mainFragment = new MainFragment(lists.get(0), history, currentShoppingList);
				fragmentTransaction.replace(R.id.content_frame, mainFragment, FRAGMENT_TAG_NAME);
				fragmentTransaction.commit();
				isFragmentSetup = true;
				
			}
			else{
				currentShoppingList = 0;
				currShoppingList = lists.get(0);
				MainFragment f = (MainFragment)getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NAME);
				f.setList(lists.get(0), currentShoppingList);
				
			}
		}
		if(lists.size() >=2){
			if(itemPosition == 2){
				currentShoppingList = 1;
				currShoppingList = lists.get(1);
				MainFragment f = (MainFragment)getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NAME);
				f.setList(lists.get(1), currentShoppingList);
			}
		}
		if(lists.size() >=3){
			if(itemPosition == 3){
				currentShoppingList = 2;
				currShoppingList = lists.get(2);
				MainFragment f = (MainFragment)getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NAME);
				f.setList(lists.get(2), currentShoppingList);
			}
		}
		
		if(showAllShoppingList == itemPosition || createShoppingListCase == itemPosition){
			Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
			intent.putExtra(CURR_SHOPPING_LIST_KEY, currShoppingList);
			//intent.putExtra(SHOPPING_LIST_KEY, lists);
			startActivityForResult(intent, SHOPPING_LIST_REQUEST_COD);
			currShoppingList = null;
		}
		
		return false;
	}

	@Override
	public void fireIntentForResult(int requestCode, ShoppingList list, int index, String mode, History history, int listIndex) {
		Intent intent = new Intent(this,
				EditItemActivity.class);
		intent.putExtra(MainFragment.ITEM_NAME_KEY, list);
		intent.putExtra(MainFragment.ITEM_INDEX_KEY, index);
		intent.putExtra(MainFragment.ITEM_MODE_KEY, mode);
		intent.putExtra(MainFragment.HISTORY_KEY, history);
		intent.putExtra(MainFragment.ITEM_LIST_INDEX_KEY, listIndex);
		startActivityForResult(intent, MainFragment.EDIT_REQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		MainFragment f = null;
		if(requestCode == MainFragment.EDIT_REQUESTCODE && resultCode == Activity.RESULT_OK){			
			f = (MainFragment)getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NAME);
			if(f != null){
				history = (History) data.getSerializableExtra(MainFragment.HISTORY_KEY);
				int listIndex = (Integer) data.getIntExtra(MainFragment.ITEM_LIST_INDEX_KEY, 0);
				lists.set(listIndex, (ShoppingList) data.getSerializableExtra(MainFragment.ITEM_NAME_KEY));
				Log.d("demo", ((ShoppingList) data.getSerializableExtra(MainFragment.ITEM_NAME_KEY)).getItems().toString());
				f.setData((ShoppingList) data.getSerializableExtra(MainFragment.ITEM_NAME_KEY), history, listIndex);
				
			}
		}
		
		if(requestCode == MainFragment.SPEECH_REQUESTCODE && resultCode == Activity.RESULT_OK){			
			f = (MainFragment)getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NAME);
			if(f != null){
				ArrayList<String> text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				f.setTextViewVoice(text.get(0));
			}
		}
		
		if(requestCode == MainFragment.SCAN_REQUESTCODE){
			if(resultCode == Activity.RESULT_OK){
				f = (MainFragment)getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NAME);
				if(f != null){
					//Log.d("demo", ((ShoppingListItem) data.getSerializableExtra(ScanActivity.SCANNED_ITEM_KEY)).toString());
					f.setItemToList((ShoppingListItem) data.getSerializableExtra(ScanActivity.SCANNED_ITEM_KEY));
				}
			}
			else if(resultCode == Activity.RESULT_CANCELED){
				f = (MainFragment)getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NAME);
				if(f != null){
					f.setItemToList(null);
				}
			}
		}
		
		if(requestCode == SHOPPING_LIST_REQUEST_COD){			
			if(resultCode == Activity.RESULT_OK){
				lists = (ArrayList<ShoppingList>) data.getSerializableExtra(SHOPPING_LIST_KEY);
				currShoppingList = null;
				onCreateDropDown();

			}
		}
		
	}

	@Override
	public void ItemSelectDialogListener(String itemName) {
		MainFragment f = (MainFragment)getFragmentManager().findFragmentByTag(FRAGMENT_TAG_NAME);
		if(f != null){
			f.selectItemHistory(itemName);
		}
		
	}

	@Override
	public void showItemHistory(int itemType, History history) {
		FragmentManager fm = getFragmentManager();
        ItemHistoryFragment editNameDialog = new ItemHistoryFragment(history, itemType);
        editNameDialog.show(fm, "fragment_item_history");	
	}

	@Override
	public void fireIntentForSpeech(int requestCode) {
		Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

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
	public void fireIntentForScan(int requestCode) {
		Intent intent = new Intent(this, ScanActivity.class);
		startActivityForResult(intent, requestCode);
	}
	
	public void NewSharedList(){
		ParseQuery<SharedListParser> parseQuery = new ParseQuery<SharedListParser>(SharedListParser.class);
		parseQuery.whereEqualTo("sharedToObjectId", ParseUser.getCurrentUser().getObjectId());
		parseQuery.findInBackground(new FindCallback<SharedListParser>() {
			public void done(List<SharedListParser> objects, ParseException e) {
				if(e==null){
					for(SharedListParser s : objects){
						if(s.isNewList())
							newSharedlistCount += 1;
					}
					showAlert();
				}
				
			}
		});
	}
	
	public void showAlert(){
		if(newSharedlistCount != 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			if(newSharedlistCount == 1)
				builder.setMessage("You have a new shared list...");
			else
				builder.setMessage("You have "+ newSharedlistCount + " new shared lists...");
			builder.setPositiveButton("View Shared List", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent inten = new Intent(MainActivity.this, SharedListByFriendActivity.class);
					startActivity(inten);
					
				}
			});
			builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			builder.create().show();
			
		}
	}
}
