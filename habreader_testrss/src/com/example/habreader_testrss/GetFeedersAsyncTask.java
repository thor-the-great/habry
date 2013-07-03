package com.example.habreader_testrss;

import java.util.List;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GetFeedersAsyncTask extends AsyncTask<String, Integer, List<Message>> {
	
	int MAX_TITLE_LENGTH = 400;
	
	ViewGroup mainLayout;
	
	public GetFeedersAsyncTask(ViewGroup mainLayout) {
		this.mainLayout = mainLayout;
	}
	
	@Override
	protected List<Message> doInBackground(String ... urls) {
		String defaultHabrRssURL = urls[0];
		BaseFeedParser feederParser = new SaxFeedParser(defaultHabrRssURL);
		List<Message> listOfHabrMsg = feederParser.parse();
		Log.d("habreader", "numbr of parsed messages " + (listOfHabrMsg == null ? "NULL" : listOfHabrMsg.size()));
		//for (Message message : listOfHabrMsg) {
			//Log.d("habreader", "message details.");
			/*Log.d("habreader", "message.getTitle() = " + message.getTitle() );
			Log.d("habreader", "message.getDescription() = " + message.getDescription());*/
		//}
		return listOfHabrMsg;
	}

	@Override
	protected void onPostExecute(List<Message> result) {
		//int tableMaxWidth = tableLayout.getWidth();
		for (int i = 0; i < result.size(); i++) {
			Message message = result.get(i);
			
			ScrollView scrollView = new ScrollView(mainLayout.getContext());
			//LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			//scrollView.setLayoutParams(layoutParams);
			
			TextView textview = new TextView(scrollView.getContext());
			LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			textview.setLayoutParams(layoutParams);
			
			int actualTitleLength = message.getTitle() == null ? 0 : message.getTitle().length();
			String feedTitle = "";
			if (actualTitleLength > MAX_TITLE_LENGTH ) {
			//if (actualTitleLength > tableMaxWidth) {
				feedTitle = message.getTitle().substring(0, MAX_TITLE_LENGTH);
				//feedTitle = message.getTitle().substring(0, tableMaxWidth);
			} else {
				feedTitle = message.getTitle();
			}
			textview.setText(feedTitle);
			textview.setTextColor(Color.DKGRAY);
			//textview.setPadding(10, 1, 10, 0);
			
			scrollView.addView(textview);
			
			LinearLayout.LayoutParams scrollViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			scrollViewLayoutParams.setMargins(10, 1, 10, 1);
			
			mainLayout.addView(scrollView, scrollViewLayoutParams);
			
			/*TableRow tr1 = new TableRow(tableLayout.getContext());
			
			tr1.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			/*AutoResizeTextView textview = new AutoResizeTextView(tableLayout.getContext());	
			
			int actualTitleLength = message.getTitle() == null ? 0 : message.getTitle().length();
			String feedTitle = "";
			//if (actualTitleLength > MAX_TITLE_LENGTH ) {
			//if (actualTitleLength > tableMaxWidth) {
				//feedTitle = message.getTitle().substring(0, MAX_TITLE_LENGTH);
				//feedTitle = message.getTitle().substring(0, tableMaxWidth);
			//} else {
				feedTitle = message.getTitle();
			//}
			textview.setText(feedTitle);
			//textview.getTextColors(R.color.)
			textview.setTextColor(Color.DKGRAY);	
			
			
			tr1.addView(textview);
			Button btn = new Button(tableLayout.getContext());
			btn.setText("Test test test test tes tes test test ");
			
			
			tr1.addView(btn);
			tableLayout.addView(tr1, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));*/
			//textview.resizeText();
		}
		
	}	
	

}
