package com.example.android.searchabledict;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class AddWordActivity extends Activity {

	private EditText wordText;
    private EditText definitionText;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addword);
		//EditText word = (EditText) findViewById(R.id.word);
		//EditText definition = (EditText) findViewById(R.id.definition);
		
	}
	
    public void addWord(){
    	Intent addIntent = new Intent(this,AddWordActivity.class);  
    	startActivity(addIntent); 
    }
	
	public void saveWord(View view){
		EditText wordT = (EditText) findViewById(R.id.word);
		EditText definitionT = (EditText) findViewById(R.id.definition);
		Log.i("AddWordActivity", "AddWordActivity");
		String word = wordT.getText().toString();
		String definition = definitionT.getText().toString();
		DictionaryProvider dictionary = new DictionaryProvider();
		Log.i("word", word);
		Log.i("de", definition);
		//Uri uri =  Uri.parse("content://" + "com.example.android.searchabledict.DictionaryProvider" + "/dictionary");
		ContentValues values = new ContentValues();
		ContentResolver contentResolver = getContentResolver();
		values.put(DictionaryDatabase.KEY_WORD, word);
		values.put(DictionaryDatabase.KEY_DEFINITION, definition);
		if(contentResolver.insert(DictionaryProvider.CONTENT_URI, values) != null){
			AlertDialog.Builder show = new Builder(AddWordActivity.this);
			show.setTitle("Tips:");
			show.setMessage("Congratulation! Add Success!")
					.setPositiveButton("Return", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
							//finish();
						}
					}).show();
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
            case android.R.id.home:
                Intent intent = new Intent(this, SearchableDictionary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.add:
            	addWord();
            	return true;
            default:
                return false;
        }
    }


}
