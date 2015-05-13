package com.rememberwords;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rememberwords.dbhelper.DBHelper;
import com.w1.R;

public class WordsActivity extends Activity implements OnClickListener {
	
	private Button btnWordDescription, btnWordsList;
	private ImageButton imgBtnBack, imgBtnForward;
	private TextView tvWordNumber, tvWord, tvDescription;
	private LinearLayout layout_word;
	
	private ArrayList<String> wordsList;
	private ArrayList<String> descriptionList;
	private ArrayList<String> translationList;
	
	private int index;
	private boolean translate = true;
	private int toShow = 1;
	
	private DBHelper dbhelper;
	
	private SharedPreferences sp;
	private final String CHECK_FILE = "checkFile";
	private final String CHECK_STATE = "checkState";
	private final String INDEX_VALUE = "indexValue";
	
	private void initViews() {
		btnWordsList = (Button) findViewById(R.id.btnWordsList);
		btnWordDescription = (Button) findViewById(R.id.btnWordDescription);
		
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);
		imgBtnForward = (ImageButton) findViewById(R.id.imgBtnForward);
		
		tvWordNumber = (TextView) findViewById(R.id.tvWordNumber);
		tvWord = (TextView) findViewById(R.id.tvElement);
		tvDescription = (TextView) findViewById(R.id.tvWordDescription);
		
		layout_word = (LinearLayout) findViewById(R.id.layout_word);
		
		wordsList = new ArrayList<String>();
		descriptionList = new ArrayList<String>();
		translationList = new ArrayList<String>();
		
		dbhelper = DBHelper.getMyDatabase(WordsActivity.this);
		
