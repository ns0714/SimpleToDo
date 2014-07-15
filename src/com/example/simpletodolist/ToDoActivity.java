package com.example.simpletodolist;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ToDoActivity extends Activity {

	private ArrayList<String> items;
	private static ToDoAdapter adapter;
	private ListView lvItems;
	public final int REQUEST_CODE = 20;
	public final int REQUEST_CODE_1 = 25;
	
	public final String ERROR_EMPTY_TITLE = "ERROR";
	public final String ERROR_EMPTY_MSG = "Please enter a ToDo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		populateItemsList();
		setUpListViewListener();
	}

	private void populateItemsList() {
		items = new ArrayList<String>();
		readItems();
		adapter = new ToDoAdapter(this, items);
		lvItems = (ListView) findViewById(R.id.lvItems);
		lvItems.setAdapter(adapter);
	}

	// after new edited text is returned
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			items = data.getStringArrayListExtra("items_s");
			long dateInMillis = data.getLongExtra("date", 0L);

			TextView dueDate = (TextView) findViewById(R.id.dueDate);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dateString = formatter.format(new Date(dateInMillis));
			dueDate.setText(dateString);

			adapter.notifyDataSetChanged();
			lvItems.setAdapter(adapter);
			saveItems();
			readItems();
			startActivity(data);
		}
	}

	// gets called when Add button is called on main activity
	public void addTodoItem(View v) {
		EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
		String addText = etNewItem.getText().toString();

		if (addText.length() > 0) {
			adapter.add(addText.trim());
			etNewItem.setText("");
			saveItems();
		}else{
			showErrorDialogForEmptyInput();
		}
	}
	
	public AlertDialog showErrorDialogForEmptyInput(){
		return new AlertDialog.Builder(this)
	    .setTitle(ERROR_EMPTY_TITLE)
	    .setMessage(ERROR_EMPTY_MSG)
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            //return to screen- do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	}

	// gets called after long click -items gets removed
	private void setUpListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long rowId) {
				// TODO Auto-generated method stub
				items.remove(position);
				adapter.notifyDataSetChanged();
				saveItems();
				return true;
			}
		});
		setUpEditItemListener();
	}

	// gets called after single click-takes to edit todo list
	private void setUpEditItemListener() {
		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				openClickItem(position, id);
			}
		});
	}

	// passes intent from ToDoActivity to EditItemActivity
	public void openClickItem(int position, long rowId) {

		Intent i = new Intent(ToDoActivity.this, EditItemActivity.class);
		i.putStringArrayListExtra("items", items);
		i.putExtra("position", position);
		i.putExtra("itemValue", items.get(position));
		startActivityForResult(i, REQUEST_CODE);
	}

	// reads items from todo.txt
	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException ex) {
			items = new ArrayList<String>();
			ex.printStackTrace();
		}
	}

	// saves items to todo.txt
	private void saveItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
