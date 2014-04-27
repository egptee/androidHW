package com.example.android.searchabledict;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddWord extends Activity {
	
	private Button submit_btn;
	private EditText word;
	private EditText definition;
	
	private String cur_word;
	private String cur_definition;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addword);
		
		word = (EditText)findViewById(R.id.edit_word);
		definition = (EditText)findViewById(R.id.edit_definition);
		
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		cur_word = intent.getStringExtra("word");
		cur_definition = intent.getStringExtra("definition");
		
		if(type.equals("EDIT")){
			word.setText(cur_word);
			word.setEnabled(false);
			definition.setText(cur_definition);
		}
		
		submit_btn = (Button)findViewById(R.id.submit);
		submit_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String str_word = word.getText().toString();
				String str_definition = definition.getText().toString();
				ContentResolver contentResolver = getContentResolver();
				ContentValues values = new ContentValues();
				values.put(DictionaryDatabase.KEY_WORD, str_word);
				values.put(DictionaryDatabase.KEY_DEFINITION, str_definition);
				Log.i("Insert Word", "Start" +" "+str_word+" "+str_definition);
				if(type.equals("ADD")){
					if(contentResolver.insert(DictionaryProvider.CONTENT_URI, values) != null){
						AlertDialog.Builder show = new Builder(AddWord.this);
						show.setTitle("Prompt");
						show.setMessage("Add Success!")
								.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										finish();
									}
								}).show();
					}
				}
				
				else{
					if(contentResolver.update(DictionaryProvider.CONTENT_URI, values, null, null) == 1){
						AlertDialog.Builder show = new Builder(AddWord.this);
						show.setTitle("Prompt");
						show.setMessage("Update Success!")
								.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// TODO Auto-generated method stub
										finish();
									}
								}).show();
					}
				}
			}
			
		});
	}

}
