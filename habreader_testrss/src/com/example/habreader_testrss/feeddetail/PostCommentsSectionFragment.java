package com.example.habreader_testrss.feeddetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habreader_testrss.R;
import com.example.habreader_testrss.dto.Message;
import com.example.habreader_testrss.tasks.GetPostMainDetailsAsyncTask;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class PostCommentsSectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String POST_DETAIL_MESSAGE = "post_detail_message";

	public PostCommentsSectionFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_post_detail_dummy, container, false);
		View mainLayout = rootView.findViewById(R.id.post_detail_container_layout);
		//TextView dummyTextView = (TextView) rootView
		//		.findViewById(R.id.section_label);
		//dummyTextView.setText(Integer.toString(getArguments().getInt(
		//		ARG_SECTION_NUMBER)));
		Message postMessage = (Message) getArguments().getSerializable(POST_DETAIL_MESSAGE);
		GetPostMainDetailsAsyncTask getFeedDetailsTask = new GetPostMainDetailsAsyncTask((ViewGroup) mainLayout, this.getActivity());
		getFeedDetailsTask.execute(postMessage);
		return rootView;
	}
}
