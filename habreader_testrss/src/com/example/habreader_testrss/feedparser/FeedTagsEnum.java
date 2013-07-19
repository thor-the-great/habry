package com.example.habreader_testrss.feedparser;

public enum FeedTagsEnum {
	RSS, CHANNEL, ITEM, TITLE, DESCRIPTION, AUTHOR, LINK, CATEGORY;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
