package org.thor.habry.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.thor.habry.AppRuntimeContext;
import org.thor.habry.dto.Message;
import org.thor.habry.feedparser.FeedXmlParser;
import org.thor.habry.feedprovider.ContentProvider;
import org.thor.habry.uimanagement.UIMediator;
import org.thor.habry.uimanagement.UIMediator.MessageListConfigJB;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

public class GetFeedersAsyncTask extends AsyncTask<ContentProvider, Integer, List<Message>> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	
	public GetFeedersAsyncTask(ViewGroup mainLayout, Activity activity) {
		this.mainLayout = mainLayout;
		this.activity = activity;
	}

	@Override
	protected List<Message> doInBackground(ContentProvider ... providers) {		
		
		ContentProvider contentProvider = providers[0];
		 
		FeedXmlParser parser = new FeedXmlParser();
		List<Message> listOfHabrMsg = new ArrayList<Message>();	
		try {		
			listOfHabrMsg = parser.parse(contentProvider);
		} catch (XmlPullParserException e) {
			Log.e("habreader", e.toString());	
			error = e;
		} catch (IOException e) {
			Log.e("habreader", e.toString());
			error = e;
		}		
		return listOfHabrMsg;
	}

	@Override
	protected void onPostExecute(List<Message> result) {
		if (error != null) {
			Toast myToast = Toast.makeText(mainLayout.getContext(), "" + error.getMessage(),
					Toast.LENGTH_LONG);			
			myToast.show();
			error = null;
		}
		
		AppRuntimeContext.getInstance().addFeedList(result);
		
		UIMediator uiMediator = new UIMediator();
		MessageListConfigJB listConfig = uiMediator.new MessageListConfigJB();	
		listConfig.setFavorFilteringEnabled(true);
		listConfig.setReadHighlightEnabled(true);
		listConfig.setSaveMessageEnabled(true);
		listConfig.setSupportDelete(false);
		uiMediator.showFeedList(result, mainLayout, activity, listConfig);		
	}

}
