package org.thor.habry.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thor.habry.R;

import android.content.Context;
import android.content.res.Resources;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent { 
	
	private static DummyContent instance = null;
	private Context context = null;

	/**
	 * An array of sample (dummy) items.
	 */
	List<DummyItem> ITEMS = new ArrayList<DummyItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

	static {
		// Add 3 sample items.
		
	}

	public void addItem(DummyItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}
	
	private DummyContent(Context contextParam) {
		context = contextParam;
		addItem(new DummyItem("1", context.getResources().getString(R.string.main_menu_item_habrs)));
		addItem(new DummyItem("2", context.getResources().getString(R.string.main_menu_item_qa)));
		addItem(new DummyItem("3", context.getResources().getString(R.string.main_menu_item_saved)));
	}
	
	public static DummyContent getInstance(Context context) {
		if (instance == null) {
			instance = new DummyContent(context);
		}
		return instance;
	}
	

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyItem {
		public String id;
		public String content;

		public DummyItem(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}


	public List<DummyItem> getITEMS() {
		return ITEMS;
	}

	public Map<String, DummyItem> getITEM_MAP() {
		return ITEM_MAP;
	}
}
