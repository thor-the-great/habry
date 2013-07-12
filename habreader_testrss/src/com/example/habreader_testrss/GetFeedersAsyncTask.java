package com.example.habreader_testrss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habreader_testrss.dto.Message;
import com.example.habreader_testrss.feedparser_stdandroid.HabrXmlParser;
import com.example.habreader_testrss.feedprovider.ContentProvider;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class GetFeedersAsyncTask extends AsyncTask<String, Integer, List<Message>> {
	
	int MAX_TITLE_LENGTH = 50;
	int MAX_DESC_LENGTH = 150;
	
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
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean isShowPartOfFullFeed = sharedPref.getBoolean("setting_isShowPartOfFullFeed", false);
		//int tableMaxWidth = tableLayout.getWidth();
		for (int i = 0; i < result.size(); i++) {
			Message message = result.get(i);
			
			ScrollView scrollView = new ScrollView(mainLayout.getContext());
			//LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			//scrollView.setLayoutParams(layoutParams);
			LinearLayout feedElementContainer = new LinearLayout(scrollView.getContext());
			feedElementContainer.setOrientation(LinearLayout.VERTICAL);
			
			TextView feedTitleTextview = new TextView(feedElementContainer.getContext());
			LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			feedTitleTextview.setLayoutParams(layoutParams);			
			int actualTitleLength = message.getTitle() == null ? 0 : message.getTitle().length();
			String feedTitle = "";
			if (actualTitleLength > MAX_TITLE_LENGTH ) {
				feedTitle = message.getTitle().substring(0, MAX_TITLE_LENGTH) + "...";
			} else {
				feedTitle = message.getTitle();
			}
			feedTitleTextview.setText(feedTitle);
			feedTitleTextview.setTextColor(Color.DKGRAY);
			feedTitleTextview.setTextSize((float) 16.0);
			feedTitleTextview.setTypeface(Typeface.DEFAULT_BOLD);
			feedElementContainer.addView(feedTitleTextview);
			
			feedTitleTextview.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					
					((View)v.getParent()).setBackgroundResource(R.drawable.borders);
					return false;
				}
			});
			
			if (isShowPartOfFullFeed) {				
				TextView feedDescriptionTextview = new TextView(feedElementContainer.getContext());			
				feedDescriptionTextview.setLayoutParams(layoutParams);			
				actualTitleLength = message.getDescription() == null ? 0 : message.getDescription().length();
				String feedDescription = "";
				if (actualTitleLength > MAX_DESC_LENGTH ) {
					feedDescription = message.getDescription().substring(0, MAX_DESC_LENGTH) + "...";
				} else {
					feedDescription = message.getDescription();
				}
				feedDescriptionTextview.setText(feedDescription);
				feedDescriptionTextview.setTextColor(Color.DKGRAY);
				feedDescriptionTextview.setTextSize((float) 12.0);
				feedElementContainer.addView(feedDescriptionTextview);
			}
			
			TextView authorInfo = new TextView(mainLayout.getContext());			
			authorInfo.setLayoutParams(layoutParams);			
			
			authorInfo.setText(message.getAuthor());
			authorInfo.setTextColor(Color.GREEN);
			authorInfo.setGravity(Gravity.RIGHT);
			authorInfo.setTextSize((float) 10.0);
			//textview.setPadding(10, 1, 10, 0);			
			//scrollView.addView(authorInfo);
			feedElementContainer.addView(authorInfo);
			
			LinearLayout.LayoutParams scrollViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			scrollViewLayoutParams.setMargins(10, 1, 10, 20);
			scrollView.addView(feedElementContainer);		
			
			mainLayout.addView(scrollView, scrollViewLayoutParams);
			//mainLayout.addView(authorInfo);
			//mainLayout.addView(scrollView);			

		}
		
	}	
	

}
