package org.thor.habry.tasks;

import org.thor.habry.AppRuntimeContext;
import org.thor.habry.dao.HabryDAOInterface;
import org.thor.habry.dao.HabrySQLDAOHelper;
import org.thor.habry.dto.Message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.ViewGroup;

public class SaveMessageAsyncTask extends AsyncTask<Message, Integer, Void> {
	
	ViewGroup mainLayout;
	Activity activity;
	Exception error;
	ProgressDialog pd;
	
	public SaveMessageAsyncTask(ViewGroup mainLayout, Activity activity, ProgressDialog pd) {
		this.mainLayout = mainLayout;
		this.activity = activity;
		this.pd = pd;
	}

	@Override
	protected Void doInBackground(Message ... messages) {		
		Message message = messages[0];		
		HabryDAOInterface daoService = AppRuntimeContext.getInstance().getDaoHelper();
		daoService.saveOneMessage(message);				
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		pd.dismiss();				
	}

}
