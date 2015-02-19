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
import com.android.model.ShoppingListItem;

public class ShoppingListDisplayItemAdapter extends
		ArrayAdapter<ShoppingListItem> {
	private List<ShoppingListItem> mData;
	private Context mContext;
	private int mResource;
	private ViewHolder viewHolder;
	private ICreateCallToShoppingListItemClick mListener;

	public ShoppingListDisplayItemAdapter(Context context, int resource,
			List<ShoppingListItem> objects,
			ICreateCallToShoppingListItemClick mListener) {
		super(context, resource, objects);
		mData = objects;
		mContext = context;
		mResource = resource;
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
					.findViewById(R.id.textViewDisplayTitle);
			viewHolder.details = (TextView) convertView
					.findViewById(R.id.textViewDisplayPrice);
			viewHolder.checked = (CheckBox) convertView
					.findViewById(R.id.checkBox_checked);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ShoppingListItem item = mData.get(position);
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
				mListener.itemCheckBoxOnClick(v);

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

	public interface ICreateCallToShoppingListItemClick {
		public void itemCheckBoxOnClick(View v);
		// public void deleteOnClick(View v);
	}

}
