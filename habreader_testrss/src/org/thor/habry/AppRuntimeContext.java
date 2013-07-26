package org.thor.habry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.thor.habry.dto.Message;


public class AppRuntimeContext {
	
	private static AppRuntimeContext instance;
	
	private List<Message> feedList;
	
	private AppRuntimeContext() {}
	
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
}
