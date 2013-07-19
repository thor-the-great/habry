package com.example.habreader_testrss.tasks;

import java.io.IOException;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.app.Activity;
import android.os.AsyncTask;
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
	
	@Override
	protected Document doInBackground(Message... feeds) {
		Message feedMessage = feeds[0];		
		Document document = MessageParser.getInstance().parsePostToDocument(feedMessage);		
		return document;
	}	

}
