package org.thor.habry.uimanagement;

import java.util.ArrayList;
import java.util.List;

import org.thor.habry.AppRuntimeContext;
import org.thor.habry.dto.Message;
import org.thor.habry.feeddetail.PostDetail;
import org.thor.habry.feeddetail.PostDetailSectionFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class UIMediator {

	int MAX_TITLE_LENGTH = 500;
	int MAX_DESC_LENGTH = 150;
	int MAX_NUMBER_OF_CATEGORIES = 3;

	public void showFeedList (List<Message> result, final ViewGroup mainLayout, final Activity activity) {
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean isShowPartOfFullFeed = sharedPref.getBoolean("setting_isShowPartOfFullFeed", false);

		for (int i = 0; i < result.size(); i++) {
			Message message = result.get(i);			
			createOneFeedMessageRow(isShowPartOfFullFeed, message, mainLayout, activity, i);
		}
	}

	private void createOneFeedMessageRow(boolean isShowPartOfFullFeed, final Message message, final ViewGroup mainLayout, final Activity activity, int messageIndexInList) {

		ScrollView scrollView = new ScrollView(mainLayout.getContext());
		//LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		//scrollView.setLayoutParams(layoutParams);
		LinearLayout feedMainContainer = new LinearLayout(scrollView.getContext());
		feedMainContainer.setOrientation(LinearLayout.HORIZONTAL);
		
		LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		CheckBox filterByCategoryWidget = new CheckBox(mainLayout.getContext());
		filterByCategoryWidget.setChecked(false);
		filterByCategoryWidget.setLayoutParams(layoutParams);
		
		feedMainContainer.addView(filterByCategoryWidget);
		
		LinearLayout feedElementContainer = new LinearLayout(feedMainContainer.getContext());
		feedElementContainer.setOrientation(LinearLayout.VERTICAL);

		TextView feedTitleTextview = new TextView(feedElementContainer.getContext());
		
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
		
		feedTitleTextview.setOnClickListener(new OnClickListener() {	
			
			@Override
			public void onClick(View v) {		
				((View)v.getParent()).setFadingEdgeLength(2);				
				Toast myToast = Toast.makeText(v.getContext(), "Loading post", Toast.LENGTH_SHORT);			
				myToast.show();
				
				//GetPostMainDetailsAsyncTask getFeedDetailsTask = new GetPostMainDetailsAsyncTask(mainLayout, activity);
				//getFeedDetailsTask.execute(message);	
				
				Intent detailIntent = new Intent(activity, PostDetail.class);
				detailIntent.putExtra(PostDetailSectionFragment.POST_DETAIL_MESSAGE, message);
				activity.startActivity(detailIntent);
				
				//return true;				
			}
		});		

		//TextView authorInfo = new TextView(mainLayout.getContext());			
		//authorInfo.setLayoutParams(layoutParams);			

		//authorInfo.setText(message.getAuthor());
		//authorInfo.setTextColor(Color.GREEN);
		//authorInfo.setGravity(Gravity.RIGHT);
		//authorInfo.setTextSize((float) 12.0);		
		//feedElementContainer.addView(authorInfo);

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
		
		feedMainContainer.addView(feedElementContainer);		

		LinearLayout.LayoutParams scrollViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scrollViewLayoutParams.setMargins(1, 5, 1, 5);
		scrollView.addView(feedMainContainer);		


		scrollView.setVerticalFadingEdgeEnabled(true);
		scrollView.setFadingEdgeLength(1);
		scrollView.setTag(messageIndexInList);
		scrollView.setBackgroundColor(Color.WHITE);
		mainLayout.addView(scrollView, scrollViewLayoutParams);
		
		FilterFeedsListener filterFeedsListener = new FilterFeedsListener(mainLayout, message, messageIndexInList);		
		filterByCategoryWidget.setOnCheckedChangeListener(filterFeedsListener);
	}
	
	class FilterFeedsListener implements OnCheckedChangeListener {
		
		View mainLayout;
		Message message;
		int messageIndex;
		
		FilterFeedsListener (View mainLayout, Message message, int messageIndexParam) {
			this.mainLayout = mainLayout;
			this.message = message;
			this.messageIndex = messageIndexParam;
		}
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
			if (isChecked) {
				List<Integer> notSameCategoryIndexList = new ArrayList<Integer>();
				List<String> filterMessageCategories = message.getCategories();
				List<Message> feedList = AppRuntimeContext.getInstance().getFeedList();
				for(int i = 0; i < feedList.size(); i++ ) {
					Message oneFeed = feedList.get(i);
					List<String> oneMessageCategoryList = oneFeed.getCategories();
					boolean oneMessageBelongsToCategories = false;
					for (String oneMessageCategory : oneMessageCategoryList) {
						if (filterMessageCategories.contains(oneMessageCategory)) {
							oneMessageBelongsToCategories = true;
							break;
						}
					}
					if(!oneMessageBelongsToCategories) {
						notSameCategoryIndexList.add(i);
					}
				}
				
				for(int i=0; i<((ViewGroup)mainLayout).getChildCount(); ++i) {
				    View nextChild = ((ViewGroup)mainLayout).getChildAt(i);
				    if (nextChild.getTag() != null) {
				    	int messageIndex = (Integer) nextChild.getTag();
				    	for (Integer sameCategoryIndexValue : notSameCategoryIndexList) {
							if (sameCategoryIndexValue.compareTo(messageIndex) == 0) {
								nextChild.setVisibility(View.GONE);
							}
						}
				    }
				}
			} else {
				for(int i=0; i<((ViewGroup)mainLayout).getChildCount(); ++i) {
				    View nextChild = ((ViewGroup)mainLayout).getChildAt(i);
				    if (View.GONE == nextChild.getVisibility() ) {
				    	nextChild.setVisibility(View.VISIBLE);
				    }
				}
			}
		}
		
	}
}
