package org.thor.habry.feeddetail;

import java.util.Locale;

import org.thor.habry.R;
import org.thor.habry.dto.Message;
import org.thor.habry.settings.SettingsActivity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class PostDetail extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	Message postMessage;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		postMessage = (Message) getIntent().getSerializableExtra(PostDetailSectionFragment.POST_DETAIL_MESSAGE);
		//Message postMessage = (Message) savedInstanceState.get();
		setContentView(R.layout.activity_post_detail);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), postMessage);
					
				
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager_post_detail);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		// Set up the action bar.
		//final ActionBar actionBar = getActionBar();
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//actionBar.setDisplayShowTitleEnabled(false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		//mSectionsPagerAdapter = new SectionsPagerAdapter(
		//		getSupportFragmentManager(), postMessage);

		// Set up the ViewPager with the sections adapter.
		//mViewPager = (ViewPager) findViewById(R.id.pager_post_detail);
		//mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		//mViewPager
		//		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		//			@Override
		//			public void onPageSelected(int position) {
		//				actionBar.setSelectedNavigationItem(position);
		//			}
		//		});

		// For each of the sections in the app, add a tab to the action bar.
		//for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			//actionBar.addTab(actionBar.newTab()
				//	.setText(mSectionsPagerAdapter.getPageTitle(i))
				//	.setTabListener(this));
		//}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_detail, menu);
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
		
		Message message;
		
		public SectionsPagerAdapter(FragmentManager fm, Message message) {
			super(fm);
			this.message = message;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new PostDetailSectionFragment();
				break;
			case 1:
				switch(message.getType()) {
					case POST:
						fragment = new PostCommentsSectionFragment();
						break;
					case QA:
						fragment = new PostQASectionFragment();
						break;
					default:
						break;
				}				
				break;	
			default:
				fragment = new PostDetailSectionFragment();
				break;
			}			
			Bundle args = new Bundle();
			//args.putInt(PostDetailSectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putSerializable(PostDetailSectionFragment.POST_DETAIL_MESSAGE, this.message);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_postDetail_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_postDetail_section2).toUpperCase(l);			
			}
			return null;
		}		

	}	

}
