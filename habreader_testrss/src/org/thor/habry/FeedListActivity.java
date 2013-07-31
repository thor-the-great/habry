package org.thor.habry;

import org.thor.habry.AppRuntimeContext.AppMode;
import org.thor.habry.HabreaderActivity.MainFeedsSectionFragment;
import org.thor.habry.dummy.DummyContent;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * An activity representing a list of Feeds. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link FeedDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link FeedListFragment} and the item details (if present) is a
 * {@link FeedDetailFragment}.
 * <p>
 * This activity also implements the required {@link FeedListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class FeedListActivity extends FragmentActivity implements
		FeedListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	//private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_list);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		AppRuntimeContext appContext = AppRuntimeContext.getInstance();
		appContext.initDAOHelper(getBaseContext());

		if (findViewById(R.id.feed_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			//mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((FeedListFragment) getSupportFragmentManager().findFragmentById(
					R.id.feed_list)).setActivateOnItemClick(true);
			appContext.setAppMode(AppMode.TWO_SIDES);
		} 
		else {
			appContext.setAppMode(AppMode.ONE_SIDE);
		}

		// TODO: If exposing deep links into your app, handle intents here.
		FeedListFragment feedListFragment = (FeedListFragment) getSupportFragmentManager().findFragmentById(R.id.feed_list);
		ListView listView = feedListFragment.getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		int index = 0;
		listView.setItemChecked(index, true);
		
		onItemSelected(DummyContent.getInstance(getBaseContext()).getITEMS().get(0).id);		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link FeedListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (AppMode.TWO_SIDES.equals(AppRuntimeContext.getInstance().getAppMode())) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(MainFeedsSectionFragment.ARG_ITEM_ID, id);
			//MainFeedsSectionFragment fragment = new MainFeedsSectionFragment();
			Fragment fragment = null;
			
			if ("3".equals(id)) {
				fragment = new SavedMessagesActivity.SavedMessagesFragment();				
			} else {
				fragment = new MainFeedsSectionFragment();					
			}
			
			fragment.setArguments(arguments);
			//getSupportFragmentManager().beginTransaction()
			//		.replace(R.id.feed_detail_container, fragment).commit();
			getFragmentManager().beginTransaction().replace(R.id.feed_detail_container, fragment).commit();			
			
			//SavedMessagesActivity saveActivity = new SavedMessagesActivity();
			//getFragmentManager().beginTransaction().replace(R.id.feed_detail_container, saveActivity).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			//Intent detailIntent = new Intent(this, FeedDetailActivity.class);
			Intent detailIntent = null;
			if ("3".equals(id)) {
				detailIntent = new Intent(this, SavedMessagesActivity.class);				
			} else {
				detailIntent = new Intent(this, HabreaderActivity.class);				
			}
			detailIntent.putExtra(MainFeedsSectionFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
