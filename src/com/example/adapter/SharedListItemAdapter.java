package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.utilist.R;
import com.example.model.SharedList;

public class SharedListItemAdapter extends ArrayAdapter<SharedList>{
	List<SharedList> mData;
	Context mContext;
	int mResource;
	
	public SharedListItemAdapter(Context context, int resource, List<SharedList> objects) {
		super(context, resource, objects);
		mData = objects;
		mContext = context;
		mResource = resource;
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(mResource, parent, false);
			holder = new ViewHolder();
			holder.textViewListTitle = (TextView) convertView.findViewById(R.id.sharedlist_item_textView_title);
			holder.textViewSharedByName = (TextView) convertView.findViewById(R.id.sharedlist_item_textView_sheard_by);
			holder.textViewNew = (TextView) convertView.findViewById(R.id.sharedlist_item_textView_new);
			holder.textViewListType = (TextView) convertView.findViewById(R.id.sharedlist_item_textView_type);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textViewListTitle.setText(mData.get(position).getListTitle());
		holder.textViewSharedByName.setText("Shared by: " + mData.get(position).getSharedByName());
		if(mData.get(position).isNewList())
			holder.textViewNew.setText("New");
		else{
			holder.textViewNew.setText("");
		}
		holder.textViewListType.setText("List Type: " + mData.get(position).getListType());
		return convertView;
		
	}
	
	public static class ViewHolder{
		TextView textViewListTitle;
		TextView textViewSharedByName;
		TextView textViewNew;
		TextView textViewListType;
	}

}
