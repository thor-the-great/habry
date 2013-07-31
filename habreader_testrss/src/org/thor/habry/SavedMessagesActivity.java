package org.thor.habry;

import org.thor.habry.HabreaderActivity.MainFeedsSectionFragment;
import org.thor.habry.dao.HabrySQLDAOHelper;
import org.thor.habry.tasks.LoadSavedMessagesAsyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class SavedMessagesActivity extends Activity {
	
	public static final int CONTENT_VIEW_ID = 10101020;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            android.app.Fragment newFragment = new SavedMessagesFragment();
            Bundle args = new Bundle();
    		//args.putInt(MainFeedsSectionFragment.ARG_SECTION_NUMBER, position + 1);
    		args.putString(MainFeedsSectionFragment.ARG_ITEM_ID, getIntent().getStringExtra(MainFeedsSectionFragment.ARG_ITEM_ID));
    		newFragment.setArguments(args);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(CONTENT_VIEW_ID, newFragment).commit();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_messages, menu);
		return true;
	}
	
	/**
	 * A fragment representing a section of the app
	 */
	public static class SavedMessagesFragment extends Fragment 
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_ITEM_ID = "item_id";
		private Handler updateListOfSavedMessagesHandler = null;
		
		ViewGroup mainFragmentLayout;
		View rootView;
		
		public SavedMessagesFragment() {
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			this.rootView = inflater.inflate(R.layout.activity_saved_messages,
					container, false);
			//setContentView(R.layout.activity_saved_messages);	
			updateListOfSavedMessagesHandler = new UpdateListOfSavedMessagesHandler(this);
			loadSavedMessages();
			return rootView;
		}
		
		private void loadSavedMessages() {
			HabrySQLDAOHelper daoHelper = AppRuntimeContext.getInstance().getDaoHelper();
			ViewGroup mainView = (ViewGroup) rootView.findViewById(R.id.saved_messages_main_layout); 
			LoadSavedMessagesAsyncTask loadMessagesTask = new LoadSavedMessagesAsyncTask(mainView, this.getActivity(), this);
			loadMessagesTask.execute(daoHelper);
		}
		
		@SuppressLint("HandlerLeak")
		static class UpdateListOfSavedMessagesHandler extends Handler {
			private SavedMessagesFragment activity;
			
			UpdateListOfSavedMessagesHandler (SavedMessagesFragment activity) {
				super();
				this.activity = activity;
			}
			@Override
			public void handleMessage(Message msg) {	
				activity.loadSavedMessages();
			}
		}

		public Handler getUpdateListOfSavedMessagesHandler() {
			if(updateListOfSavedMessagesHandler == null)
				updateListOfSavedMessagesHandler = new UpdateListOfSavedMessagesHandler(this);
			return updateListOfSavedMessagesHandler;
		};
	}
}
