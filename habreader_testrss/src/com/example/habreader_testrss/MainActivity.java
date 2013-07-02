package com.example.habreader_testrss;

import java.util.List;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	
	Button btn_loadRssForUser = null;
	EditText txtField_userName = null;
	EditText txtField_userPassword = null;
	
	String defaultHabrRssURL = "http://habrahabr.ru/rss/hubs/";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_loadRssForUser = (Button) findViewById(R.id.loadRss);
        btn_loadRssForUser.setOnClickListener(this);
        txtField_userName = (EditText) findViewById(R.id.userName);
        txtField_userPassword = (EditText) findViewById(R.id.userPassword);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		String userName = txtField_userName.getText().toString();
		String userPassword = txtField_userPassword.getText().toString();
		Log.d("habreader", "user info : username = " + userName + "; password = " + userPassword);
		/*BaseFeedParser feederParser = new SaxFeedParser(defaultHabrRssURL);
		List<Message> listOfHabrMsg = feederParser.parse();
		Log.d("habreader", "numbr of parsed messages " + (listOfHabrMsg == null ? "NULL" : listOfHabrMsg.size()));
		*/
		
		new GetFeedersAsyncTask().execute(defaultHabrRssURL);
		
	}
    
}
