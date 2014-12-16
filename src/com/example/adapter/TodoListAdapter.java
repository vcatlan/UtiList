package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.utilist.R;
import com.example.model.TodoList;

public class TodoListAdapter extends ArrayAdapter<TodoList>{
	private List<TodoList> mData;
	private Context mContext;
	private int mResource;
	
	public TodoListAdapter(Context context, int resource, List<TodoList> objects) {
		super(context, resource, objects);
		mData = objects;
		mContext = context;
		mResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TodoListHolder listHolder;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(mResource, parent, false);
			listHolder = new TodoListHolder();
			listHolder.textViewTitle = (TextView) convertView.findViewById(R.id.todo_list_textView_title);
			listHolder.textViewOutstanding = (TextView) convertView.findViewById(R.id.todo_list_textView_outstanding);
			convertView.setTag(listHolder);
		}
		else{
			listHolder = (TodoListHolder) convertView.getTag();
		}
		TodoList list = mData.get(position);
		listHolder.textViewTitle.setText(list.getTitle());
		listHolder.textViewOutstanding.setText(list.getOutstandingItem());
		return convertView;
	}
	
	private class TodoListHolder{
		TextView textViewTitle;
		TextView textViewOutstanding;
		
	}

}
