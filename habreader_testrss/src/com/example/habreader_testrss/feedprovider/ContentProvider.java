package com.example.habreader_testrss.feedprovider;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public abstract class ContentProvider {
	
	public final static String NETWORK_CONTENT_PROVIDER = "NETWORK_CONTENT_PROVIDER";
	public final static String TEST_FILE_CONTENT_PROVIDER = "TEST_FILE_CONTENT_PROVIDER";
	
	static public ContentProvider getInstance(String contentProviderStrategy, Object initObject){
		ContentProvider instance = null;
		if ( NETWORK_CONTENT_PROVIDER.equals(contentProviderStrategy) ) {
			instance = new NetworkContentProvider();
		} else if ( TEST_FILE_CONTENT_PROVIDER.equals(contentProviderStrategy) ) {
			instance = new TestFileContentProvider((XmlPullParser) initObject);
		}
		return instance;
	}
	
	public abstract XmlPullParser getContentParser() throws MalformedURLException, IOException, XmlPullParserException;
	
	public abstract void flashResources();
	
}

class NetworkContentProvider extends ContentProvider {		
	String url = "http://habrahabr.ru/rss/hubs/";
	InputStream is = null;

	@Override
	public XmlPullParser getContentParser() throws MalformedURLException, IOException, XmlPullParserException {		
		XmlPullParser parser = Xml.newPullParser();
		try {		
			URL feedUrl = new URL(url);		
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			InputStream is = feedUrl.openConnection().getInputStream();
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
