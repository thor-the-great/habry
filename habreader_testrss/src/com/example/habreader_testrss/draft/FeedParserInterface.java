package com.example.habreader_testrss.draft;

import java.util.List;

import com.example.habreader_testrss.dto.Message;
import com.example.habreader_testrss.feedprovider.ContentProvider;

public interface FeedParserInterface {
	 List<Message> parse(ContentProvider contentProvider);
}
