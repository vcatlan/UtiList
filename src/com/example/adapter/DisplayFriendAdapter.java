package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.FriendListItem;
import com.example.utilist.R;

public class DisplayFriendAdapter extends ArrayAdapter<FriendListItem>{
	List<FriendListItem> mData;
	Context mContext;
	int mResource;
	ICreateCallToFriendListItemClick mListener;
	
	public DisplayFriendAdapter(Context context, int resource, List<FriendListItem> objects, ICreateCallToFriendListItemClick mListener) {
		super(context, resource, objects);
		mData = objects;
		mContext = context;
		mResource = resource;
		this.mListener = mListener;
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(mResource, parent, false);
			holder = new ViewHolder();
			holder.textViewName = (TextView) convertView.findViewById(R.id.textViewFriendName);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxToShare);
			holder.imageViewDelete = (ImageView) convertView.findViewById(R.id.imageViewDeleteFriend);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textViewName.setText(mData.get(position).getFirstLastName());
		holder.checkBox.setTag(position);
		holder.checkBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.checkBokOnClick(v);
				
			}
		});
		holder.imageViewDelete.setTag(position);
		holder.imageViewDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.deleteOnClick(v);	
			}
		});
		return convertView;
		
	}
	
	public static class ViewHolder{
		TextView textViewName;
		CheckBox checkBox;
		ImageView imageViewDelete;
	}
	
	public interface ICreateCallToFriendListItemClick{
		public void checkBokOnClick(View v);
		public void deleteOnClick(View v);
		
	}
}
