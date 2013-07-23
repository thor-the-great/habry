package com.example.habreader_testrss;

import java.util.Locale;

import com.example.habreader_testrss.dummy.DummyContent;
import com.example.habreader_testrss.feedprovider.ContentProvider;
import com.example.habreader_testrss.settings.SettingsActivity;
import com.example.habreader_testrss.tasks.GetFeedersAsyncTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HabreaderActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	//static String defaultHabrRssURL = "http://habrahabr.ru/rss/hubs/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_habreader);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mSectionsPagerAdapter.setMenuSelection(getIntent().getStringExtra(FeedDetailFragment.ARG_ITEM_ID));
		//savedInstanceState = new Bundle();
		//savedInstanceState.putString(FeedDetailFragment.ARG_ITEM_ID, );
		
				
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.habreader, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            //newGame();
	        	Intent myIntent = new Intent(this, SettingsActivity.class);
				startActivity(myIntent);
	            return true;	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		String mainMenuSelection = null;
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		void setMenuSelection(String mainMenuSelection) {
			this.mainMenuSelection = mainMenuSelection;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			if ( position == 0 ) {				
				fragment = new MainFeedsSectionFragment();
				//Bundle arguments = new Bundle();				
				//fragment.setArguments(arguments);
			} else {
				fragment = new DummySectionFragment();
			}
			Bundle args = new Bundle();
			args.putInt(MainFeedsSectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putString(FeedDetailFragment.ARG_ITEM_ID, mainMenuSelection);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class MainFeedsSectionFragment extends Fragment implements OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		ViewGroup mainFragmentLayout;
		
		ContentProvider contentProvider = null;

		public MainFeedsSectionFragment() {
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getArguments().containsKey(FeedDetailFragment.ARG_ITEM_ID)) {
				
				contentProvider = getContentProvider(getArguments().getString(FeedDetailFragment.ARG_ITEM_ID));
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_habreader_dummy,
					container, false);
			//TextView dummyTextView = (TextView) rootView
			//		.findViewById(R.id.section_label);
			//dummyTextView.setText(Integer.toString(getArguments().getInt(
			//		ARG_SECTION_NUMBER)));
			//ViewGroup mainFragmentLayout = (ViewGroup) rootView.findViewById(R.id.fragmentMainLayout);
			//new GetFeedersAsyncTask(mainFragmentLayout).execute(defaultHabrRssURL);
			this.mainFragmentLayout = (ViewGroup) rootView.findViewById(R.id.fragmentFeedLayout);;
						
			int childCount = mainFragmentLayout.getChildCount();
			for (int i = childCount - 1; i >=1; i--) {
				//View nextChildView = mainFragmentLayout.getChildAt(i);
				mainFragmentLayout.removeViewAt(i);
			}
			
			Toast myToast = Toast.makeText(rootView.getContext(), "Loading feeds",
					Toast.LENGTH_SHORT);			
			myToast.show();
			
			if (contentProvider == null)
				contentProvider = getContentProvider(null);			
			new GetFeedersAsyncTask(mainFragmentLayout, super.getActivity()).execute(contentProvider);	
			
			return rootView;
		}

		@Override
		public void onClick(View v) {
			int childCount = mainFragmentLayout.getChildCount();
			for (int i = childCount - 1; i >=1; i--) {
				//View nextChildView = mainFragmentLayout.getChildAt(i);
				mainFragmentLayout.removeViewAt(i);
			}
			
			Toast myToast = Toast.makeText(v.getContext(), "Loading feeds",
					Toast.LENGTH_SHORT);			
			myToast.show();
			
			if (contentProvider == null)
				contentProvider = getContentProvider(null);				
			//ViewGroup mainFragmentLayout = (ViewGroup) v.findViewById(R.id.fragmentMainLayout);
			new GetFeedersAsyncTask(mainFragmentLayout, super.getActivity()).execute(contentProvider);			
		}
		
		private ContentProvider getContentProvider(String index) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(super.getActivity());
			boolean isUseTestContentProvider = sharedPref.getBoolean("setting_isUseTestContentProvider", false);		
			
			ContentProvider contentProvider;
			if (isUseTestContentProvider || index == null || "".equalsIgnoreCase(index))
				contentProvider = ContentProvider.getInstance(ContentProvider.TEST_FILE_CONTENT_PROVIDER, super.getActivity().getResources().getXml(R.xml.testfeeds));
			else if (1 == Integer.parseInt(index))	
				contentProvider = ContentProvider.getInstance(ContentProvider.BEST_HUBS_CONTENT_PROVIDER, null);
			else if (2 == Integer.parseInt(index))
				contentProvider = ContentProvider.getInstance(ContentProvider.QA_CONTENT_PROVIDER, null);
			else
				contentProvider = ContentProvider.getInstance(ContentProvider.TEST_FILE_CONTENT_PROVIDER, super.getActivity().getResources().getXml(R.xml.testfeeds));
			return contentProvider;
		}
	}
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_habreader_dummy,	container, false);			
			return rootView;
		}
	}

}
