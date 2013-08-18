package org.thor.habry.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thor.habry.AppRuntimeContext;
import org.thor.habry.dto.Comment;
import org.thor.habry.dto.Message;
import org.thor.habry.messageparser.MessageParser;
import org.thor.habry.messageparser.MessageParser.CommentParsing;
import org.thor.habry.messageparser.MessageParser.ParsingStrategy;
import org.thor.habry.uimanagement.UIMediator;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;


public class GetPostCommentsAsyncTask extends AsyncTask<Message, Integer, List<Comment>> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	
	public GetPostCommentsAsyncTask(ViewGroup mainLayout, Activity activity) {
		this.mainLayout = mainLayout;
		this.activity = activity;
	}
	@Override
	protected void onPostExecute(List<Comment> result) {
		if (result != null) {		
			UIMediator uiManager = new UIMediator();
			uiManager.showCommentList(result, mainLayout, activity);
		}
	}
	
	@Override
	protected List<Comment> doInBackground(Message... feeds) {
		List<Comment> commentList = AppRuntimeContext.getInstance().getCommentReplyList();
		if (commentList == null || commentList.size() == 0) {
			Message feedMessage = feeds[0];			
			Map<String, Object> documentParams = new HashMap<String, Object>(); 
			ParsingStrategy parsingStrategy = new CommentParsing();
			commentList = MessageParser.getInstance(documentParams).parseToComments(feedMessage, parsingStrategy);
			AppRuntimeContext.getInstance().addCommentReplyList(commentList);
		}
		return commentList;
	}	

}
