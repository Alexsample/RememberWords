package com.rememberwords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rememberwords.dbhelper.DBHelper;
import com.w1.R;

public class MainMenuActivity extends Activity implements OnClickListener{
	
	private Button btnWords, btnSettings, btnExit;
	private DBHelper dbhelper;
	
	private void initViews() {
		btnWords = (Button) findViewById(R.id.btnWords);
		btnSettings = (Button) findViewById(R.id.btnSettings);
		btnExit = (Button) findViewById(R.id.btnExit);
		
		dbhelper = new DBHelper(MainMenuActivity.this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		initViews();
		
		btnWords.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		
		dbhelper.createTable();
		
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		
		case R.id.btnWords:
			intent = new Intent(MainMenuActivity.this, WordsActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btnSettings:
			Intent intentSomeSettings = new Intent(MainMenuActivity.this, SomeSettingsActivity.class);
			startActivity(intentSomeSettings);
			break;
			
		case R.id.btnExit:
			finish();
			break;
			
		}
		
	}
	
}
