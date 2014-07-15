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
	public final int REQUEST_CODE_1 = 30;
	public static long dateInMillis = 0L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		// Loads the text into edit text screen
		EditText editText = (EditText) findViewById(R.id.editItem);
		String oldText = getIntent().getStringExtra("itemValue");

		int textLength = oldText.length();
		editText.setText(oldText);
		editText.setSelection(textLength);
	}

	// gets called when SAVE button is pressed
	public void onSubmit(View v) {
		// closes the activity and returns to first screen
		Intent intent = new Intent(EditItemActivity.this, ToDoActivity.class);

		EditText editText = (EditText) findViewById(R.id.editItem);
		String newItem = editText.getText().toString();
		
		if (newItem.trim().length() > 0) {
			ArrayList<String> items = getIntent().getStringArrayListExtra("items");
			int pos = getIntent().getIntExtra("position", 0);
			items.set(pos, newItem);
			intent.putStringArrayListExtra("items_s", items);
			intent.putExtra("date", dateInMillis);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	public void onClickPriority(View v) {
		// closes the activity and returns to first screen
		Intent intent = new Intent(EditItemActivity.this,
				PriorityActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			dateInMillis = data.getLongExtra("date", 0L);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dateString = formatter.format(new Date(dateInMillis));

			EditText dateText = (EditText) findViewById(R.id.dateText);
			dateText.setText(dateString);
		}
	}
}
