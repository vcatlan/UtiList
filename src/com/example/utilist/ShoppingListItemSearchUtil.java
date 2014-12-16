package com.example.utilist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.model.ShoppingListItem;

public class ShoppingListItemSearchUtil {
	static public class ShoppingListItemJSONParser{

		static public ShoppingListItem parseItem(String in) throws JSONException{
			ShoppingListItem item = null;
			//Log.d("demo", in);
			JSONObject root = new JSONObject(in);
			if(root.getString("valid").equals("true")){
				if(root.getString("itemname").equals("")){
					item = new ShoppingListItem(root.getString("description"));
				}
				else{
					item = new ShoppingListItem(root.getString("itemname"));
				}
				if(!root.getString("avg_price").equals(""))
					item.setUnitPrice(Double.parseDouble(root.getString("avg_price")));
			}
			return item;
		}
		static public ShoppingListItem parseItemWalmart(String in) throws JSONException{
			ShoppingListItem item = null;
			//Log.d("demo", in);
			JSONObject root = new JSONObject(in);
			//Log.d("demo", root.getInt("totalResults")+"");
			if(root.getInt("totalResults") != 0){
				JSONArray items = root.getJSONArray("items");
				JSONObject itemObject = items.getJSONObject(0);
				item = new ShoppingListItem(itemObject.getString("name"));
				item.setUnitPrice(itemObject.getDouble("salePrice"));
			}
			return item;
		}
	}
}
