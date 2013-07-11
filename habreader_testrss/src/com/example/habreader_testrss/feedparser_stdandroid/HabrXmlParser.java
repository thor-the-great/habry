package com.example.habreader_testrss.feedparser_stdandroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.example.habreader_testrss.dto.Message;
import com.example.habreader_testrss.feedprovider.ContentProvider;

public class HabrXmlParser {
	// We don't use namespaces
	private static final String ns = null;

	public List<Message> parse( ContentProvider contentProvider ) throws XmlPullParserException, IOException {
		XmlPullParser parser = contentProvider.getContentParser();		
		try {		
			int eventType = parser.getEventType();
			List<Message> entries = new ArrayList<Message>();
		    while (eventType != XmlPullParser.END_DOCUMENT) {
		    	switch (eventType){
	            case XmlPullParser.START_TAG:
	                String name = parser.getName(); 
	                /*if (name != null) {
	                	Log.i("habreader - trace", "name = " + name);
	                }*/
	                if (FeedTags.ITEM.toString().equalsIgnoreCase(name)) {
	                	Message message = readEntry(parser);
	                	entries.add(message);
	                }
	                break;
		    	}
		    	eventType = parser.next();
		    }
		    return entries;
		} finally {
			contentProvider.flashResources();
		}
	}

	private List<Message> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Message> entries = new ArrayList<Message>();

		
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (FeedTags.ITEM.toString().equalsIgnoreCase(name)) {
				entries.add(readEntry(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	// Parses the contents of an entry. If it encounters a title, summary, or
	// link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private Message readEntry(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		//parser.require(XmlPullParser.START_TAG, ns, FeedTags.ITEM.toString());
		String title = null;
		String summary = null;
		String link = null;
		String description = null;
		String author = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			 /*if (name != null) {
             	Log.i("habreader - trace", "name = " + name);
             }*/
			if (FeedTags.TITLE.toString().equalsIgnoreCase(name)) {
				title = readTitle(parser);
			} else if (FeedTags.DESCRIPTION.toString().equalsIgnoreCase(name)) {
				description = readDescription(parser);
			} else if (FeedTags.AUTHOR.toString().equalsIgnoreCase(name)) {
				author = readAuthor(parser);			
			} else {			
				skip(parser);
			}
		}
		return new Message(title, description, author);
	}

	// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		return readTextValueOfTag(parser, FeedTags.TITLE);
	}

	// Processes title tags in the feed.
	private String readDescription(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		return readTextValueOfTag(parser, FeedTags.DESCRIPTION);
	}
	
	private String readAuthor(XmlPullParser parser) throws IOException,	XmlPullParserException {		
		return readTextValueOfTag(parser, FeedTags.AUTHOR);
	}
	
	private String readTextValueOfTag(XmlPullParser parser, FeedTags feedTag) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, feedTag.toString());
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, feedTag.toString());
		return title;
	}

	/*
	 * // Processes link tags in the feed. private String readLink(XmlPullParser
	 * parser) throws IOException, XmlPullParserException { String link = "";
	 * parser.require(XmlPullParser.START_TAG, ns, "link"); String tag =
	 * parser.getName(); String relType = parser.getAttributeValue(null, "rel");
	 * if (tag.equals("link")) { if (relType.equals("alternate")){ link =
	 * parser.getAttributeValue(null, "href"); parser.nextTag(); } }
	 * parser.require(XmlPullParser.END_TAG, ns, "link"); return link; }
	 * 
	 * // Processes summary tags in the feed. private String
	 * readSummary(XmlPullParser parser) throws IOException,
	 * XmlPullParserException { parser.require(XmlPullParser.START_TAG, ns,
	 * "summary"); String summary = readText(parser);
	 * parser.require(XmlPullParser.END_TAG, ns, "summary"); return summary; }
	 */

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		//if (parser.getEventType() != XmlPullParser.START_TAG) {
			//throw new IllegalStateException();
		//}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {			
			case XmlPullParser.END_TAG: {
				depth--;
				String name = parser.getName();
				 /*if (name != null) {
	             	Log.i("habreader - skip", "</" + name + ">");
	             }*/
			}
				break;
			case XmlPullParser.START_TAG: {
				depth++;
				String name = parser.getName();
				 /*if (name != null) {
	             	Log.i("habreader - skip", "<" + name + ">");
	             }*/
			}
				break;
			}
		}
	}
}
