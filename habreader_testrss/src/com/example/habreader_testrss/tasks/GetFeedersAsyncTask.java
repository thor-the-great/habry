package com.example.habreader_testrss.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.habreader_testrss.AppRuntimeContext;
import com.example.habreader_testrss.R;
import com.example.habreader_testrss.dto.Message;
import com.example.habreader_testrss.feedparser.FeedXmlParser;
import com.example.habreader_testrss.feedprovider.ContentProvider;
import com.example.habreader_testrss.uimanagement.UIMediator;

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
		uiMediator.showFeedList(result, mainLayout, activity);		
	}

}
