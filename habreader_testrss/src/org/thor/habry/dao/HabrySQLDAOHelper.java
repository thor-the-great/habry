package org.thor.habry.dao;

import java.util.ArrayList;
import java.util.List;

import org.thor.habry.dto.Message;
import org.thor.habry.dto.MessageType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class HabrySQLDAOHelper extends SQLiteOpenHelper {
	
	 private static final int DATABASE_VERSION = 1;
	 private static final String DB_NAME = "habreader";
	 private static String SAVED_MESSAGE_TABLE_CREATE;
	 private static String SAVED_MESSAGE_CATEGORY_TABLE_CREATE;
	
	public HabrySQLDAOHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		initQueries();		
	}
	
	private void initQueries() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(HabryTables.SAVED_MESSAGE).append(" (")
			.append(" REFERENCE TEXT NOT NULL, ")
			.append(" TYPE TEXT NOT NULL, ")
			.append(" TITLE TEXT NOT NULL, ")
			.append(" DESCRIPTION TEXT, ")
			.append(" AUTHOR TEXT NOT NULL, ")			
			.append(" URL TEXT NOT NULL, ")
			.append(" DATETIME INTEGER, ")
			.append(" SAVED_DATETIME INTEGER );");
		SAVED_MESSAGE_TABLE_CREATE = sb.toString();
		sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(HabryTables.SAVED_MESSAGE_CATEGORY).append(" (")
			.append(" CATEGORY TEXT, ")
			.append(" MESSAGE_REF TEXT );");
		SAVED_MESSAGE_CATEGORY_TABLE_CREATE = sb.toString();
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		db.execSQL(SAVED_MESSAGE_TABLE_CREATE);
		db.execSQL(SAVED_MESSAGE_CATEGORY_TABLE_CREATE);
		db.setTransactionSuccessful();
		db.endTransaction();
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void saveOneMessage(Message message) {
		SQLiteDatabase db = getWritableDatabase();	
		db.beginTransaction();	
		try {
			SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			queryBuilder.setTables(HabryTables.SAVED_MESSAGE.name());
			Cursor cursor = queryBuilder.query(db, new String[]{"REFERENCE"}, "REFERENCE = ?", new String[] {message.getMessageReference()}, null, null, null);
			if (cursor.getCount() == 0) {	
				List<String> categories = message.getCategories();
				for (String category : categories) {
					ContentValues categoryCV = new ContentValues();
					categoryCV.put("CATEGORY", category);
					categoryCV.put("MESSAGE_REF", message.getMessageReference());
					db.insert(HabryTables.SAVED_MESSAGE_CATEGORY.name(), null, categoryCV);	
				}
				ContentValues messageCV = new ContentValues();
				messageCV.put("REFERENCE", message.getMessageReference());
				messageCV.put("TYPE", message.getType().name());
				messageCV.put("TITLE", message.getTitle());
				messageCV.put("DESCRIPTION", message.getDescription());
				messageCV.put("AUTHOR", message.getAuthor());
				messageCV.put("URL", message.getLink().toString());
				/*if (message.getDate() != null)
					messageCV.put("DATETIME", message.getDate());*/
				long nowDate = System.currentTimeMillis();
				messageCV.put("SAVED_DATETIME", nowDate);
				db.insert(HabryTables.SAVED_MESSAGE.name(), null, messageCV);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("habreader", "Cannot save message to DB. " + e );			
		} finally {
			db.endTransaction();
		}
	}
	
	public List<Message> findAllMessages() {
		List<Message> messageList = new ArrayList<Message>();
		SQLiteDatabase db = getReadableDatabase();				
		try {
			//read messages
			SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			queryBuilder.setTables(HabryTables.SAVED_MESSAGE.name());
			Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
			if (cursor.getCount() > 0) {	
				cursor.moveToFirst();
				boolean hasMore = true;
				do {
					//String reference = cursor.getString(cursor.getColumnIndexOrThrow("REFERENCE"));
					Message newMessage = new Message();
					newMessage.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("TITLE")));
					newMessage.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("AUTHOR")));
					newMessage.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")));
					newMessage.setLink(cursor.getString(cursor.getColumnIndexOrThrow("URL")));
					newMessage.setType(MessageType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("TYPE"))));
					messageList.add(newMessage);
					hasMore = cursor.moveToNext();
				} while (hasMore);			
			}
			//read categories
			if (messageList != null && messageList.size() > 0) {
				for (Message message : messageList) {			
					queryBuilder = new SQLiteQueryBuilder();
					queryBuilder.setTables(HabryTables.SAVED_MESSAGE_CATEGORY.name());
					cursor = queryBuilder.query(db, new String[] {"CATEGORY"}, "MESSAGE_REF=?", new String[]{message.getMessageReference()}, null, null, null);
					List<String> categories = new ArrayList<String>();
					if (cursor.getCount() > 0) {	
						cursor.moveToFirst();
						boolean hasMore = true;
						do {		
							categories.add(cursor.getString(cursor.getColumnIndexOrThrow("CATEGORY")));							
							hasMore = cursor.moveToNext();
						} while (hasMore);							
					}
					message.getCategories().addAll(categories);
				}
			}
			
		} catch (Exception e) {
			Log.e("habreader", "Cannot read message from DB. " + e );			
		} 
		return messageList;
	}
	
	public enum HabryTables {SAVED_MESSAGE, SAVED_MESSAGE_CATEGORY};

}
