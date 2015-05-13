package com.rememberwords;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.rememberwords.dbhelper.DBHelper;
import com.w1.R;

public class SomeSettingsActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	
	private SharedPreferences sp;
	private boolean chState = false;
	private final String CHECK_FILE = "checkFile";
	private final String CHECK_STATE = "checkState";
	
	private CheckBox chbLastWord;
	private Button btnWriteDBOnSD, btnReadDBOnSD, btnDeleteAll;
	private DBHelper dbhelper;
	private ProgressDialog progressDlg;
	private Handler handler;
	
	private Handler.Callback handlerCallback = new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			Toast.makeText(SomeSettingsActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
	};

	private void initViews() {
		chbLastWord = (CheckBox) findViewById(R.id.chbLastWord);
		
		btnWriteDBOnSD = (Button) findViewById(R.id.btnWriteDBOnSD);
		btnReadDBOnSD = (Button) findViewById(R.id.btnReadDBFromSD);
		btnDeleteAll = (Button) findViewById(R.id.btnDeleteAll);
		
		dbhelper = DBHelper.getMyDatabase(SomeSettingsActivity.this);
		
		handler = new Handler(handlerCallback);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_some_settings);
		
		initViews();
		
		btnWriteDBOnSD.setOnClickListener(this);
		btnReadDBOnSD.setOnClickListener(this);
		btnDeleteAll.setOnClickListener(this);
		chbLastWord.setOnCheckedChangeListener(this);
		
		loadSettings();
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnWriteDBOnSD:
			progressDlg = new ProgressDialog(SomeSettingsActivity.this);
			progressDlg.setMessage("Пожалуйста, подождите...");
			progressDlg.show();
			Thread treadWrite = new Thread(new Runnable() {
				
				@Override
				public void run() {
					String resultWrite = dbhelper.writeDBOnSD();
					
					Message ma = new Message();
					ma.obj = resultWrite;
					ma.setTarget(handler);
					ma.sendToTarget();
					
					progressDlg.dismiss();
				}
			});
			treadWrite.start();
			break;
			
		case R.id.btnReadDBFromSD:
			progressDlg = new ProgressDialog(SomeSettingsActivity.this);
			progressDlg.setMessage("Пожалуйста, подождите...");
			progressDlg.show();
			Thread treadRead = new Thread(new Runnable() {
				
				@Override
				public void run() {
					if (!dbhelper.isTableExist(DBHelper.TABLE_NAME)) {
						dbhelper.createTable();
					}
					String resultRead = dbhelper.readDBFromSD();
					
					Message ma = new Message();
					ma.obj = resultRead;
					ma.setTarget(handler);
					ma.sendToTarget();
					
					progressDlg.dismiss();
				}
			});
			treadRead.start();
			break;
			
		case R.id.btnDeleteAll:
			progressDlg = new ProgressDialog(SomeSettingsActivity.this);
			progressDlg.setMessage("Пожалуйста, подождите...");
			progressDlg.show();
			Thread treadDel = new Thread(new Runnable() {
				
				@Override
				public void run() {
					dbhelper.deleteAll();
					
					Message ma = new Message();
					ma.obj = "Завершено.";
					ma.setTarget(handler);
					ma.sendToTarget();
					
					progressDlg.dismiss();
				}
			});
			treadDel.start();
			break;
			
		}
		
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (chbLastWord.isChecked()) {
			chState = true;
			saveSettings();
		} else {
			chState = false;
			saveSettings();
		}
	}
	
	private void saveSettings() {
		sp = getSharedPreferences(CHECK_FILE, MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putBoolean(CHECK_STATE, chState);
		ed.commit();
	}

	public void loadSettings() {
		sp = getSharedPreferences(CHECK_FILE, MODE_PRIVATE);
		if (sp.getBoolean(CHECK_STATE, false)) {
			chbLastWord.setChecked(true);
		} else {
			chbLastWord.setChecked(false);
		}
	}
	
	@Override
	protected void onResume() {
		loadSettings();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		saveSettings();
		super.onDestroy();
	}
	
}
