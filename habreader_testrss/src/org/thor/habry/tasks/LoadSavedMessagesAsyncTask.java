package org.thor.habry.tasks;

import java.util.List;

import org.thor.habry.R;
import org.thor.habry.SavedMessagesActivity.SavedMessagesFragment;
import org.thor.habry.dao.HabrySQLDAOHelper;
import org.thor.habry.dto.Message;
import org.thor.habry.uimanagement.UIMediator;
import org.thor.habry.uimanagement.UIMediator.MessageListConfigJB;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.Toast;

public class LoadSavedMessagesAsyncTask extends AsyncTask<HabrySQLDAOHelper, Integer, List<Message>> {
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	SavedMessagesFragment messageFragment;
	
	public LoadSavedMessagesAsyncTask(ViewGroup mainLayout, Activity activity, SavedMessagesFragment messageFragment) {
		this.mainLayout = mainLayout;
		this.activity = activity;
		this.messageFragment = messageFragment;
	}

	@Override
	protected List<Message> doInBackground(HabrySQLDAOHelper... arg0) {
		HabrySQLDAOHelper daoHelper = arg0[0];
		List<Message> messageList = daoHelper.findAllMessages();
		return messageList;
	}
	
	@Override
	protected void onPostExecute(List<Message> result) {
		if(result == null || result.size() == 0) {
			Toast.makeText(activity, activity.getResources().getString(R.string.status_message_no_saved_messages), Toast.LENGTH_SHORT).show();			
		}		
		UIMediator uiMediator = new UIMediator();
		MessageListConfigJB listConfig = uiMediator.new MessageListConfigJB();	
		listConfig.setFavorFilteringEnabled(false);
		listConfig.setReadHighlightEnabled(false);
		listConfig.setSaveMessageEnabled(false);
		listConfig.setSupportDelete(true);
		listConfig.setMessageHandler(messageFragment.getUpdateListOfSavedMessagesHandler());
		uiMediator.showFeedList(result, mainLayout, activity, listConfig);		
	}
}
