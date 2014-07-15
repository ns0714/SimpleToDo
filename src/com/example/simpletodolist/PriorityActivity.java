package com.example.simpletodolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

public class PriorityActivity extends Activity {

	public final int REQUEST_CODE = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_priority);
	}

	public void selectDate(View v) {
		// returns date to the edit activity
		Intent intent = new Intent(PriorityActivity.this, EditItemActivity.class);
		CalendarView cal = (CalendarView) findViewById(R.id.calendarView);
		long dateInMillis = cal.getDate();
		intent.putExtra("date", dateInMillis);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