		index = loadIndex();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_words);
		
		initViews();
		
		btnWordDescription.setOnClickListener(this);
		btnWordsList.setOnClickListener(this);
		imgBtnBack.setOnClickListener(this);
		imgBtnForward.setOnClickListener(this);
		layout_word.setOnClickListener(this);
		tvWord.setOnClickListener(this);
		
		
		if (dbhelper.isTableExist(DBHelper.TABLE_NAME)) {
			if (dbhelper.isEmpty(DBHelper.TABLE_NAME)) {
				dbhelper.addItem("Hello", "Привет", "Приветствие");
			}
		} else {
			dbhelper.createTable();
			dbhelper.addItem("Hello", "Привет", "Приветствие");
		}
		
		wordsList = dbhelper.getAllWords();
		translationList = dbhelper.getAllTranslations();
		descriptionList = dbhelper.getAllDescriptions();
		
		
		
		if (sp.getBoolean(CHECK_STATE, false)) {
			if (index >= wordsList.size()) {
				index = 0;
			}
		} else {
			index = 0;
		}
		
		tvWordNumber.setText("" + (index + 1));
		tvWord.setText(wordsList.get(index));
		
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		case R.id.btnWordsList:
			Intent intentList = new Intent(this, ListActivity.class);
			intentList.putExtra("words_list", wordsList);
			intentList.putExtra("translation_list", translationList);
			startActivityForResult(intentList, 30);
			break;
			
		case R.id.imgBtnBack:
			getPrevious();
			break;
			
		case R.id.layout_word:
			if (translate) {
				tvWord.setText(dbhelper.getTranslation(wordsList.get(index)));
				tvDescription.setText("");
				translate = false;
				toShow = 1;
			} else {
				tvWord.setText(dbhelper.getWord(translationList.get(index)));
				translate = true;
				toShow = 0;
			}
			break;
			
		case R.id.imgBtnForward:
			getNext();
			break;
			
		case R.id.btnWordDescription:
			String descr = dbhelper.getDescription(wordsList.get(index));
			if (toShow == 1) {
				tvDescription.setText(descr);
				toShow = 0;
			} else {
				tvDescription.setText("");
				toShow = 1;
			}
			break;
			
		case R.id.tvElement:
			if (translate) {
				tvWord.setText(dbhelper.getTranslation(wordsList.get(index)));
				tvDescription.setText("");
				translate = false;
				toShow = 1;
			} else {
				tvWord.setText(dbhelper.getWord(translationList.get(index)));
				translate = true;
				toShow = 0;
			}
			break;
		}
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if(resultCode == RESULT_OK && data.getExtras().containsKey("new_word")) {
        	
        	String word = data.getExtras().getString("new_word");
        	String description = data.getExtras().getString("new_description");
        	String translation = data.getExtras().getString("new_translation");
        	
	        if (requestCode == 10) {
        		wordsList.add(word);
        		descriptionList.add(description);
        		translationList.add(translation);
    			Toast.makeText(WordsActivity.this, "Добавлено", Toast.LENGTH_SHORT).show();
        	} else {
        		wordsList.set(index, word);
        		descriptionList.set(index, description);
        		translationList.set(index, translation);
        		tvWord.setText(word);
        		tvDescription.setText(description);
        		Toast.makeText(WordsActivity.this, "Готово", Toast.LENGTH_SHORT).show();
        	}
            
        }
        
        if(resultCode == RESULT_OK && data.getExtras().containsKey("from_list")) {
        	index = data.getExtras().getInt("from_list");
        	tvWordNumber.setText("" + (index + 1));
        	tvWord.setText(wordsList.get(index));
        }
    }
	
	//***************************************
	private void getPrevious() {
		index--;
		if (index < 0) {
			index = wordsList.size() - 1;
		}
		tvWordNumber.setText("" + (index + 1));
		tvWord.setText(wordsList.get(index));
		tvDescription.setText("");
		translate = true;
		toShow = 1;
	}
	
	private void getNext() {
		index++;
		if (index == wordsList.size()) {
			index = 0;
		}
		tvWordNumber.setText("" + (index + 1));
		tvWord.setText(wordsList.get(index));
		tvDescription.setText("");
		translate = true;
		toShow = 1;
	}
	//****************************************
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//       gr id order title
		menu.add(0, 1, 1, R.string.edit_description);
		menu.add(0, 2, 2, R.string.delete_word);
		menu.add(0, 3, 3, R.string.add_word);
		menu.add(0, 4, 4, R.string.amount);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();
		switch (item_id) {
		case 1: //Edit word
			Intent intentEdit = new Intent(this, FormActivity.class);
			intentEdit.putExtra("word_key", "edit");
			intentEdit.putExtra("word_translation", tvWord.getText().toString());
			startActivityForResult(intentEdit, 20);
			break;
		case 2: //Delete word
			boolean condition = dbhelper.deleteItem(index);
			if (condition) {
				wordsList.remove(index);
				translationList.remove(index);
				descriptionList.remove(index);
				getPrevious();
				Toast.makeText(WordsActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(WordsActivity.this, "Не может быть удалено.", Toast.LENGTH_SHORT).show();
			}
			break;
		case 3: //Add word
			Intent intentAddWord = new Intent(this, FormActivity.class);
			intentAddWord.putExtra("word_key", "add");
			startActivityForResult(intentAddWord, 10);
			break;
		case 4: //Common amount of words
			Toast.makeText(WordsActivity.this, "Общее количество слов = " + dbhelper.getAmount(), Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Activity state *************************************************
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		index = savedInstanceState.getInt("index");
		translate = savedInstanceState.getBoolean("translate");
		toShow = savedInstanceState.getInt("to_show");
		tvWordNumber.setText("" + (index + 1));
		tvWord.setText(wordsList.get(index));
		if (toShow == 0) {
			tvDescription.setText(descriptionList.get(index));
		} else {
			tvDescription.setText("");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("index", index);
		outState.putBoolean("translate", translate);
		outState.putInt("to_show", toShow);
	}
	//*****************************************************************
	
	private void saveIndex(int ind) {
		sp = getSharedPreferences(CHECK_FILE, MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putInt(INDEX_VALUE, ind);
		ed.commit();
	}
	
	private int loadIndex() {
		sp = getSharedPreferences(CHECK_FILE, MODE_PRIVATE);
		return sp.getInt(INDEX_VALUE, 0);
	}
	
	@Override
	protected void onDestroy() {
		if (sp.getBoolean(CHECK_STATE, false)) {
			saveIndex(index);
		}
		super.onDestroy();
	}
	
	
	
}
