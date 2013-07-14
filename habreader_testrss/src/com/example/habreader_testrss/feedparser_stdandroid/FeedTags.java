package com.example.habreader_testrss.feedparser_stdandroid;

public enum FeedTags {
	RSS, CHANNEL, ITEM, TITLE, DESCRIPTION, AUTHOR, LINK, CATEGORY;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
