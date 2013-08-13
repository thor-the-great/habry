package org.thor.habry.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
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

public class HabrySQLDAOHelper extends SQLiteOpenHelper implements HabryDAOInterface {
	
	 private static final int DATABASE_VERSION = 2;
	 private static final String DB_NAME = "habreader";
	 private static String SAVED_MESSAGE_TABLE_CREATE;
	 private static String SAVED_MESSAGE_CATEGORY_TABLE_CREATE;
	 private static String SAVED_MESSAGE_CONTENT_TABLE_CREATE;
	
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
		sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(HabryTables.SAVED_MESSAGE_CONTENT).append(" (")
			.append(" MESSAGE_REF TEXT, ")
			.append(" CONTENT TEXT );");
		SAVED_MESSAGE_CONTENT_TABLE_CREATE = sb.toString();
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		db.execSQL(SAVED_MESSAGE_TABLE_CREATE);
		db.execSQL(SAVED_MESSAGE_CATEGORY_TABLE_CREATE);
		db.execSQL(SAVED_MESSAGE_CONTENT_TABLE_CREATE);
		db.setTransactionSuccessful();
		db.endTransaction();
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + HabryTables.SAVED_MESSAGE );
        db.execSQL("DROP TABLE IF EXISTS " + HabryTables.SAVED_MESSAGE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + HabryTables.SAVED_MESSAGE_CONTENT);
        onCreate(db);
	}
	
	public void saveOneMessage(Message message) {
		
		URLConnection urlConnection = null;
		BufferedReader in = null;
		StringBuilder messageHtmlBuilder = new StringBuilder();
		try {		
			urlConnection = message.getLink().openConnection();
					
			in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				messageHtmlBuilder.append(inputLine);
			}		            
			in.close();
			
		} catch (MalformedURLException e) {
			Log.e("habreader error", e.toString());
		} catch (IOException e) {
			Log.e("habreader error", e.toString());
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("habreader error", e.toString());
				}
			}
		}
		
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
				
				ContentValues contentCV = new ContentValues();
				contentCV.put("MESSAGE_REF", message.getMessageReference());
				contentCV.put("CONTENT", messageHtmlBuilder.toString());
				db.insert(HabryTables.SAVED_MESSAGE_CONTENT.name(), null, contentCV);
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
					newMessage.setOnline(false);
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
	
	public String readMessageContentByRef(String messageRef) {
		String content = "";
		SQLiteDatabase db = getReadableDatabase();				
		try {
			SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			queryBuilder.setTables(HabryTables.SAVED_MESSAGE.name() + "," + HabryTables.SAVED_MESSAGE_CONTENT);
			Cursor cursor = queryBuilder.query(db, new String[]{"CONTENT"}, "MESSAGE_REF=?", new String[]{messageRef}, null, null, null);
			if (cursor.getCount() > 0) {	
				cursor.moveToFirst();
				content = cursor.getString(cursor.getColumnIndexOrThrow("CONTENT"));							
			}	
			
		} catch (Exception e) {
			Log.e("habreader", "Cannot read message content from DB. " + e );			
		} 
		return content;
	}
	
	public void deleteMessage(String messageRef) {
 		SQLiteDatabase db = getWritableDatabase();
 		try {
 			db.delete(HabryTables.SAVED_MESSAGE_CATEGORY.name(), "MESSAGE_REF=?", new String[]{messageRef});
 			db.delete(HabryTables.SAVED_MESSAGE.name(), "REFERENCE=?", new String[]{messageRef});
 		} catch (Exception e) {
			Log.e("habreader", "Cannot delete message from DB. " + e );			
		}
 	}
	
	public enum HabryTables {SAVED_MESSAGE, SAVED_MESSAGE_CATEGORY, SAVED_MESSAGE_CONTENT}

	@Override
	public List<String> findSavedMessageRefByType(String type) {
		List<String> messageRefList = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();				
		try {
			SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
			queryBuilder.setTables(HabryTables.SAVED_MESSAGE.name());
			Cursor cursor = null;
			if (type == null || "".equalsIgnoreCase(type)) {
				cursor = queryBuilder.query(db, null, null, new String[]{type}, null, null, null);
			}
			else {
				cursor = queryBuilder.query(db, new String[]{"REFERENCE"}, "TYPE=?", new String[]{type}, null, null, null);
			}
			if (cursor.getCount() > 0) {	
				cursor.moveToFirst();
				boolean hasMore = true;
				do {
					messageRefList.add(cursor.getString(cursor.getColumnIndexOrThrow("REFERENCE")));
					hasMore = cursor.moveToNext();
				} while (hasMore);			
			}			
		} catch (Exception e) {
			Log.e("habreader", "Cannot read message from DB. " + e );			
		} 
		return messageRefList;
	};

}
