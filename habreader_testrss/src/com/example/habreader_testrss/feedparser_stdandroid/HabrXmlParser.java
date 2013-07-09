package com.example.habreader_testrss.feedparser_stdandroid;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.example.habreader_testrss.Message;

import android.util.Xml;

public class HabrXmlParser {
	// We don't use namespaces
	private static final String ns = null;

	//public List<Message> parse(InputStream in) throws XmlPullParserException,
		//	IOException {
	public List<Message> parse( XmlPullParser parser, InputStream in ) throws XmlPullParserException, IOException {
		if (parser == null) {
			parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.next();
			//parser.require(XmlPullParser.START_TAG, ns, FeedTags.RSS.toString());
		} 
		try {		
			int eventType = parser.getEventType();
			List<Message> entries = new ArrayList<Message>();
		    while (eventType != XmlPullParser.END_DOCUMENT) {
		    	switch (eventType){
	            case XmlPullParser.START_TAG:
	                String name = parser.getName(); 
	                if (FeedTags.ITEM.toString().equalsIgnoreCase(name)) {
	                	Message message = readEntry(parser);
	                	entries.add(message);
	                }
	                break;
		    	}
		    	eventType = parser.next();
		    }
			//parser.nextTag();
			
			//return readFeed(parser);
		    return entries;
		} finally {
			if (in != null)
			in.close();
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
		while (parser.next() != XmlPullParser.END_TAG) {
			//if (parser.getEventType() != XmlPullParser.START_TAG) {
			//	continue;
			//}
			String name = parser.getName();
			if (FeedTags.TITLE.toString().equalsIgnoreCase(name)) {
				title = readTitle(parser);
			} else if (FeedTags.DESCRIPTION.toString().equalsIgnoreCase(name)) {
				description = readDescription(parser);
			}
			/*
			 * else if (name.equals("summary")) { summary = readSummary(parser);
			 * } else if (name.equals("link")) { link = readLink(parser); }
			 *///else {
				//skip(parser);
			//}
		}
		return new Message(title, description);
	}

	// Processes title tags in the feed.
	private String readTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}

	// Processes title tags in the feed.
	private String readDescription(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "description");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "description");
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
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
}
