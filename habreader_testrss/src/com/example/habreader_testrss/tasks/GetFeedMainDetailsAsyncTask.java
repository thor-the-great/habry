package com.example.habreader_testrss.tasks;

import java.io.IOException;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;

import com.example.habreader_testrss.dto.Message;

public class GetFeedMainDetailsAsyncTask extends AsyncTask<Message, Integer, List<Message>> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	
	public GetFeedMainDetailsAsyncTask(ViewGroup mainLayout, Activity activity) {
		this.mainLayout = mainLayout;
		this.activity = activity;
	}
	@Override
	protected List<Message> doInBackground(Message... feeds) {
		Message feedMessage = feeds[0];
		XMLReader tagsoup;
		try {
			tagsoup = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
			Builder bob = new Builder(tagsoup);
		    Document feedDocument = bob.build(feedMessage.getLink().toString());
		    Log.d("habry", "Document is parsed " + feedDocument.getChildCount());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

}
