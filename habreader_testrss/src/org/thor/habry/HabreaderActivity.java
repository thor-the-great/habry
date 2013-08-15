package org.thor.habry;

import java.util.List;

import org.thor.habry.dto.Message;
import org.thor.habry.dto.MessageType;
import org.thor.habry.feedprovider.ContentProvider;
import org.thor.habry.settings.SettingsActivity;
import org.thor.habry.tasks.GetFeedersAsyncTask;
import org.thor.habry.uimanagement.UIMediator;
import org.thor.habry.uimanagement.UIMediator.MessageListConfigJB;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class HabreaderActivity extends FragmentActivity {

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private static final int CONTENT_VIEW_ID = 10101010;
	private static final String FRAGMENT_ID = "HabreaderActivity_FRAGMENT_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
					
		FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //if (savedInstanceState == null) {
            
    		Fragment contentFragment = getFragmentManager().findFragmentByTag(FRAGMENT_ID);
    		if (contentFragment == null) {
    			MainFeedsSectionFragment newFragment = new MainFeedsSectionFragment();
                Bundle args = new Bundle();
        		//args.putInt(MainFeedsSectionFragment.ARG_SECTION_NUMBER, position + 1);
        		args.putString(MainFeedsSectionFragment.ARG_ITEM_ID, getIntent().getStringExtra(MainFeedsSectionFragment.ARG_ITEM_ID));
        		newFragment.setArguments(args);
    			FragmentTransaction ft = getFragmentManager().beginTransaction();
    			ft.add(CONTENT_VIEW_ID, newFragment, FRAGMENT_ID).commit();
    		} else {
    			if (contentFragment instanceof MainFeedsSectionFragment) {
    				((MainFeedsSectionFragment)contentFragment).readData = false;
    			}
    		}
        //}
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
	 * A fragment representing a section of the app
	 */
	public static class MainFeedsSectionFragment extends Fragment implements OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_ITEM_ID = "item_id";
		
		ViewGroup mainFragmentLayout;		
		ContentProvider contentProvider = null;
		boolean readData = false;

		public MainFeedsSectionFragment() {
			this.setRetainInstance(true);
			readData = true;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getArguments().containsKey(MainFeedsSectionFragment.ARG_ITEM_ID)) {
				
				contentProvider = getContentProvider(getArguments().getString(MainFeedsSectionFragment.ARG_ITEM_ID));
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_habreader_dummy,
					container, false);			
			this.mainFragmentLayout = (ViewGroup) rootView.findViewById(R.id.fragmentFeedLayout);;
						
			int childCount = mainFragmentLayout.getChildCount();
			for (int i = childCount - 1; i >=1; i--) {				
				mainFragmentLayout.removeViewAt(i);
			}			
			if (readData) {
				ProgressDialog pd = ProgressDialog.show(this.getActivity(), null, 
						this.getActivity().getResources().getString(R.string.status_message_loading_feed), 
						true, false, null);
				
				if (contentProvider == null)
					contentProvider = getContentProvider(null);	
				new GetFeedersAsyncTask(mainFragmentLayout, super.getActivity(), pd).execute(contentProvider);	
			} else {
				List<Message> result = AppRuntimeContext.getInstance().getFeedList();
				
				List<String> savedMessageRef = AppRuntimeContext.getInstance().getDaoHelper().findSavedMessageRefByType(MessageType.POST.name());
				
				UIMediator uiMediator = new UIMediator();
				MessageListConfigJB listConfig = uiMediator.new MessageListConfigJB();	
				listConfig.setFavorFilteringEnabled(true);
				listConfig.setReadHighlightEnabled(true);
				listConfig.setSaveMessageEnabled(true);
				listConfig.setSupportDelete(false);
				uiMediator.showFeedList(result, mainFragmentLayout, getActivity(), listConfig, savedMessageRef);		
			}
			return rootView;
		}

		@Override
		public void onClick(View v) {
			int childCount = mainFragmentLayout.getChildCount();
			for (int i = childCount - 1; i >=1; i--) {
				//View nextChildView = mainFragmentLayout.getChildAt(i);
				mainFragmentLayout.removeViewAt(i);
			}
			ProgressDialog pd = ProgressDialog.show(this.getActivity(), null, 
					this.getActivity().getResources().getString(R.string.status_message_loading_feed), 
					true, false, null);
			
			if (contentProvider == null)
				contentProvider = getContentProvider(null);				
			new GetFeedersAsyncTask(mainFragmentLayout, super.getActivity(), pd).execute(contentProvider);
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

}
