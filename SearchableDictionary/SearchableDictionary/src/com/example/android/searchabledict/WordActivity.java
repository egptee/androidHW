/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.searchabledict;

import android.app.Activity;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Displays a word and its definition.
 */
public class WordActivity extends Activity implements OnClickListener{
	
	private Button edit_word;
	private Button delete_word;
	private String cur_word;
	private String cur_definition;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        edit_word = (Button)findViewById(R.id.change);
        delete_word = (Button)findViewById(R.id.delete);
        
        edit_word.setOnClickListener(this);
        delete_word.setOnClickListener(this);

        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (cursor == null) {
            finish();
        } else {
            cursor.moveToFirst();

            TextView word = (TextView) findViewById(R.id.word);
            TextView definition = (TextView) findViewById(R.id.definition);

            int wIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.KEY_WORD);
            int dIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.KEY_DEFINITION);
            
            cur_word = cursor.getString(wIndex);
            cur_definition = cursor.getString(dIndex);

            word.setText(cursor.getString(wIndex));
            definition.setText(cursor.getString(dIndex));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            case R.id.add_word:
            	Intent it = new Intent(WordActivity.this, AddWord.class);
            	it.putExtra("type", "ADD");
                startActivity(it);
            case android.R.id.home:
                Intent intent = new Intent(this, SearchableDictionary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

	@Override
	public void onClick(View target) {
		// TODO Auto-generated method stub
		if(target == edit_word){
			Intent it = new Intent(WordActivity.this, AddWord.class);
        	it.putExtra("type", "EDIT");
        	it.putExtra("word", cur_word);
        	it.putExtra("definition", cur_definition);
            startActivity(it);
		}
		if(target == delete_word){
			ContentResolver contentResolver = getContentResolver();
			contentResolver.delete(DictionaryProvider.CONTENT_URI, cur_word, null);
			Intent it = new Intent(WordActivity.this, SearchableDictionary.class);
			startActivity(it);
		}
	}
}
