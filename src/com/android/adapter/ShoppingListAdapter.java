package com.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.unccUtilistProject.R;
import com.android.model.ShoppingList;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {
	List<ShoppingList> mData;
	Context mContext;
	int mResource;

	public ShoppingListAdapter(Context context, int resource,
			List<ShoppingList> objects) {
		super(context, resource, objects);
		mData = objects;
		mContext = context;
		mResource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShoppingListHolder listHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(mResource, parent, false);
			listHolder = new ShoppingListHolder();
			listHolder.textViewTitle = (TextView) convertView
					.findViewById(R.id.shopping_list_textView_title);
			listHolder.textViewOutstanding = (TextView) convertView
					.findViewById(R.id.shopping_list_textView_outstanding);
			convertView.setTag(listHolder);
		} else {
			listHolder = (ShoppingListHolder) convertView.getTag();
		}
		ShoppingList list = mData.get(position);
		listHolder.textViewTitle.setText(list.getTitle());
		listHolder.textViewOutstanding.setText(list.getOutstandingItem());
		return convertView;
	}

	private class ShoppingListHolder {
		TextView textViewTitle;
		TextView textViewOutstanding;

	}

}
