package com.example.habreader_testrss;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.example.habreader_testrss.feedparser_stdandroid.HabrXmlParser;
import com.example.habreader_testrss.feedprovider.BaseFeedParser;
import com.example.habreader_testrss.feedprovider.ContentProvider;
import com.example.habreader_testrss.feedprovider.SaxFeedParser;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GetFeedersAsyncTask extends AsyncTask<String, Integer, List<Message>> {
	
	int MAX_TITLE_LENGTH = 400;
	
	ViewGroup mainLayout;
	Activity activity;
	
	public GetFeedersAsyncTask(ViewGroup mainLayout, Activity activity) {
		this.mainLayout = mainLayout;
		this.activity = activity;
	}
	
	@Override
	protected List<Message> doInBackground(String ... urls) {
		XmlResourceParser xmlParser = activity.getResources().getXml(R.xml.testfeeds);
		HabrXmlParser parser = new HabrXmlParser();
		List<Message> listOfHabrMsg = new ArrayList<Message>();
		try {
			//listOfHabrMsg = parser.parse(xmlParser, null);
			String url = "http://habrahabr.ru/rss/hubs/";
			try {			
					URL feedUrl = new URL(url);
					listOfHabrMsg = parser.parse(null, feedUrl.openConnection().getInputStream());
		        } catch (MalformedURLException e) {
		        	Log.e("habreader error", e.toString());
		            throw new RuntimeException(e);
		        } catch (IOException e) {
		        	Log.e("habreader error", e.toString());
		        	throw new RuntimeException(e);
				}
	
			
		} catch (XmlPullParserException e) {
			Log.e("habreader", e.toString());
		} /*catch (IOException e) {
			Log.e("habreader", e.toString());
		}*/
		
		//String defaultHabrRssURL = urls[0];
		
		//ContentProvider contentProvider = ContentProvider.getInstance(ContentProvider.NETWORK_CONTENT_PROVIDER, activity.getResources().openRawResource(R.xml.testfeeds));
		//ContentProvider contentProvider = ContentProvider.getInstance(ContentProvider.NETWORK_CONTENT_PROVIDER, null);
		//BaseFeedParser feederParser = new SaxFeedParser();
		//List<Message> listOfHabrMsg = feederParser.parse(contentProvider);
		//Log.d("habreader", "numbr of parsed messages " + (listOfHabrMsg == null ? "NULL" : listOfHabrMsg.size()));
		//for (Message message : listOfHabrMsg) {
			//Log.d("habreader", "message details.");
			/*Log.d("habreader", "message.getTitle() = " + message.getTitle() );
			Log.d("habreader", "message.getDescription() = " + message.getDescription());*/
		//}
		return listOfHabrMsg;
	}

	@Override
	protected void onPostExecute(List<Message> result) {
		//int tableMaxWidth = tableLayout.getWidth();
		for (int i = 0; i < result.size(); i++) {
			Message message = result.get(i);
			
			ScrollView scrollView = new ScrollView(mainLayout.getContext());
			//LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			//scrollView.setLayoutParams(layoutParams);
			
			TextView textview = new TextView(scrollView.getContext());
			LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			textview.setLayoutParams(layoutParams);
			
			int actualTitleLength = message.getTitle() == null ? 0 : message.getTitle().length();
			String feedTitle = "";
			if (actualTitleLength > MAX_TITLE_LENGTH ) {
			//if (actualTitleLength > tableMaxWidth) {
				feedTitle = message.getTitle().substring(0, MAX_TITLE_LENGTH);
				//feedTitle = message.getTitle().substring(0, tableMaxWidth);
			} else {
				feedTitle = message.getTitle();
			}
			textview.setText(feedTitle);
			textview.setTextColor(Color.DKGRAY);
			//textview.setPadding(10, 1, 10, 0);
			
			scrollView.addView(textview);
			
			LinearLayout.LayoutParams scrollViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			scrollViewLayoutParams.setMargins(10, 1, 10, 20);
			
			mainLayout.addView(scrollView, scrollViewLayoutParams);
			//mainLayout.addView(scrollView);
			
			/*TableRow tr1 = new TableRow(tableLayout.getContext());
			
			tr1.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			/*AutoResizeTextView textview = new AutoResizeTextView(tableLayout.getContext());	
			
			int actualTitleLength = message.getTitle() == null ? 0 : message.getTitle().length();
			String feedTitle = "";
			//if (actualTitleLength > MAX_TITLE_LENGTH ) {
			//if (actualTitleLength > tableMaxWidth) {
				//feedTitle = message.getTitle().substring(0, MAX_TITLE_LENGTH);
				//feedTitle = message.getTitle().substring(0, tableMaxWidth);
			//} else {
				feedTitle = message.getTitle();
			//}
			textview.setText(feedTitle);
			//textview.getTextColors(R.color.)
			textview.setTextColor(Color.DKGRAY);	
			
			
			tr1.addView(textview);
			Button btn = new Button(tableLayout.getContext());
			btn.setText("Test test test test tes tes test test ");
			
			
			tr1.addView(btn);
			tableLayout.addView(tr1, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));*/
			//textview.resizeText();
		}
		
	}	
	

}
