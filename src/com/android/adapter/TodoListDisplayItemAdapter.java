package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import edu.unccUtilistProject.R;
import com.android.model.TodoListItem;

public class TodoListDisplayItemAdapter extends ArrayAdapter<TodoListItem> {
	private List<TodoListItem> mData;
	private Context mContext;
	private int mResource;
	private ViewHolder viewHolder;
	private ICreateCallToTodoListItemClick mListener;

	public TodoListDisplayItemAdapter(Context context, int resource,
			List<TodoListItem> objects, ICreateCallToTodoListItemClick mListener) {
		super(context, resource, objects);
		this.mData = objects;
		this.mContext = context;
		this.mResource = resource;
		this.mListener = mListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(mResource, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.todo_list_textViewDisplayTitle);
			viewHolder.details = (TextView) convertView
					.findViewById(R.id.todo_list_textViewDisplayPrice);
			viewHolder.checked = (CheckBox) convertView
					.findViewById(R.id.todo_list_checkBox_checked);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		TodoListItem item = mData.get(position);
		viewHolder.title.setText(item.getName());
		if (item.isChecked()) {
			viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.checked.setChecked(true);
		} else {
			viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags()
					& (~Paint.STRIKE_THRU_TEXT_FLAG));
			viewHolder.checked.setChecked(false);
		}
		viewHolder.checked.setTag(position);
		viewHolder.checked.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mListener.todoItemCheckBoxOnClick(v);

			}
		});
		if (mListener == null) {
			viewHolder.checked.setClickable(false);
		}
		viewHolder.details.setText(item.getItemDetail());

		return convertView;
	}

	public static class ViewHolder {
		TextView title;
		TextView details;
		CheckBox checked;

	}

	public interface ICreateCallToTodoListItemClick {
		public void todoItemCheckBoxOnClick(View v);
		// public void deleteOnClick(View v);
	}
}