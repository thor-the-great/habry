package org.thor.habry.feeddetail;

import org.thor.habry.dto.Message;
import org.thor.habry.tasks.GetPostMainDetailsAsyncTask;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.thor.habry.R;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class PostDetailSectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String POST_DETAIL_MESSAGE = "post_detail_message";

	public PostDetailSectionFragment() {
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
		ProgressDialog pd = ProgressDialog.show(this.getActivity(), null, 
				this.getActivity().getResources().getString(R.string.status_message_loading_feed), 
				true, false, null);
		GetPostMainDetailsAsyncTask getFeedDetailsTask = new GetPostMainDetailsAsyncTask((ViewGroup) mainLayout, this.getActivity(), pd);
		getFeedDetailsTask.execute(postMessage);
		return rootView;
	}
}
