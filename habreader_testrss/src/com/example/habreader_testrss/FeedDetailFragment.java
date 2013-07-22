package com.example.habreader_testrss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.habreader_testrss.dummy.DummyContent;
import com.example.habreader_testrss.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a single Feed detail screen. This fragment is either
 * contained in a {@link FeedListActivity} in two-pane mode (on tablets) or a
 * {@link FeedDetailActivity} on handsets.
 */
public class FeedDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public FeedDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_feed_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.feed_detail))
					.setText(mItem.content);
		}

		return rootView;
	}
	
	public static FeedDetailFragment newInstance(int index) {
		FeedDetailFragment f = new FeedDetailFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        
        args.putString(FeedDetailFragment.ARG_ITEM_ID, "1");
        f.setArguments(args);        

        return f;
    }
}
