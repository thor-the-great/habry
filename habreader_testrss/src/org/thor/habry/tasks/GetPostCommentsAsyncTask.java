package org.thor.habry.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		Message feedMessage = feeds[0];			
		Map<String, Object> documentParams = new HashMap<String, Object>(); 
		//documentParams.put("MAX_DISPLAY_WIDTH", Integer.valueOf(sizePoint.x));
		//Document document = MessageParser.getInstance(documentParams).parseCommentsToDocument(feedMessage);	
		ParsingStrategy parsingStrategy = new CommentParsing();
		List<Comment> commentList = MessageParser.getInstance(documentParams).parseToComments(feedMessage, parsingStrategy);
		Log.d("habry", "comments!!!");
		if (commentList != null) {
			for (Comment comment : commentList) {
				Log.d("habry", "comment.getAuthor() = " + comment.getAuthor());
				Log.d("habry", "comment.getChild()= " + comment.getChildComments().size());
			}
		}
		return commentList;
	}	

}
