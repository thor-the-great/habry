package com.example.habreader_testrss.feedprovider;

import java.util.List;

import com.example.habreader_testrss.Message;

public interface FeedParserInterface {
	 List<Message> parse(ContentProvider contentProvider);
}
