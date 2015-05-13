package com.rememberwords;

import com.rememberwords.dbhelper.DBHelper;
import com.w1.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FormActivity extends Activity implements OnClickListener{
	
	private Button btnFormOK, btnFormCancel;
	private EditText etFormWord, etFormDescription, etFormTranslation;
	private DBHelper dbhelper = DBHelper.getMyDatabase(FormActivity.this);
	private Intent intent;
	private String key, wordTranslation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);
		
		initViews();
		
		intent = getIntent();
		
		btnFormOK.setOnClickListener(this);
		btnFormCancel.setOnClickListener(this);
		
		key = intent.getExtras().getString("word_key");
		if (key.equalsIgnoreCase("edit")) {
			
			wordTranslation = intent.getExtras().getString("word_translation");
			
			etFormWord.setText(dbhelper.getWord(wordTranslation));
			etFormTranslation.setText(dbhelper.getTranslation(wordTranslation));			
			etFormDescription.setText(dbhelper.getDescription(wordTranslation));
		}
		
	}
	
	@Override
	public void onClick(View v) {
		
	    switch (v.getId()) {
	    
	    case R.id.btnFormOK:
	        String word_form = etFormWord.getText().toString();
	        String translation_form = etFormTranslation.getText().toString();
	        String description_form = etFormDescription.getText().toString();
	        
	        if (TextUtils.isEmpty(word_form)) {
	        	word_form = " ";
	        }
	        if (TextUtils.isEmpty(translation_form)) {
	        	translation_form = " ";
	        }
	        if (TextUtils.isEmpty(description_form)) {
	        	description_form = " ";
	        }
	        
	        if (key.equalsIgnoreCase("add")) {
	        	dbhelper.addItem(word_form, translation_form, description_form);
	        } else {
	        	dbhelper.rewrite(wordTranslation, word_form, translation_form, description_form);
	        }
	        intent.putExtra("new_word", word_form);
	        intent.putExtra("new_translation", translation_form);
	        intent.putExtra("new_description", description_form);
	        setResult(RESULT_OK, intent);
	        break;
	        
	    case R.id.btnFormCancel:
	    	setResult(RESULT_CANCELED, intent);
	        break;
	        
	    }
	    
	    finish();
		
	}
	
	private void initViews() {
		
		btnFormOK = (Button) findViewById(R.id.btnFormOK);
		btnFormCancel = (Button) findViewById(R.id.btnFormCancel);
		
		etFormWord = (EditText) findViewById(R.id.etFormWord);
		etFormTranslation = (EditText) findViewById(R.id.etFormTranslation);
		etFormDescription = (EditText) findViewById(R.id.etFormDescription);
	}
	
}
