package com.example.simpletodolist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

@SuppressLint("SimpleDateFormat")
public class ToDoActivity extends Activity {

	private ArrayList<Item> items;
	private static ToDoAdapter adapter;
	private ListView lvItems;
	public final int REQUEST_CODE = 20;

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
		items = new ArrayList<Item>();
		readItems();
		adapter = new ToDoAdapter(this, items);
		lvItems = (ListView) findViewById(R.id.lvItems);
		lvItems.setAdapter(adapter);
	}

	// after new edited text is returned
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			items = data.getParcelableArrayListExtra("items_s");
			adapter.notifyDataSetChanged();
			lvItems.setAdapter(adapter);
			saveItems();
			readItems();
			populateItemsList();
			startActivity(data);
		}
	}

	// gets called when Add button is called on main activity
	public void addTodoItem(View v) {
		EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
		String newItemText = etNewItem.getText().toString();
		Item item = new Item(newItemText.trim(), "");
		if (newItemText.length() > 0) {
			adapter.add(item);
			etNewItem.setText("");
			saveItems();
		} else {
			showErrorDialogForEmptyInput();
		}
	}

	public AlertDialog showErrorDialogForEmptyInput() {
		return new AlertDialog.Builder(this)
				.setTitle(ERROR_EMPTY_TITLE)
				.setMessage(ERROR_EMPTY_MSG)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// return to screen- do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
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
		i.putParcelableArrayListExtra("items", items);
		i.putExtra("position", position);
		startActivityForResult(i, REQUEST_CODE);
	}

	// reads items from todo.txt
	private void readItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			ArrayList<String> todos = (ArrayList<String>) FileUtils.readLines(todoFile);
			for (String todo : todos) {
				String[] toSplit = todo.split(",");

				if (toSplit.length > 1) {
					items.add(new Item(toSplit[0], toSplit[1]));
				} else {
					items.add(new Item(toSplit[0], ""));
				}
			}
		} catch (IOException ex) {
			items = new ArrayList<Item>();
			ex.printStackTrace();
		}
	}

	// saves items to todo.txt
	private void saveItems() {
		File filesDir = getFilesDir();
		File todoFile = new File(filesDir, "todo.txt");
		try {
			ArrayList<String> savedTodos = new ArrayList<String>();
			for (Item todo : items) {
				savedTodos.add(todo.getItemName() + "," + todo.getDueDate());
			}
			FileUtils.writeLines(todoFile, savedTodos);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
