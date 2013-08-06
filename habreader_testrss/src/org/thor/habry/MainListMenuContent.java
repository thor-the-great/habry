package org.thor.habry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thor.habry.R;

import android.content.Context;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
public class MainListMenuContent { 
	
	private static MainListMenuContent instance = null;
	private Context context = null;

	/**
	 * An array of sample (dummy) items.
	 */
	List<MainListMenuItem> itemList = new ArrayList<MainListMenuItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	Map<String, MainListMenuItem> itemMap = new HashMap<String, MainListMenuItem>();

	public void addItem(MainListMenuItem item) {
		itemList.add(item);
		itemMap.put(item.id, item);
	}
	
	private MainListMenuContent(Context contextParam) {
		context = contextParam;
		addItem(new MainListMenuItem("1", context.getResources().getString(R.string.main_menu_item_habrs)));
		addItem(new MainListMenuItem("2", context.getResources().getString(R.string.main_menu_item_qa)));
		addItem(new MainListMenuItem("3", context.getResources().getString(R.string.main_menu_item_saved)));
	}
	
	public static MainListMenuContent getInstance(Context context) {
		if (instance == null) {
			instance = new MainListMenuContent(context);
		}
		return instance;
	}
	

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class MainListMenuItem {
		public String id;
		public String content;

		public MainListMenuItem(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}


	public List<MainListMenuItem> getItemList() {
		return itemList;
	}

	public Map<String, MainListMenuItem> getItemMap() {
		return itemMap;
	}
}
