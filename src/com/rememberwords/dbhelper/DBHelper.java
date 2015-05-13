package com.rememberwords.dbhelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Xml.Encoding;

public class DBHelper extends SQLiteOpenHelper {
	
	public static DBHelper mysql;
	private static String dbName = "myDataBase";
	private static int dbVersion = 1;
	
	public static final String TABLE_NAME = "myTable";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_WORDS = "words";
	public static final String COLUMN_TRANSLATION = "translation";
	public static final String COLUMN_DESCRIPTION = "description";
	
	private static final String FILE_PATH = "WordsDir";
	private static final String FILE_NAME = "WordsDB.txt";
	
	
	
	public DBHelper(Context c) {
		super(c, dbName, null, dbVersion);
	}
	
	public static DBHelper getMyDatabase(Context c) {
		if (mysql == null) {
			mysql = new DBHelper(c);
		}
		return mysql;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + TABLE_NAME + " ("
		          + COLUMN_ID + " integer primary key autoincrement," 
		          + COLUMN_WORDS + " text,"
		          + COLUMN_TRANSLATION + " text," 
		          + COLUMN_DESCRIPTION + " text" + ");"
		          );
	}
	
	public void createTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("create table if not exists " + TABLE_NAME + " ("
		          + COLUMN_ID + " integer primary key autoincrement," 
		          + COLUMN_WORDS + " text,"
		          + COLUMN_TRANSLATION + " text," 
		          + COLUMN_DESCRIPTION + " text" + ");"
		          );
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}
	
	//*******************************************************************************
	
	public boolean isTableExist(String tableName) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
		if (!c.moveToFirst()) {
			return false;
		}
		int count = c.getInt(0);
		c.close();
		db.close();
		return count > 0;
	}
		
	public boolean isEmpty(String tableName) {
		boolean empty;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query(tableName, null, null, null, null, null, null);
			
		if (c.moveToFirst()) {
			empty = false;
		} else {
			empty = true;
		}
		c.close();
		db.close();
			
		return empty;
	}
		
	//Add item into DB ********************************************************
	public void addItem(String word, String translation, String description) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_WORDS, word);
		cv.put(COLUMN_TRANSLATION, translation);
		cv.put(COLUMN_DESCRIPTION, description);
		db.insert(TABLE_NAME, null, cv);
		db.close();
	}
	
	//Get one word from DB ********************************************************
	public String getWord(String translation) {
		String item = "";
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT " + COLUMN_WORDS 
						+ " FROM " + TABLE_NAME 
						+ " WHERE " + COLUMN_TRANSLATION + " = ?";
		Cursor c = db.rawQuery(sql, new String[] {translation});
		if (c.getCount() != 0) {
			if (c.moveToFirst()) {
				item = c.getString(c.getColumnIndex(COLUMN_WORDS));
			}
		} else {
			item = translation;
		}
		c.close();
		db.close();
		return item;
	}
	
	//Get all words from DB ***************************************************
	public ArrayList<String> getAllWords() {
		ArrayList<String> al = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT " + COLUMN_WORDS 
				+ " FROM " + TABLE_NAME ;
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() != 0) {
			if (c.moveToFirst()) {
				do {
					al.add(c.getString(c.getColumnIndex(COLUMN_WORDS)));
				} while (c.moveToNext());
			}
		} else {
		}
		c.close();
		db.close();
		return al;
	}
	
	//Get one translation from DB *************************************************
	public String getTranslation(String word) {
		String item = "";
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT " + COLUMN_TRANSLATION 
						+ " FROM " + TABLE_NAME 
						+ " WHERE " + COLUMN_WORDS + " = ?";
		Cursor c = db.rawQuery(sql, new String[] {word});
		if (c.getCount() != 0) {
			if (c.moveToFirst()) {
				item = c.getString(c.getColumnIndex(COLUMN_TRANSLATION));
			}
		} else {
			item = word;
		}
		c.close();
		db.close();
		return item;
	}
	
	//Get all translations from DB ********************************************
	public ArrayList<String> getAllTranslations() {
		ArrayList<String> al = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT " + COLUMN_TRANSLATION 
				+ " FROM " + TABLE_NAME ;
		Cursor c = db.rawQuery(sql, null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					al.add(c.getString(c.getColumnIndex(COLUMN_TRANSLATION)));
				} while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		return al;
	}
	
	//Get one description from DB *************************************************
	public String getDescription(String word) {
		String item = "";
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT " + COLUMN_DESCRIPTION 
						+ " FROM " + TABLE_NAME 
						+ " WHERE " + COLUMN_WORDS + " = ?";
		Cursor c = db.rawQuery(sql, new String[] {word});
		if (c.getCount() != 0) {
			if (c.moveToFirst()) {
				item = c.getString(c.getColumnIndex(COLUMN_DESCRIPTION));
			}
		} else {
			item = word;
		}
		c.close();
		db.close();
		return item;
	}
	
	//Get all descriptions from DB ********************************************
	public ArrayList<String> getAllDescriptions() {
		ArrayList<String> al = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT " + COLUMN_DESCRIPTION 
				+ " FROM " + TABLE_NAME ;
		Cursor c = db.rawQuery(sql, null);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					al.add(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
				} while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		return al;
	}
	
	//Rewrite word ************************************************************
	public void rewrite(String old_word_translation, String new_word, String new_translation, String new_description) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT " + COLUMN_ID 
				+ " FROM " + TABLE_NAME 
				+ " WHERE " + COLUMN_WORDS + " = ?";
		Cursor c = db.rawQuery(sql, new String[] {old_word_translation});
		if (c.getCount() != 0) {
			if (c.moveToFirst()) {
				String id = c.getString(c.getColumnIndex(COLUMN_ID));
				ContentValues cv = new ContentValues();
				cv.put(COLUMN_WORDS, new_word);
				cv.put(COLUMN_TRANSLATION, new_translation);
				cv.put(COLUMN_DESCRIPTION, new_description);
				db.update(TABLE_NAME, cv, COLUMN_ID + " = " + id, null);			
			}
		}
		c.close();
		db.close();
	}
	
	//Delete ONE row from DB ***************************************************
	public boolean deleteItem(int number) {
		boolean answer = false;		
		if (this.getAmount() > 1) {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
			if (c.getCount() != 0) {
				if (c.moveToFirst()) {
					do {
						c.moveToNext();
						number--;
					} while (number != 0);
					String id = c.getString(c.getColumnIndex(COLUMN_ID));
					db.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
					answer = true;
				}
			}
			c.close();
			db.close();
		} else {
			answer = false;
		}
		return answer;
	}
	
	//Delete all words from db **************************************************
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}
	
	//Get amount of all words
	public int getAmount() {
		int count = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		count = c.getCount();
		c.close();
		db.close();
		return count;
	}
	
	
	
	//Write DB on SD card ********************************************************
	public String writeDBOnSD() {
		String s = "";
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			s = "SD карта недоступна.";
			return s;
		}
		
		File sdPath = Environment.getExternalStorageDirectory();
		sdPath = new File(sdPath.getAbsolutePath() + "/" + FILE_PATH);
		sdPath.mkdir();
		
		File sdFile = new File(sdPath, FILE_NAME);
		try {
			ArrayList<String> massWords = this.getAllWords();
			ArrayList<String> massTranslations = this.getAllTranslations();
			ArrayList<String> massDescriptions = this.getAllDescriptions();
			BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
			for (int counter = 0; counter < this.getAmount(); counter++) {
				String str = massWords.get(counter) + "_" 
						+ massTranslations.get(counter) + "_" 
						+ massDescriptions.get(counter);
				bw.write(str + "\n");
			}
			bw.close();
			s = "Файл записан.";
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			s = "Файл не записан.";
		}
		return s;
	}
	
	//Read DB from SD card ********************************************************
	public String readDBFromSD() {
		String s = "";
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			s = "SD карта недоступна.";
			return s;
		}
		
		File sdPath = Environment.getExternalStorageDirectory();
		sdPath = new File(sdPath.getAbsolutePath() + "/" + FILE_PATH);
		
		File sdFile = new File(sdPath, FILE_NAME);
		if (sdFile.exists()) {
			try {
				InputStreamReader isr = new InputStreamReader(new FileInputStream(sdFile), Encoding.UTF_8.toString());
				BufferedReader br = new BufferedReader(isr);
				String str = "";
				while ((str = br.readLine()) != null) {
				
					String[] oneline = str.split("_");
				
					for (int i = 0; i < oneline.length; i++) {
						if (TextUtils.isEmpty(oneline[i])) {
							oneline[i] = " ";
						}
					}
				
					SQLiteDatabase db = this.getWritableDatabase();
					ContentValues cv = new ContentValues();
					cv.put(COLUMN_WORDS, oneline[0]);
					cv.put(COLUMN_TRANSLATION, oneline[1]);
					cv.put(COLUMN_DESCRIPTION, oneline[2]);
					db.insert(TABLE_NAME, null, cv);
					db.close();
				}
				br.close();
				s = "Файл прочитан.";
				return s;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				s = "Файл не прочитан.";
			} catch (IOException e) {
				e.printStackTrace();
				s = "Файл не прочитан.";
			}
		} else {
			s = "Файл не существует.";
		}
		return s;
	}
	
	
	
}
