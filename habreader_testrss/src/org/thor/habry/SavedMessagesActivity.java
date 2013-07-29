package org.thor.habry;

import java.util.List;

import org.thor.habry.dto.Message;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SavedMessagesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_messages);
		List<Message> messageList = AppRuntimeContext.getInstance().getDaoHelper().findAllMessages();
		if(messageList == null || messageList.size() == 0) {
			Toast.makeText(getBaseContext(), getResources().getString(R.string.status_message_no_saved_messages), Toast.LENGTH_SHORT).show();
			
		} else {
			ViewGroup mainView = (ViewGroup) findViewById(R.id.saved_messages_main_layout); 
			for (Message message : messageList) {
				TextView messageTitle = new TextView(getBaseContext());			
				//messageStatus.setLayoutParams(layoutParams);			
				messageTitle.setText(message.getTitle());				
				mainView.addView(messageTitle);				
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_messages, menu);
		return true;
	}

}
