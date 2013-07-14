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

import com.example.habreader_testrss.R;
import com.example.habreader_testrss.dto.Message;
import com.example.habreader_testrss.feedparser_stdandroid.HabrXmlParser;
import com.example.habreader_testrss.feedprovider.ContentProvider;
import com.example.habreader_testrss.uimanagement.UIMediator;

public class GetFeedersAsyncTask extends AsyncTask<String, Integer, List<Message>> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	
	public GetFeedersAsyncTask(ViewGroup mainLayout, Activity activity) {
		this.mainLayout = mainLayout;
		this.activity = activity;
	}

	@Override
	protected List<Message> doInBackground(String ... urls) {		
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean isUseTestContentProvider = sharedPref.getBoolean("setting_isUseTestContentProvider", false);		
		
		ContentProvider contentProvider;
		if (isUseTestContentProvider)
			contentProvider = ContentProvider.getInstance(ContentProvider.TEST_FILE_CONTENT_PROVIDER, activity.getResources().getXml(R.xml.testfeeds));
		else 	
			contentProvider = ContentProvider.getInstance(ContentProvider.NETWORK_CONTENT_PROVIDER, null);
		 
		HabrXmlParser parser = new HabrXmlParser();
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
		
		UIMediator uiMediator = new UIMediator();
		uiMediator.showFeedList(result, mainLayout, activity);		
	}

}
