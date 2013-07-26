package org.thor.habry.tasks;

import java.util.HashMap;
import java.util.Map;

import org.thor.habry.dto.Message;
import org.thor.habry.messageparser.MessageParser;

import nu.xom.Document;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class GetPostMainDetailsAsyncTask extends AsyncTask<Message, Integer, Document> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	
	public GetPostMainDetailsAsyncTask(ViewGroup mainLayout, Activity activity) {
		this.mainLayout = mainLayout;
		this.activity = activity;
	}
	@Override
	protected void onPostExecute(Document result) {
		if (result != null) {									
			WebView webview = new WebView(activity);
			mainLayout.addView(webview);
			//activity.setContentView(webview);
			webview.getSettings().setJavaScriptEnabled(true);		
			webview.setWebViewClient(new WebViewClient() {
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					Toast.makeText(activity, "Post cannot be loaded! " + description, Toast.LENGTH_SHORT).show();
				}
			});			
			webview.loadDataWithBaseURL("", result.toXML(), "text/html", "UTF-8", null);
			webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		}
	}
	
	@Override
	protected Document doInBackground(Message... feeds) {
		Message feedMessage = feeds[0];			
		Map<String, Object> documentParams = new HashMap<String, Object>(); 
		//documentParams.put("MAX_DISPLAY_WIDTH", Integer.valueOf(sizePoint.x));
		Document document = MessageParser.getInstance(documentParams).parsePostToDocument(feedMessage);		
		return document;
	}	

}
