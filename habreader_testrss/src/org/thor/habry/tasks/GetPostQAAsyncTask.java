package org.thor.habry.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thor.habry.AppRuntimeContext;
import org.thor.habry.R;
import org.thor.habry.dto.Comment;
import org.thor.habry.dto.Message;
import org.thor.habry.messageparser.MessageParser;
import org.thor.habry.messageparser.MessageParser.ReplyParsing;
import org.thor.habry.messageparser.MessageParser.ParsingStrategy;
import org.thor.habry.uimanagement.UIMediator;

import nu.xom.Document;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class GetPostQAAsyncTask extends AsyncTask<Message, Integer, List<Comment>> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	
	public GetPostQAAsyncTask(ViewGroup mainLayout, Activity activity) {
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
			ParsingStrategy parsingStrategy = new ReplyParsing();
			commentList = MessageParser.getInstance(documentParams).parseToComments(feedMessage, parsingStrategy);
			AppRuntimeContext.getInstance().addCommentReplyList(commentList);
		}
		return commentList;
	}	

}
