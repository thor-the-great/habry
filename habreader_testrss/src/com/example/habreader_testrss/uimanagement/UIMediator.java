package com.example.habreader_testrss.uimanagement;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habreader_testrss.dto.Message;
import com.example.habreader_testrss.tasks.GetFeedMainDetailsAsyncTask;

public class UIMediator {

	int MAX_TITLE_LENGTH = 50;
	int MAX_DESC_LENGTH = 150;
	int MAX_NUMBER_OF_CATEGORIES = 3;

	public void showFeedList (List<Message> result, final ViewGroup mainLayout, final Activity activity) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean isShowPartOfFullFeed = sharedPref.getBoolean("setting_isShowPartOfFullFeed", false);

		for (int i = 0; i < result.size(); i++) {
			Message message = result.get(i);			
			createOneFeedMessageRow(isShowPartOfFullFeed, message, mainLayout, activity);
		}
	}

	private void createOneFeedMessageRow(boolean isShowPartOfFullFeed, final Message message, final ViewGroup mainLayout, final Activity activity) {

		ScrollView scrollView = new ScrollView(mainLayout.getContext());
		//LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		//scrollView.setLayoutParams(layoutParams);
		LinearLayout feedElementContainer = new LinearLayout(scrollView.getContext());
		feedElementContainer.setOrientation(LinearLayout.VERTICAL);

		TextView feedTitleTextview = new TextView(feedElementContainer.getContext());
		LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		feedTitleTextview.setLayoutParams(layoutParams);			
		int actualTitleLength = message.getTitle() == null ? 0 : message.getTitle().length();
		String feedTitle = "";
		if (actualTitleLength > MAX_TITLE_LENGTH ) {
			feedTitle = message.getTitle().substring(0, MAX_TITLE_LENGTH) + "...";
		} else {
			feedTitle = message.getTitle();
		}
		feedTitleTextview.setText(feedTitle);
		feedTitleTextview.setTextColor(Color.DKGRAY);
		feedTitleTextview.setTextSize((float) 16.0);
		feedTitleTextview.setTypeface(Typeface.DEFAULT_BOLD);
		feedElementContainer.addView(feedTitleTextview);

		feedTitleTextview.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {		


				//((View)v.getParent()).setBackgroundResource(R.drawable.borders);
				((View)v.getParent()).setFadingEdgeLength(2);
				
				Toast myToast = Toast.makeText(v.getContext(), "Loading post", Toast.LENGTH_SHORT);			
				myToast.show();

				GetFeedMainDetailsAsyncTask getFeedDetailsTask = new GetFeedMainDetailsAsyncTask(mainLayout, activity);
				getFeedDetailsTask.execute(message);
				
				return false;

				//((View)v.getParent()).
				//setBackgroundDrawable(activity.getResources().getDrawable(android.R.drawable.list_selector_background)); 
			}
		});

		if (isShowPartOfFullFeed) {				
			TextView feedDescriptionTextview = new TextView(feedElementContainer.getContext());			
			feedDescriptionTextview.setLayoutParams(layoutParams);			
			actualTitleLength = message.getDescription() == null ? 0 : message.getDescription().length();
			String feedDescription = "";
			if (actualTitleLength > MAX_DESC_LENGTH ) {
				feedDescription = message.getDescription().substring(0, MAX_DESC_LENGTH) + "...";
			} else {
				feedDescription = message.getDescription();
			}
			feedDescriptionTextview.setText(feedDescription);
			feedDescriptionTextview.setTextColor(Color.DKGRAY);
			feedDescriptionTextview.setTextSize((float) 12.0);
			feedElementContainer.addView(feedDescriptionTextview);
		}		

		TextView authorInfo = new TextView(mainLayout.getContext());			
		authorInfo.setLayoutParams(layoutParams);			

		authorInfo.setText(message.getAuthor());
		authorInfo.setTextColor(Color.GREEN);
		authorInfo.setGravity(Gravity.RIGHT);
		authorInfo.setTextSize((float) 12.0);
		//textview.setPadding(10, 1, 10, 0);			
		//scrollView.addView(authorInfo);
		feedElementContainer.addView(authorInfo);

		TextView categories = new TextView(mainLayout.getContext());			
		categories.setLayoutParams(layoutParams);			
		StringBuilder categorySB = new StringBuilder();
		if (message.getCategories() != null) { 
			for (int j = 0; j < message.getCategories().size() && j < MAX_NUMBER_OF_CATEGORIES; j++) {
				categorySB.append(message.getCategories().get(j));
				if (((j + 1) < message.getCategories().size()) && ((j + 1) < MAX_NUMBER_OF_CATEGORIES)) {
					categorySB.append( " * " ) ; 
				}
			}
		}
		categories.setText(categorySB.toString());
		categories.setTextColor(Color.GRAY);
		categories.setTextSize((float) 12.0);
		//textview.setPadding(10, 1, 10, 0);			
		//scrollView.addView(authorInfo);
		feedElementContainer.addView(categories);

		LinearLayout.LayoutParams scrollViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		scrollViewLayoutParams.setMargins(10, 1, 10, 20);
		scrollView.addView(feedElementContainer);		


		scrollView.setVerticalFadingEdgeEnabled(true);
		scrollView.setFadingEdgeLength(2);

		mainLayout.addView(scrollView, scrollViewLayoutParams);
	}	
}
