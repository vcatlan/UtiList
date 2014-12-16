package com.example.utilist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.utilist.R;
import com.example.model.History;
import com.example.model.HistoryParser;
import com.example.model.ShoppingList;
import com.example.model.ShoppingListItem;
import com.example.model.ShoppingListParser;

public class EditItemActivity extends Activity {
	final static String DETAILS_ITEM_KEY = "details_item_key";
	
	private Button cancel, save;
	private EditText itemName, itemQty, itemPrice, itemNote;
	private ImageView priceHistory, itemDelete, qtyPlus, qtyMinus;
	private Spinner itemType;
	private ShoppingListItem item;
	private ShoppingList list;
	private ShoppingListParser listParser;
	private HistoryParser historyParser;
	private History history;
	private int index, listIndex;
	private String mode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		getActionBar().hide();
		cancel = (Button) findViewById(R.id.dialog_shopping_item_button_cancel);
		save = (Button) findViewById(R.id.dialog_shopping_item_button_save);
		itemName = (EditText) findViewById(R.id.editTextItemName);
		itemQty = (EditText) findViewById(R.id.editTextQuantity);
		itemPrice = (EditText) findViewById(R.id.editTextItemPrice);
		itemType = (Spinner) findViewById(R.id.spinnerItemType);
		itemNote = (EditText) findViewById(R.id.editTextItemNote);
		priceHistory = (ImageView) findViewById(R.id.imageViewPriceHistory);
		itemDelete = (ImageView) findViewById(R.id.imageViewDelete);
		qtyPlus = (ImageView) findViewById(R.id.imageViewPlusQty);
		qtyMinus = (ImageView) findViewById(R.id.imageViewMinusQty);
		list = (ShoppingList) getIntent().getSerializableExtra(MainFragment.ITEM_NAME_KEY);
		index = (Integer) getIntent().getIntExtra(MainFragment.ITEM_INDEX_KEY,0);
		listIndex = (Integer) getIntent().getIntExtra(MainFragment.ITEM_LIST_INDEX_KEY,0);
		mode = getIntent().getStringExtra(MainFragment.ITEM_MODE_KEY);
		history = (History) getIntent().getSerializableExtra(MainFragment.HISTORY_KEY);
		item = list.getItems().get(index);
		itemName.setText(item.getName());
		itemQty.setText(item.getQuantity()+"");
		itemPrice.setText(item.getUnitPrice()+"");
		itemNote.setText(item.getNote());
		for (int i = 0; i < getResources().getStringArray(R.array.type).length; i++) {
			if(item.getType().toLowerCase().equals(getResources().getStringArray(R.array.type)[i]))
				itemType.setSelection(i);
		}
		qtyPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int qty = Integer.valueOf(itemQty.getText().toString());
				qty++;
				itemQty.setText(Integer.toString(qty));
			}
		});

		qtyMinus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int qty = Integer.parseInt(itemQty.getText().toString());
				qty--;
				if (qty < 1) {
					qty = 1;
					itemQty.setText(Integer.toString(qty));
				} else {
					itemQty.setText(Integer.toString(qty));
				}
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mode.equals("adding")){
					list.getItems().remove(index);
					list.setTRO();
				}
				Intent intent = new Intent();
				intent.putExtra(MainFragment.ITEM_NAME_KEY, list);
				intent.putExtra(MainFragment.HISTORY_KEY, history);
				intent.putExtra(MainFragment.ITEM_LIST_INDEX_KEY, listIndex);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(itemName.getText().toString().equals(""))
					Toast.makeText(EditItemActivity.this, "Item's name cannot be empty", Toast.LENGTH_SHORT).show();
				else{
					item.setName(itemName.getText().toString());
					item.setType(itemType.getSelectedItem().toString());
					if(!itemQty.getText().toString().equals(""))
						item.setQuantity((Integer.valueOf(itemQty.getText().toString())==0) ? 1 : (Integer.valueOf(itemQty.getText().toString())));
					else
						item.setQuantity(1);
					if(!itemPrice.getText().toString().equals(""))
						item.setUnitPrice(Double.valueOf(itemPrice.getText().toString()));
					else
						item.setUnitPrice(0);
					item.setNote(itemNote.getText().toString());
					list.getItems().set(index, item);
					list.setTRO();
					history.getItemNameHistoryList().add(itemName.getText().toString());
					if(!itemPrice.getText().toString().equals("")){
						if(Double.valueOf(itemPrice.getText().toString()) !=0 )
							history.getPriceHistoryList().add(Double.valueOf(itemPrice.getText().toString()));
					}
					saveList();
				}
			}
		});
		
		itemDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				list.getItems().remove(index);
				list.setTRO();
				saveList();
				
			}
		});
		
		priceHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CharSequence [] charSequences = new CharSequence[history.getPriceHistoryList().size()]; 
				AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
				builder.setTitle("Select Price");
				if(charSequences.length != 0)
					builder.setItems(history.getPriceCarSequence(), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							itemPrice.setText(history.getPriceCarSequence()[which]);
							
						}
					});
				else
					builder.setMessage("The Price History is empty");
				builder.create().show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		if(mode.equals("adding")){
			list.getItems().remove(index);
			list.setTRO();
			Log.d("demo", list.getItems().toString());
		}
		saveList();
	}
	
	public void saveList(){
		listParser = new ShoppingListParser(list);
		historyParser = new HistoryParser(history);
		historyParser.saveInBackground();
		listParser.saveInBackground();
		Intent intent = new Intent();
		intent.putExtra(MainFragment.ITEM_NAME_KEY, list);
		intent.putExtra(MainFragment.HISTORY_KEY, history);
		intent.putExtra(MainFragment.ITEM_LIST_INDEX_KEY, listIndex);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	
	

}