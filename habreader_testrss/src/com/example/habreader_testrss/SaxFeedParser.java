package com.example.habreader_testrss;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser {
	
	 protected SaxFeedParser(String feedUrl){
	        super(feedUrl);
	    }
	    
	    public List<Message> parse() {
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        try {
	            SAXParser parser = factory.newSAXParser();
	            MainHubRssHandler handler = new MainHubRssHandler();
	            parser.parse(this.getInputStream(), handler);
	            return handler.getMessages();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        } 
	    }
	}
