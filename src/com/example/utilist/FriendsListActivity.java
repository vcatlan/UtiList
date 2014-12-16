package com.example.utilist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.adapter.DisplayFriendAdapter;
import com.example.model.FriendList;
import com.example.model.FriendListItem;
import com.example.model.FriendListParser;
import com.example.model.SharedList;
import com.example.model.SharedListParser;
import com.example.model.ShoppingList;
import com.example.model.ShoppingListParser;
import com.example.model.TodoList;
import com.example.model.TodoListParser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FriendsListActivity extends Activity implements DisplayFriendAdapter.ICreateCallToFriendListItemClick{

	private Button addFriend, shareList;
	private ListView friendsList;
	private AlertDialog alertDialog;
	private DisplayFriendAdapter adapter;
	private FriendList list;
	private FriendListParser listParser;
	// private ArrayList<FriendListItem> friends;
	private FriendListItem friendListItem;
	private PopupWindow popup;
	private ArrayList<TodoList> todoLists;
	private ArrayList<ShoppingList> shoppingLists;
	private ParseQuery<TodoListParser> parseQueryTodo;
	private ParseQuery<ShoppingListParser> parseQueryShop;
	private ArrayList<Integer> selectedFriends;
	private ArrayList<Integer> selectedTodoList;
	private ArrayList<Integer> selectedShoppingList;
	private Button iv_share_todo;
	private Button iv_share_shopping;
	private CharSequence[] charSequencesTodo;
	private CharSequence[] charSequencesShopping;
	private ArrayList<SharedList> sharedLists;
	private ArrayList<SharedListParser> sharedListParsers;
	private String sharedListType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_list);

		addFriend = (Button) findViewById(R.id.buttonAddFriend);
		shareList = (Button) findViewById(R.id.buttonShareList);
		friendsList = (ListView) findViewById(R.id.listViewFriends);
		shoppingLists = new ArrayList<ShoppingList>();
		todoLists = new ArrayList<TodoList>();
		selectedFriends = new ArrayList<Integer>();
		selectedShoppingList = new ArrayList<Integer>();
		selectedTodoList = new ArrayList<Integer>();
		sharedLists = new ArrayList<SharedList>();
		// friends = new ArrayList<FriendListItem>();
		ParseQuery<FriendListParser> parseQuery = new ParseQuery<FriendListParser>(
				FriendListParser.class);
		parseQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
		parseQuery.findInBackground(new FindCallback<FriendListParser>() {
			public void done(List<FriendListParser> objects, ParseException e) {
				if (objects.isEmpty()) {
					listParser = new FriendListParser();
					listParser.setItems("[]");
					listParser.setOwner(ParseUser.getCurrentUser());
					listParser.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException e) {
							if (e == null) {
								list = new FriendList(listParser);
								showFriendsList();
							}

						}
					});

				} else {
					list = new FriendList(objects.get(0));
					showFriendsList();
				}

			}
		});
		parseQueryShop = new ParseQuery<ShoppingListParser>(
				ShoppingListParser.class);
		parseQueryShop.whereEqualTo("onwer", ParseUser.getCurrentUser());
		parseQueryShop.findInBackground(new FindCallback<ShoppingListParser>() {
			public void done(List<ShoppingListParser> objects, ParseException e) {
				if (e == null) {
					charSequencesShopping = new CharSequence[objects.size()];
					for (int i = 0; i < objects.size(); i++) {
						charSequencesShopping[i] = objects.get(i).getTitle();
						shoppingLists.add(new ShoppingList(objects.get(i)));
						// Log.d("demo", shoppingLists.toString());
					}
				}

			}
		});
		parseQueryTodo = new ParseQuery<TodoListParser>(TodoListParser.class);
		parseQueryTodo.whereEqualTo("owner", ParseUser.getCurrentUser());
		parseQueryTodo.findInBackground(new FindCallback<TodoListParser>() {
			public void done(List<TodoListParser> objects, ParseException e) {
				if (e == null) {
					charSequencesTodo = new CharSequence[objects.size()];
					for (int i = 0; i < objects.size(); i++) {
						charSequencesTodo[i] = objects.get(i).getTitle();
						todoLists.add(new TodoList(objects.get(i)));
						// Log.d("demo", todoLists.toString());
					}
				}

			}
		});
		
		ParseQuery<SharedListParser> parseQuerySharedList = new ParseQuery<SharedListParser>(SharedListParser.class);
		parseQuerySharedList.whereEqualTo("sharedByObjectId", ParseUser.getCurrentUser().getObjectId());
		parseQuerySharedList.findInBackground(new FindCallback<SharedListParser>() {
			public void done(List<SharedListParser> objects, ParseException e) {
				if(e == null){
					for(SharedListParser s: objects){
						sharedLists.add(new SharedList(s));
					}
				}
				
			}
		});

	}

	public void userNotFound() {
		Toast.makeText(getApplicationContext(), "User Not found",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}
	
	public void showFriendsList() {
		// friends = list.getItems();
		adapter = new DisplayFriendAdapter(this,
				R.layout.display_friend_layout, list.getItems(), FriendsListActivity.this);
		friendsList.setAdapter(adapter);
		adapter.setNotifyOnChange(true);

		addFriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						FriendsListActivity.this);
				alert.setTitle("Add Friend");
				alert.setMessage("Please search by username(same as email)");
				// Set an EditText view to get user input
				final EditText input = new EditText(FriendsListActivity.this);
				alert.setView(input)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						String friend = input.getText()
								.toString();
						if (friend.equals("")) {
							Toast.makeText(
									getApplicationContext(),
									"Invalid friend search",
									Toast.LENGTH_SHORT).show();
						} 
						else {
							if(!ParseUser.getCurrentUser().getEmail().equals(friend)){
								ParseQuery<ParseUser> query = ParseUser.getQuery();
								query.whereEqualTo("username",friend);
								query.findInBackground(new FindCallback<ParseUser>() {
								public void done(List<ParseUser> objects, ParseException e) {
								if (e == null) {
									if (objects.size() != 0) {
										if (!list.isExist(objects.get(0).getObjectId())) {
											friendListItem = new FriendListItem();
											friendListItem.setEmail(objects.get(0).getEmail());
											friendListItem.setFirstName(objects.get(0).getString("firstName"));
											friendListItem.setLastName(objects.get(0).getString("lastName"));
											friendListItem.setObjectId(objects.get(0).getObjectId());
											//friendListItem.setParseUser(objects.get(0));
											adapter.add(friendListItem);
											listParser = new FriendListParser(list);
											listParser.saveInBackground();} 
										else {
											Toast.makeText(getApplicationContext(),"The new friend is aleardy in your list!",Toast.LENGTH_SHORT).show();
										}
									} 
									else {
											userNotFound();
									}
								} 
								else {
									Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
								}
								}
								});
							}
							else
								Toast.makeText(getApplicationContext(), "You cannot add yourself as friend" ,Toast.LENGTH_SHORT).show();
							
							alertDialog.dismiss();
						}
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						alertDialog.dismiss();
					}
				});

				alertDialog = alert.create();
				alertDialog.show();
			}
		});

		shareList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectedFriends.size() > 0)
					displayPopupWindow(shareList);
				else {
					AlertDialog.Builder alert = new AlertDialog.Builder(
							FriendsListActivity.this);
					// alert.setTitle("Error");
					alert.setMessage("Select a friend first");
					alert.create().show();
				}
			}
		});
	}

	public void shareTodoList() {
		ArrayList<SharedListParser> newSharedListsParsers = new ArrayList<SharedListParser>(); 
		ParseUser user = ParseUser.getCurrentUser();
		for(Integer t: selectedTodoList){
			for(Integer f: selectedFriends){
				SharedList sharedList = new SharedList(todoLists.get(t).getObjectId(), user.getObjectId(), todoLists.get(t).getTitle(), user.getString("firstName") + " " + user.getString("lastName"), true, "todoList", list.getItems().get(f).getObjectId());
				if(sharedLists.indexOf(sharedList) == -1){
					newSharedListsParsers.add(new SharedListParser(sharedList));
				}
			}
		}
		//SharedListParser sharedListParser = new SharedListParser();
		SharedListParser.saveAllInBackground(newSharedListsParsers, new SaveCallback() {
			public void done(ParseException e) {
				if(e == null){
					ParseQuery<SharedListParser> parseQuerySharedList = new ParseQuery<SharedListParser>(SharedListParser.class);
					parseQuerySharedList.whereEqualTo("sharedByObjectId", ParseUser.getCurrentUser().getObjectId());
					parseQuerySharedList.findInBackground(new FindCallback<SharedListParser>() {
						public void done(List<SharedListParser> objects, ParseException e) {
							if(e == null){
								sharedLists = new ArrayList<SharedList>();
								for(SharedListParser s: objects){
									sharedLists.add(new SharedList(s));
								}
							}
							
						}
					});
				}
				
			}
		});
		
		selectedTodoList = new ArrayList<Integer>();
		selectedFriends = new ArrayList<Integer>();
		friendsList.setAdapter(adapter);
		successfullSharing();
	}

	public void showTodoList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FriendsListActivity.this);
		builder.setTitle("Select lists to share");
		builder.setMultiChoiceItems(charSequencesTodo, null,
				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				if (isChecked == true)
					selectedTodoList.add(Integer.valueOf(which));
				else if (isChecked == false)
					selectedTodoList.remove(Integer.valueOf(which));

			}
		});
		builder.setPositiveButton("Share",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				shareTodoList();
				sharedListType = "todoList";
				dialog.dismiss();

			}

		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedTodoList = new ArrayList<Integer>();
				dialog.cancel();

			}

		});
		builder.create().show();

	}

	public void shareShoppingList() {
		ArrayList<SharedListParser> newSharedListsParsers = new ArrayList<SharedListParser>(); 
		ParseUser user = ParseUser.getCurrentUser();
		for(Integer t: selectedShoppingList){
			for(Integer f: selectedFriends){
				SharedList sharedList = new SharedList(shoppingLists.get(t).getObjectId(), user.getObjectId(), shoppingLists.get(t).getTitle(), user.getString("firstName") + " " + user.getString("lastName"), true, "shoppingList", list.getItems().get(f).getObjectId());
				if(sharedLists.indexOf(sharedList) == -1){
					newSharedListsParsers.add(new SharedListParser(sharedList));
				}
			}
		}
		//SharedListParser sharedListParser = new SharedListParser();
		SharedListParser.saveAllInBackground(newSharedListsParsers, new SaveCallback() {
			public void done(ParseException e) {
				if(e == null){
					ParseQuery<SharedListParser> parseQuerySharedList = new ParseQuery<SharedListParser>(SharedListParser.class);
					parseQuerySharedList.whereEqualTo("sharedByObjectId", ParseUser.getCurrentUser().getObjectId());
					parseQuerySharedList.findInBackground(new FindCallback<SharedListParser>() {
						public void done(List<SharedListParser> objects, ParseException e) {
							if(e == null){
								sharedLists = new ArrayList<SharedList>();
								for(SharedListParser s: objects){
									sharedLists.add(new SharedList(s));
								}
							}
							
						}
					});
				}
				
			}
		});
		
		selectedShoppingList = new ArrayList<Integer>();
		selectedFriends = new ArrayList<Integer>();
		friendsList.setAdapter(adapter);
		successfullSharing();
	}

	public void showShoppingList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FriendsListActivity.this);
		builder.setTitle("Select lists to share");
		builder.setMultiChoiceItems(charSequencesShopping, null,
				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				if (isChecked == true)
					selectedShoppingList.add(Integer.valueOf(which));
				else if (isChecked == false)
					selectedShoppingList.remove(Integer.valueOf(which));

			}
		});
		builder.setPositiveButton("Share",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				shareShoppingList();
				sharedListType = "shoppingList";
				dialog.dismiss();

			}

		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedShoppingList = new ArrayList<Integer>();
				dialog.cancel();

			}

		});
		builder.create().show();
	}

	private void displayPopupWindow(View anchorView) {
		popup = new PopupWindow(FriendsListActivity.this);
		View view = getLayoutInflater().inflate(
				R.layout.activity_friends_list_share_button, null);
		iv_share_todo = (Button) view
				.findViewById(R.id.activity_friends_popup_button_todo);
		iv_share_shopping = (Button) view
				.findViewById(R.id.activity_friends_popup_button_shopping);

		iv_share_todo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTodoList();
				popup.dismiss();

			}
		});

		iv_share_shopping.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showShoppingList();
				popup.dismiss();

			}
		});

		popup.setContentView(view);

		// Set content width and height
		popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

		// Closes the popup window when touch outside of it - when looses focus
		popup.setOutsideTouchable(true);
		popup.setFocusable(true);

		// Show anchored to button
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.showAsDropDown(anchorView);
	}

	@Override
	public void checkBokOnClick(View v) {
		if (((CheckBox) v).isChecked() == true)
			selectedFriends.add((Integer)Integer.parseInt(v.getTag().toString()));
		else if (((CheckBox) v).isChecked() == false)
			selectedFriends.remove((Integer)Integer.parseInt(v.getTag().toString()));
		
	}
	
	@Override
	public void deleteOnClick(View v) {
		list.getItems().remove(Integer.parseInt(v.getTag().toString()));
		adapter.notifyDataSetChanged();
		listParser = new FriendListParser(list);
		listParser.saveInBackground();	
	}
	
	public void successfullSharing(){
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FriendsListActivity.this);
		builder.setMessage("You have successfully shared the lists");
		builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		builder.create().show();
	}

	
	
	

}
