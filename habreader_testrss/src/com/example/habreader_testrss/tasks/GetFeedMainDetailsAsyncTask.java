package com.example.habreader_testrss.tasks;

import java.util.HashMap;
import java.util.Map;

import nu.xom.Document;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.view.Display;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.habreader_testrss.dto.Message;
import com.example.habreader_testrss.messageparser.MessageParser;

public class GetFeedMainDetailsAsyncTask extends AsyncTask<Message, Integer, Document> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	
	public GetFeedMainDetailsAsyncTask(ViewGroup mainLayout, Activity activity) {
		this.mainLayout = mainLayout;
		this.activity = activity;
	}
	@Override
	protected void onPostExecute(Document result) {
		if (result != null) {
			WebView webview = new WebView(activity);
			activity.setContentView(webview);
			webview.loadDataWithBaseURL("", result.toXML(), "text/html", "UTF-8", null);
			
			//webview.getSettings().setLoadWithOverviewMode(true);
			//webview.getSettings().setUseWideViewPort(true);
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	protected Document doInBackground(Message... feeds) {
		Message feedMessage = feeds[0];		
		Display dispaly = activity.getWindowManager().getDefaultDisplay();
		Point sizePoint = new Point();
		dispaly.getSize(sizePoint);
		Map<String, Object> documentParams = new HashMap<String, Object>(); 
		documentParams.put("MAX_DISPLAY_WIDTH", Integer.valueOf(sizePoint.x));
		Document document = MessageParser.getInstance(documentParams).parsePostToDocument(feedMessage);		
		return document;
	}	

}
