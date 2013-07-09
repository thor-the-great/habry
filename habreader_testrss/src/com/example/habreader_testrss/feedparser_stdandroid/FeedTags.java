package com.example.habreader_testrss.feedparser_stdandroid;

public enum FeedTags {
	RSS, TITLE, DESCRIPTION, CHANNEL, ITEM;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
