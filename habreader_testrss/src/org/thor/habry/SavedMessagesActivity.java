package org.thor.habry;

import org.thor.habry.dao.HabrySQLDAOHelper;
import org.thor.habry.tasks.LoadSavedMessagesAsyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.ViewGroup;

public class SavedMessagesActivity extends Activity {
	
	private Handler updateListOfSavedMessagesHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_messages);	
		updateListOfSavedMessagesHandler = new UpdateListOfSavedMessagesHandler(this);
		loadSavedMessages();
	}

	private void loadSavedMessages() {
		HabrySQLDAOHelper daoHelper = AppRuntimeContext.getInstance().getDaoHelper();
		ViewGroup mainView = (ViewGroup) findViewById(R.id.saved_messages_main_layout); 
		LoadSavedMessagesAsyncTask loadMessagesTask = new LoadSavedMessagesAsyncTask(mainView, this);
		loadMessagesTask.execute(daoHelper);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_messages, menu);
		return true;
	}
	
	@SuppressLint("HandlerLeak")
	static class UpdateListOfSavedMessagesHandler extends Handler {
		private SavedMessagesActivity activity;
		
		UpdateListOfSavedMessagesHandler (SavedMessagesActivity activity) {
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
