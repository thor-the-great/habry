package org.thor.habry.feeddetail;

import org.thor.habry.dto.Message;
import org.thor.habry.tasks.GetPostCommentsAsyncTask;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.thor.habry.AppRuntimeContext;
import org.thor.habry.R;

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
				R.layout.fragment_post_detail_comment, container, false);
		View mainLayout = rootView.findViewById(R.id.post_detail_comment_main_layout);
		
		Message postMessage = (Message) getArguments().getSerializable(POST_DETAIL_MESSAGE);
		GetPostCommentsAsyncTask getFeedDetailsTask = new GetPostCommentsAsyncTask((ViewGroup) mainLayout, this.getActivity());
		getFeedDetailsTask.execute(postMessage);
		return rootView;
	}
}
