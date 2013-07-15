package com.example.habreader_testrss.feedprovider;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public abstract class ContentProvider {
	
	public final static String BEST_HUBS_CONTENT_PROVIDER = "BEST_HUBS_CONTENT_PROVIDER";
	public final static String QA_CONTENT_PROVIDER = "QA_CONTENT_PROVIDER";
	public final static String TEST_FILE_CONTENT_PROVIDER = "TEST_FILE_CONTENT_PROVIDER";
	
	final static int HTTP_OPEN_CONNECTION_TIMEOUT = 10000;
	final static int HTTP_READ_STREAM_TIMEOUT = 20000;
	
	static public ContentProvider getInstance(String contentProviderStrategy, Object initObject){
		ContentProvider instance = null;
		if ( BEST_HUBS_CONTENT_PROVIDER.equals(contentProviderStrategy) ) {
			instance = new BestHubsContentProvider();
		} else if ( QA_CONTENT_PROVIDER.equals(contentProviderStrategy) ) {
			instance = new QAHubsContentProvider();
		} else if ( TEST_FILE_CONTENT_PROVIDER.equals(contentProviderStrategy) ) {
			instance = new TestFileContentProvider((XmlPullParser) initObject);
		}
		return instance;
	}
	
	public abstract XmlPullParser getContentParser() throws MalformedURLException, IOException, XmlPullParserException;
	
	public abstract void flashResources();
	
}

abstract class NetworkContentProvider extends ContentProvider {		
	InputStream is = null;

	abstract String getUrl();
	
	@Override
	public XmlPullParser getContentParser() throws MalformedURLException, IOException, XmlPullParserException {		
		XmlPullParser parser = Xml.newPullParser();
		try {		
			URL feedUrl = new URL(getUrl());		
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			URLConnection urlConnection = feedUrl.openConnection();
			urlConnection.setConnectTimeout(HTTP_OPEN_CONNECTION_TIMEOUT);
			urlConnection.setReadTimeout(HTTP_READ_STREAM_TIMEOUT);
			InputStream is = urlConnection.getInputStream();
			parser.setInput(is, "utf-8");
			parser.next();
		} catch (MalformedURLException e) {
			Log.e("habreader error", e.toString());
			throw e;
		} catch (IOException e) {
			Log.e("habreader error", e.toString());
			throw e;
		} catch (XmlPullParserException e) {
			Log.e("habreader error", e.toString());
			throw e;
		}
		return parser;
	}

	@Override
	public void flashResources() {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				Log.e("habreader error", e.toString());
				throw new RuntimeException(e);
			}
		}
	}		
}

class BestHubsContentProvider extends NetworkContentProvider {		
	String url = "http://habrahabr.ru/rss/hubs/";

	@Override
	String getUrl() {		
		return url;
	}
	
}

class QAHubsContentProvider extends NetworkContentProvider {		
	String url = "http://habrahabr.ru/rss/qa/";

	@Override
	String getUrl() {		
		return url;
	}
	
}

class TestFileContentProvider extends ContentProvider {		
	
	XmlPullParser parser = null;
	
	TestFileContentProvider (XmlPullParser parser) {
		this.parser = parser;
	}
	
	public XmlPullParser getContentParser() {		
		return this.parser;
	}

	@Override
	public void flashResources() {
		
	}			
}
