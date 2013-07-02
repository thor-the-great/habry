package com.example.habreader_testrss;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

public class GetFeedersAsyncTask extends AsyncTask<String, Integer, List<Message>> {

	@Override
	protected List<Message> doInBackground(String ... urls) {
		String defaultHabrRssURL = urls[0];
		BaseFeedParser feederParser = new SaxFeedParser(defaultHabrRssURL);
		List<Message> listOfHabrMsg = feederParser.parse();
		Log.d("habreader", "numbr of parsed messages " + (listOfHabrMsg == null ? "NULL" : listOfHabrMsg.size()));
		for (Message message : listOfHabrMsg) {
			Log.d("habreader", "message details.");
			Log.d("habreader", "message.getTitle() = " + message.getTitle() );
			Log.d("habreader", "message.getDescription() = " + message.getDescription());
		}
		return listOfHabrMsg;
	}

}
