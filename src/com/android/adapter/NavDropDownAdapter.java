package com.android.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.unccUtilistProject.R;
import com.android.model.NavDropDownHeader;
import com.android.model.NavDropDownItem;
import com.android.model.NavDropDownMenuItem;

public class NavDropDownAdapter extends ArrayAdapter<NavDropDownItem> {

	private LayoutInflater inflater;

	public NavDropDownAdapter(Context context, int ResourceId,
			ArrayList<NavDropDownItem> objects) {
		super(context, ResourceId, objects);
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	private View getCustomView(int position, View convertView, ViewGroup parent) {
		View view = null;
		NavDropDownItem menuItem = this.getItem(position);
		if (menuItem.getType() == NavDropDownMenuItem.ITEM_TYPE) {
			view = getItemView(convertView, parent, menuItem);
		} else {
			view = getHeaderView(convertView, parent, menuItem);
		}
		return view;
	}

	public View getItemView(View convertView, ViewGroup parentView,
			NavDropDownItem navDropDownItem) {

		NavDropDownMenuItem navDropDownMenuItem = (NavDropDownMenuItem) navDropDownItem;
		// NavMenuItemHolder navMenuItemHolder = null;
		//
		// if (convertView == null) {
		convertView = inflater.inflate(R.layout.navdropdown_item, null, false);
		TextView labelView = (TextView) convertView
				.findViewById(R.id.navmenuitem_label);

		// navMenuItemHolder = new NavMenuItemHolder();
		// navMenuItemHolder.labelView = labelView ;
		// navMenuItemHolder.iconView = iconView ;

		// convertView.setTag(navMenuItemHolder);
		// }

		// if ( navMenuItemHolder == null ) {
		// navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
		// }
		//
		// navMenuItemHolder.labelView.setText(menuItem.getLabel());
		// navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());

		labelView.setText(navDropDownMenuItem.getLabel());

		return convertView;
	}

	@SuppressLint("ResourceAsColor")
	public View getHeaderView(View convertView, ViewGroup parentView,
			NavDropDownItem navDropDownItem) {

		NavDropDownHeader navDropDownHeader = (NavDropDownHeader) navDropDownItem;
		// NavMenuSectionHolder navMenuItemHolder = null;

		// if (convertView == null) {
		convertView = inflater
				.inflate(R.layout.navdropdown_header, null, false);
		TextView labelView = (TextView) convertView
				.findViewById(R.id.navmenusection_label);

		// navMenuItemHolder = new NavMenuSectionHolder();
		// navMenuItemHolder.labelView = labelView ;
		// convertView.setTag(navMenuItemHolder);
		// }

		// if ( navMenuItemHolder == null ) {
		// navMenuItemHolder = (NavMenuSectionHolder) convertView.getTag();
		// }
		//
		// navMenuItemHolder.labelView.setText(menuSection.getLabel());
		labelView.setText(navDropDownHeader.getLabel());

		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return this.getItem(position).getType();
	}

	@Override
	public boolean isEnabled(int position) {
		return getItem(position).isEnabled();
	}

	private static class NavMenuItemHolder {
		private TextView labelView;
	}

	private class NavMenuSectionHolder {
		private TextView labelView;
	}
}
