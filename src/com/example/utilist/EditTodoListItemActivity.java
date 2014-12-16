package com.example.utilist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.utilist.R;
import com.example.model.History;
import com.example.model.HistoryParser;
import com.example.model.TodoList;
import com.example.model.TodoListItem;
import com.example.model.TodoListParser;

public class EditTodoListItemActivity extends Activity {

final static String DETAILS_ITEM_KEY = "details_item_key";
	
	private Button cancel, save;
	private EditText itemName, itemDate, itemTime, itemNote;
	private ImageView clear, itemDelete;
	private TodoListItem item;
	private TodoList list;
	private TodoListParser listParser;
	private int index, listIndex;
	private String mode;
	private History history; 
	private HistoryParser historyParser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_todo_list);
		getActionBar().hide();
		cancel = (Button) findViewById(R.id.todo_item_button_cancel);
		save = (Button) findViewById(R.id.todo_item_button_save);
		itemName = (EditText) findViewById(R.id.todo_editTextItemName);
		itemDate = (EditText) findViewById(R.id.todo_date_editText);
		itemTime = (EditText) findViewById(R.id.todo_editText_time);
		itemNote = (EditText) findViewById(R.id.todo_editTextItemNote);
		clear = (ImageView) findViewById(R.id.todo_imageView_clear);
		itemDelete = (ImageView) findViewById(R.id.todo_imageViewDelete);
		itemDate.setKeyListener(null);
		itemTime.setKeyListener(null);
		list = (TodoList) getIntent().getSerializableExtra(TodoListFragment.ITEM_NAME_KEY);
		history = (History) getIntent().getSerializableExtra(TodoListFragment.ITEM_HISTORY_KEY);
		index = (Integer) getIntent().getIntExtra(TodoListFragment.ITEM_INDEX_KEY,0);
		listIndex = (Integer) getIntent().getIntExtra(TodoListFragment.ITEM_LIST_IDEX_KEY,0);
		mode = getIntent().getStringExtra(TodoListFragment.ITEM_MODE_KEY);
		item = list.getItems().get(index);
		itemName.setText(item.getName());
		itemDate.setText(item.getDate());
		itemTime.setText(item.getTime());
		itemNote.setText(item.getNote());
		if(!item.getDate().equals("..."))
			clear.setVisibility(View.VISIBLE);
		itemDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePicker();
			}
		});

		itemTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePicker();
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mode.equals("adding")){
					list.getItems().remove(index);
					list.setOutstandingItem();
				}
				Intent intent = new Intent();
				intent.putExtra(TodoListFragment.ITEM_NAME_KEY, list);
				intent.putExtra(TodoListFragment.ITEM_LIST_IDEX_KEY, listIndex);
				intent.putExtra(TodoListFragment.ITEM_HISTORY_KEY, history);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(itemName.getText().toString().equals(""))
					Toast.makeText(EditTodoListItemActivity.this, "Item's name cannot be empty", Toast.LENGTH_SHORT).show();
				else{
					item.setName(itemName.getText().toString());
					item.setDate(itemDate.getText().toString());
					item.setTime(itemTime.getText().toString());
					item.setNote(itemNote.getText().toString());
					list.getItems().set(index, item);
					list.setOutstandingItem();
					history.getItemTodoHistoryList().add(itemName.getText().toString());
					saveList();
				}
			}
		});
		
		itemDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				list.getItems().remove(index);
				list.setOutstandingItem();
				saveList();
				
			}
		});
		
		clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				itemDate.setText("...");
				itemTime.setText("...");
				clear.setVisibility(View.GONE);
				
			}
		});
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		if(mode.equals("adding")){
			list.getItems().remove(index);
			list.setOutstandingItem();
		}
		saveList();
	}
	
	public void saveList(){
		listParser = new TodoListParser(list);
		historyParser = new HistoryParser(history);
		listParser.saveInBackground();
		historyParser.saveInBackground();
		Intent intent = new Intent();
		intent.putExtra(TodoListFragment.ITEM_NAME_KEY, list);
		intent.putExtra(TodoListFragment.ITEM_LIST_IDEX_KEY, listIndex);
		intent.putExtra(TodoListFragment.ITEM_HISTORY_KEY, history);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	
	public void showDatePicker(){
		DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				itemDate.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
				if(itemTime.getText().toString().equals("...")){
					String newString = new SimpleDateFormat("h:mm a").format(new Date(System.currentTimeMillis()));
					itemTime.setText(newString);
//					Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(originalString);
//					SimpleDateFormat formatter = new SimpleDateFormat.ofPattern("yyyy MM dd");
//					  String text = date.toString(formatter);
//					  LocalDate date = LocalDate.parse(text, formatter);
				}
				clear.setVisibility(View.VISIBLE);
			}
		};
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				onDateSetListener, Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH), Calendar
						.getInstance().get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
	}
	
	public void showTimePicker(){
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				if (hourOfDay >= 12) {
					if (hourOfDay == 12)
						itemTime.setText(hourOfDay + ":" + minute + " PM");
					else
						itemTime.setText((hourOfDay - 12) + ":" + minute + " PM");
				} else {
					if (hourOfDay == 0)
						itemTime.setText((hourOfDay + 12) + ":" + minute + " AM");
					else
						if(hourOfDay<10)
							itemTime.setText("0" + hourOfDay + ":" + minute + " AM");
						else
							itemTime.setText(hourOfDay + ":" + minute + " AM");
				}
				if(itemDate.getText().toString().equals("...")){
					String newString = new SimpleDateFormat("mm/dd/yyyy").format(new Date(System.currentTimeMillis()));
					itemDate.setText(newString);
				}
				clear.setVisibility(View.VISIBLE);
			}
		};
		TimePickerDialog timePickerDialog = new TimePickerDialog(this,
				onTimeSetListener, Calendar.getInstance().get(
						Calendar.HOUR_OF_DAY), Calendar.getInstance().get(
						Calendar.MINUTE), false);
		timePickerDialog.show();
	}
	
	
	
	
}
