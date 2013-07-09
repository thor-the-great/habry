package com.example.habreader_testrss.feedprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.example.habreader_testrss.Message;

import android.util.Log;

public class SaxFeedParser extends BaseFeedParser {
	
	 //public SaxFeedParser(String feedUrl){
	 //       super(feedUrl);
	 //   }
	    
	    public List<Message> parse(ContentProvider contentProvider) {
	    	//ContentProvider feedContentProvider = new TestFileContentProvider();
	    	
			InputStream is = contentProvider.getContentStream();
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
				
				StringBuffer sb = new StringBuffer();
				int oneByte = reader.read();
				while (oneByte != -1) {
					sb.append(oneByte);
					oneByte = is.read();
				}
				Log.d("habreader", "readed file is " + sb.toString());
				
				
			} catch (UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    	
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        try {
	            SAXParser parser = factory.newSAXParser();
	            MainHubRssHandler handler = new MainHubRssHandler();
	            parser.parse(is, handler);
	            return handler.getMessages();
	        } catch (Exception e) {
	            Log.e("habreader", "Error getting feeds. Exception is " + e);	
	            throw new RuntimeException(e);
	        } 
	    }
	}
