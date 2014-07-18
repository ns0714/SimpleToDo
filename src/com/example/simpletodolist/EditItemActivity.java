package com.example.simpletodolist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

@SuppressLint("SimpleDateFormat")
public class EditItemActivity extends Activity {

	public final int REQUEST_CODE = 25;
	public long dateInMillis = 0L;
	private String dateString ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		// Loads the text into edit text screen
		EditText editText = (EditText) findViewById(R.id.editItem);
		ArrayList<Item> items = getIntent().getParcelableArrayListExtra("items");

		int position = getIntent().getIntExtra("position", 0);

		String oldText = items.get(position).getItemName().toString();
		editText.setText(oldText);

		int textLength = oldText.length();
		editText.setSelection(textLength);
	}

	// gets called when SAVE button is pressed
	public void onSubmit(View v) {
		// closes the activity and returns to first screen
		Intent intent = new Intent(EditItemActivity.this, ToDoActivity.class);

		EditText editText = (EditText) findViewById(R.id.editItem);

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		EditText dateText = (EditText) findViewById(R.id.dateText);
		
		if(dateText.getText().equals("")){
			dateInMillis =0L;
		}
		
		if(dateInMillis!=0L){
			dateString = formatter.format(new Date(dateInMillis));
		}else{
			dateString = "";
		}

		Item newEditedItem = new Item(editText.getText().toString(), dateString);

		if (newEditedItem != null) {
			ArrayList<Item> items = getIntent().getParcelableArrayListExtra(
					"items");
			int pos = getIntent().getIntExtra("position", 0);
			items.set(pos, newEditedItem);
			intent.putParcelableArrayListExtra("items_s", items);
			// intent.putExtra("date", dateInMillis);
			// intent.putExtra("posr", pos);
			// startActivityForResult(intent, REQUEST_CODE);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	// click on Add priority text field
	public void onClickEditPriority(View v) {
		// closes the activity and returns to first screen
		Intent intent = new Intent(EditItemActivity.this,
				PriorityActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			dateInMillis = data.getLongExtra("date", 0L);
			EditText dateText = (EditText) findViewById(R.id.dateText);

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dateString = formatter.format(new Date(dateInMillis));
			dateText.setText(dateString);
		}
	}
}
