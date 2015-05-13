package com.rememberwords;

import java.util.ArrayList;

import com.rememberwords.adapter.ListAdapter;
import com.w1.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ListActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	private ListView lvWordsList;
	private Button btnFormEn, btnFormEnRus, btnFormRus;
	private EditText etSearch;
	private ProgressBar pbSearch;
	private ArrayList<String> wordsList, translationsList, findingList;
	private Intent intent;
	private ListAdapter adapter;
	
	
	
	private void initViews() {
		lvWordsList = (ListView) findViewById(R.id.lvWordsList);
		
		btnFormEn = (Button) findViewById(R.id.btnFormEn);
		btnFormEnRus = (Button) findViewById(R.id.btnFormEnRus);
		btnFormRus = (Button) findViewById(R.id.btnFormRus);
		
		etSearch = (EditText) findViewById(R.id.etSearch);
		pbSearch = (ProgressBar) findViewById(R.id.pbSearch);
		
		intent = getIntent();
		
		wordsList = intent.getExtras().getStringArrayList("words_list");
		translationsList = intent.getExtras().getStringArrayList("translation_list");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		initViews();
		
		btnFormEn.setOnClickListener(this);
		btnFormEnRus.setOnClickListener(this);
		btnFormRus.setOnClickListener(this);		
		lvWordsList.setOnItemClickListener(this);
		
		adapter = new ListAdapter(ListActivity.this, wordsList, translationsList);
		lvWordsList.setAdapter(adapter);
		
		
		
		etSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				findingList = find(etSearch.getText().toString());
				ArrayAdapter<String> findListAdapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, findingList);
				
				if (TextUtils.isEmpty(etSearch.getText().toString())) {
					lvWordsList.setAdapter(adapter);
				} else {
					lvWordsList.setAdapter(findListAdapter);
				}
				
			}
		});
	}
	
	//Find and show words from wordsList
	private ArrayList<String> find(String findString) {
		ArrayList<String> foundStrings = new ArrayList<String>();
		
		pbSearch.setVisibility(View.VISIBLE);
		int length = findString.length();
		int counter = 0;
		for (String item : wordsList) {
			if (item.length() >= length) {
				String equil = item.substring(0, length);
				if (equil.equalsIgnoreCase(findString)) {
					foundStrings.add(item);
					counter++;
				}
			}
		}
		if (counter == 0) {
			for (String item : translationsList) {
				if (item.length() >= length) {
					String equil = item.substring(0, length);
					if (equil.equalsIgnoreCase(findString)) {
						foundStrings.add(item);
					}
				}
			}
		}
		pbSearch.setVisibility(View.GONE);
		
		return foundStrings;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.btnFormEn:
			adapter.setType("en");
			adapter.notifyDataSetChanged();
			break;
			
		case R.id.btnFormEnRus:
			adapter.setType("en_rus");
			adapter.notifyDataSetChanged();
			break;
			
		case R.id.btnFormRus:
			adapter.setType("rus");
			adapter.notifyDataSetChanged();
			break;
			
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		intent.putExtra("from_list", position);
		setResult(RESULT_OK, intent);
		finish();
	}

}
