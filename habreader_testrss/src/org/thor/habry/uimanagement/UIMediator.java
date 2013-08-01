package org.thor.habry.uimanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.thor.habry.AppRuntimeContext;
import org.thor.habry.R;
import org.thor.habry.dao.HabrySQLDAOHelper;
import org.thor.habry.dto.Message;
import org.thor.habry.feeddetail.PostDetail;
import org.thor.habry.feeddetail.PostDetailSectionFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
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

	public void showFeedList (List<Message> result, final ViewGroup mainLayout, final Activity activity, MessageListConfigJB listConfigJB) {
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean isShowPartOfFullFeed = sharedPref.getBoolean("setting_isShowPartOfFullFeed", false);
		if(((ViewGroup)mainLayout).getChildCount() > 0)		
			((ViewGroup)mainLayout).removeAllViews();

		for (int i = 0; i < result.size(); i++) {
			Message message = result.get(i);			
			createOneFeedMessageRow(isShowPartOfFullFeed, message, mainLayout, activity, i, listConfigJB);
		}
	}

	private void createOneFeedMessageRow(boolean isShowPartOfFullFeed, final Message message, final ViewGroup mainLayout, final Activity activity, int messageIndexInList, MessageListConfigJB listConfigJB) {

		ScrollView scrollView = new ScrollView(mainLayout.getContext());
		//LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		//scrollView.setLayoutParams(layoutParams);
		LinearLayout feedMainContainer = new LinearLayout(scrollView.getContext());
		feedMainContainer.setOrientation(LinearLayout.HORIZONTAL);
		
		LayoutParams layoutParams = new LayoutParams( LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		if(listConfigJB.isFavorFilteringEnabled()) {
			CheckBox filterByCategoryWidget = new CheckBox(mainLayout.getContext());
			filterByCategoryWidget.setChecked(false);
			filterByCategoryWidget.setLayoutParams(layoutParams);
			
			feedMainContainer.addView(filterByCategoryWidget);
			
			FilterFeedsListener filterFeedsListener = new FilterFeedsListener(mainLayout, message, messageIndexInList);		
			filterByCategoryWidget.setOnCheckedChangeListener(filterFeedsListener);
		}
		
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
		feedTitleTextview.setTextSize((float) 16.0);
		Set<String> readedMessageRefList = AppRuntimeContext.getInstance().getReadedFeedRefList();
		if(listConfigJB.isReadHighlightEnabled()) {
			if (readedMessageRefList.contains(message.getMessageReference())) {
				markViewAsReaded(feedTitleTextview);
			}
			else {
				feedTitleTextview.setTypeface(Typeface.DEFAULT_BOLD);
				feedTitleTextview.setTextColor(Color.DKGRAY);
			}
		} else {
			feedTitleTextview.setTypeface(Typeface.DEFAULT_BOLD);
			feedTitleTextview.setTextColor(Color.DKGRAY);
		}
		feedElementContainer.addView(feedTitleTextview);
		
		MessageOnClickListener onClickListener = new MessageOnClickListener(activity, message, listConfigJB);
		feedTitleTextview.setOnClickListener(onClickListener);		
		
		if(listConfigJB.isSaveMessageEnabled()) {
			feedTitleTextview.setOnLongClickListener(new OnLongClickListener() {			
				@Override
				public boolean onLongClick(View v) {
					Toast myToast = Toast.makeText(v.getContext(), R.string.status_message_saving_feed, Toast.LENGTH_SHORT);			
					myToast.show();	
					HabrySQLDAOHelper daoHelper = AppRuntimeContext.getInstance().getDaoHelper();
					daoHelper.saveOneMessage(message);
					markViewAsReaded(v);
					v.invalidate();
					return true;
				}
			});
		}
		if (listConfigJB.isSupportDelete()) {
			DeleteMessageOnLongClickListener deleteMessageOnLongClickListener = new DeleteMessageOnLongClickListener(activity, message, listConfigJB);
			feedTitleTextview.setOnLongClickListener(deleteMessageOnLongClickListener);			 
		}
		
		TextView messageStatus = new TextView(mainLayout.getContext());			
		messageStatus.setLayoutParams(layoutParams);			
		messageStatus.setText(R.string.message_list_message_status_online);
		messageStatus.setTextColor(Color.GREEN);
		messageStatus.setTextSize((float) 11.0);
		feedElementContainer.addView(messageStatus);
		
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
		feedElementContainer.addView(categories);
		
		TextView authorInfo = new TextView(mainLayout.getContext());			
		authorInfo.setLayoutParams(layoutParams);			

		authorInfo.setText(message.getAuthor());
		authorInfo.setTextColor(Color.parseColor("#6699FF"));
		authorInfo.setGravity(Gravity.RIGHT);
		authorInfo.setTextSize((float) 12.0);		
		feedElementContainer.addView(authorInfo);

		
		feedMainContainer.addView(feedElementContainer);		

		LinearLayout.LayoutParams scrollViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scrollViewLayoutParams.setMargins(1, 5, 1, 5);
		scrollView.addView(feedMainContainer);		


		scrollView.setVerticalFadingEdgeEnabled(true);
		scrollView.setFadingEdgeLength(1);
		scrollView.setTag(messageIndexInList);
		scrollView.setBackgroundColor(Color.WHITE);
		mainLayout.addView(scrollView, scrollViewLayoutParams);	
	}
	
	private void markViewAsReaded(View v) {
		((TextView)v).setTypeface(Typeface.DEFAULT);
		((TextView)v).setTextColor(Color.parseColor("#888888"));
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
	
	class MessageOnClickListener implements OnClickListener {	
		
		private Activity activity;
		private Message message;
		private MessageListConfigJB messageListConfig;
		
		MessageOnClickListener(Activity activity, Message message, MessageListConfigJB messageListConfig) {
			this.activity = activity;
			this.message = message;
			this.messageListConfig = messageListConfig;
		}
		
		@Override
		public void onClick(View v) {
			if (messageListConfig.isReadHighlightEnabled()) {			
				//((View)v.getParent()).setBackgroundColor(Color.parseColor("#6699FF"));
				AppRuntimeContext.getInstance().addMessageToReadedFeedRefList(message);
				markViewAsReaded(v);
				v.invalidate();
			}
			
			((View)v.getParent()).setFadingEdgeLength(2);				
			Toast myToast = Toast.makeText(v.getContext(), R.string.status_message_loading_feed, Toast.LENGTH_SHORT);			
			myToast.show();				
			Intent detailIntent = new Intent(activity, PostDetail.class);
			detailIntent.putExtra(PostDetailSectionFragment.POST_DETAIL_MESSAGE, message);
			activity.startActivity(detailIntent);
		}
		
	}
	
	class DeleteMessageOnLongClickListener implements OnLongClickListener {	
		
		private Activity activity;
		private Message message;
		MessageListConfigJB listConfig;
		
		DeleteMessageOnLongClickListener(Activity activity, Message message, MessageListConfigJB listConfig) {
			this.activity = activity;
			this.message = message;
			this.listConfig = listConfig;
		}
		
		@Override
		public boolean onLongClick(final View v) {					
			new AlertDialog.Builder(activity)
			//.setIcon(android.R.drawable.ic_dialog_alert)
			.setCancelable(true)
			//.setTitle(activity.getResources().getString(R.string.confirm_delete_saved_title))
			.setMessage(activity.getResources().getString(R.string.confirm_delete_saved_question))
			.setPositiveButton(activity.getResources().getString(R.string.confirm_option_yes), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) {
					HabrySQLDAOHelper daoHelper = AppRuntimeContext.getInstance().getDaoHelper();
					daoHelper.deleteMessage(message.getMessageReference()); 
					android.os.Message updateSavedList = new android.os.Message();
					if (listConfig.getMessageHandler() != null)
						listConfig.getMessageHandler().dispatchMessage(updateSavedList);
				}

			})
			.setNegativeButton(activity.getResources().getString(R.string.confirm_option_no), null)
			.show();
			return true;
		}
	}	
	
	public class MessageListConfigJB implements Serializable {

		private static final long serialVersionUID = 5433594263926342591L;
		
		private boolean favorFilteringEnabled = true;
		private boolean readHighlightEnabled = true;
		private boolean saveMessageEnabled = true;
		private boolean supportDelete = false;
		private Handler messageHandler;
		
		public boolean isFavorFilteringEnabled() {
			return favorFilteringEnabled;
		}
		public void setFavorFilteringEnabled(boolean favorFilteringEnabled) {
			this.favorFilteringEnabled = favorFilteringEnabled;
		}
		public boolean isReadHighlightEnabled() {
			return readHighlightEnabled;
		}
		public void setReadHighlightEnabled(boolean readHighlightEnabled) {
			this.readHighlightEnabled = readHighlightEnabled;
		}
		public boolean isSaveMessageEnabled() {
			return saveMessageEnabled;
		}
		public void setSaveMessageEnabled(boolean saveMessageEnabled) {
			this.saveMessageEnabled = saveMessageEnabled;
		}
		public boolean isSupportDelete() {
			return supportDelete;
		}
		public void setSupportDelete(boolean supportDelete) {
			this.supportDelete = supportDelete;
		}
		public Handler getMessageHandler() {
			return messageHandler;
		}
		public void setMessageHandler(Handler messageHandler) {
			this.messageHandler = messageHandler;
		}
		
	}
}
