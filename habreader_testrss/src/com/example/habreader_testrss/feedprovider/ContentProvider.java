package com.example.habreader_testrss.feedprovider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.R;
import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

public abstract class ContentProvider {
	
	public final static String NETWORK_CONTENT_PROVIDER = "NETWORK_CONTENT_PROVIDER";
	public final static String TEST_FILE_CONTENT_PROVIDER = "TEST_FILE_CONTENT_PROVIDER";
	
	static public ContentProvider getInstance(String contentProviderStrategy, Object initObject){
		ContentProvider instance = null;
		if ( NETWORK_CONTENT_PROVIDER.equals(contentProviderStrategy) ) {
			instance = new NetworkContentProvider();
		} else if ( TEST_FILE_CONTENT_PROVIDER.equals(contentProviderStrategy) ) {
			instance = new TestFileContentProvider((InputStream) initObject);
		}
		return instance;
	}
	
	public abstract InputStream getContentStream();
	
}

class NetworkContentProvider extends ContentProvider {		
	String url = "http://habrahabr.ru/rss/hubs/";

	@Override
	public InputStream getContentStream() {
		
		try {			
			URL feedUrl = new URL(url);
            return feedUrl.openConnection().getInputStream();
        } catch (MalformedURLException e) {
        	Log.e("habreader error", e.toString());
            throw new RuntimeException(e);
        } catch (IOException e) {
        	Log.e("habreader error", e.toString());
        	throw new RuntimeException(e);
		}
	}		
}

class TestFileContentProvider extends ContentProvider {		
	
	InputStream is = null;
	
	TestFileContentProvider (InputStream is) {
		this.is = new BufferedInputStream(is);
	}
	
	String TEST_FILE_NAME = "testFeeds.xml";		
	@Override
	public InputStream getContentStream() {
		return this.is;
	}		
}
