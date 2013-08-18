package org.thor.habry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Document;

import org.thor.habry.dao.HabryDAOInterface;
import org.thor.habry.dao.HabrySQLDAOHelper;
import org.thor.habry.dto.Comment;
import org.thor.habry.dto.Message;

import android.content.Context;


public class AppRuntimeContext {
	
	private static AppRuntimeContext instance;
	
	private List<Message> feedList;
	private List<Comment> commentReplyList;
	private AppMode appMode;
	private Set<String> readedFeedRefList = new HashSet<String>();
	private HabrySQLDAOHelper daoHelper;
	private Document parsedDocumentForOneMessage;
	
	private AppRuntimeContext() {
	}
	
	public static AppRuntimeContext getInstance() {
		if (instance == null)
			instance = new AppRuntimeContext();
		return instance;
	}
	
	public void addFeedList(List<Message> feedListParam) {
		if (feedList == null)
			feedList = new ArrayList<Message>();
		feedList.clear();
		feedList.addAll(feedListParam);
	}
	
	public List<Message> getFeedList() {
		return Collections.unmodifiableList(feedList);
	}	
	
	public void addCommentReplyList(List<Comment> commentReplyList) {
		if (this.commentReplyList == null)
			this.commentReplyList = new ArrayList<Comment>();		
		this.commentReplyList.clear();
		if (commentReplyList != null)
			this.commentReplyList.addAll(commentReplyList);
	}
	
	public List<Comment> getCommentReplyList() {
		return Collections.unmodifiableList(this.commentReplyList);
	}

	public AppMode getAppMode() {
		return appMode;
	}

	public void setAppMode(AppMode appMode) {
		this.appMode = appMode;
	}

	public Set<String> getReadedFeedRefList() {
		return readedFeedRefList;
	}

	public void addMessageToReadedFeedRefList(Message message) {
		if (this.readedFeedRefList == null)
			readedFeedRefList = new HashSet<String>();
		readedFeedRefList.add(message.getMessageReference());
	}
	
	public void initDAOHelper(Context context) {
		daoHelper = new HabrySQLDAOHelper(context);
	}
	
	public HabryDAOInterface getDaoHelper() {
		return daoHelper;
	}

	public enum AppMode {
		ONE_SIDE, TWO_SIDES
	}

	public Document getParsedDocumentForOneMessage() {
		return parsedDocumentForOneMessage;
	}

	public void setParsedDocumentForOneMessage(Document parsedDocumentForOneMessage) {
		this.parsedDocumentForOneMessage = parsedDocumentForOneMessage;
	}
	
}
