package com.example.simpletodolist;

import java.util.ArrayList;
import java.util.List;

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
	private MySqlLiteHelper db;

	public final int REQUEST_CODE = 20;
	public final String ERROR_EMPTY_TITLE = "ERROR";
	public final String ERROR_EMPTY_MSG = "Please enter a ToDo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		db = new MySqlLiteHelper(this);
		populateItemsList();
		setUpListViewListener();
	}

	// displays items
	private void populateItemsList() {
		items = new ArrayList<Item>();
		readItems();
		adapter = new ToDoAdapter(this, items);
		lvItems = (ListView) findViewById(R.id.lvItems);
		lvItems.setAdapter(adapter);
	}

	// gets called when Add button is clicked
	public void addTodoItem(View v) {
		EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
		String newItemText = etNewItem.getText().toString();
		Item item = new Item(newItemText.trim(), "");
		if (newItemText.length() > 0) {
			adapter.add(item);
			etNewItem.setText("");
			saveItem(item);
			readItems();
		} else {
			showErrorDialogForEmptyInput();
		}
	}

	// error message when nothing is entered and Add is clicked
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

	// gets called after long click -delete item from db and list
	private void setUpListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long rowId) {
				db.deleteItem(items.get(position));
				items.remove(position);
				adapter.notifyDataSetChanged();
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
		i.putExtra("itemid", items.get(position).getId());
		i.putExtra("position", position);
		startActivityForResult(i, REQUEST_CODE);
	}

	// reads items from the database
	private void readItems() {
		List<Item> todos = db.getAllItems();
		items = new ArrayList<Item>();
		for (Item todo : todos) {
			items.add(new Item(todo.getId(), todo.getItemName(), todo
					.getDueDate()));
		}
	}

	// saves items to the database
	private void saveItem(Item todo) {
		db.addItem(todo);
	}

	// updates the edited item to the database
	private void updateItems(Item item) {
		db.updateItem(item);
	}

	// displays new edited text
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

			int itemid = data.getIntExtra("itemid", 0);
			String newItem = data.getExtras().getString("newItem");
			String date = data.getExtras().getString("date");
			int pos = data.getIntExtra("position", 0);

			Item newEditedItem = new Item(itemid, newItem, date);

			items.set(pos, newEditedItem);
			updateItems(items.get(pos));
			adapter.notifyDataSetChanged();
			lvItems.setAdapter(adapter);
			populateItemsList();
			startActivity(data);
		}
	}
}
