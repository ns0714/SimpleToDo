package com.example.simpletodolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ToDoAdapter extends ArrayAdapter<Item> {
	private TextView dueDateField;
	private TextView todoListName;

	public ToDoAdapter(Context context, ArrayList<Item> items) {
		super(context, R.layout.list_row, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_row, parent, false);
		}

		Item item = getItem(position);

		if (item != null) {
			todoListName = (TextView) convertView.findViewById(R.id.itemName);
			dueDateField = (TextView) convertView.findViewById(R.id.dueDate);

			if (todoListName != null) {
				todoListName.setText(item.getItemName().toString());
			}

			Calendar c = Calendar.getInstance();

			Date todaysDate = c.getTime();
			SimpleDateFormat due = new SimpleDateFormat("MM/dd/yyyy");
			Date dueDate = new Date();
			try {
				dueDate = due.parse(item.getDueDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (dueDate != null) {
				String date = item.getDueDate().toString();
				if (date.length() > 0) {
					if (dueDate.before(todaysDate)) {
						dueDateField.setTextColor(Color.RED);
						dueDateField.setText("Due: " + date);
					} else if (dueDate.after(todaysDate)) {
						dueDateField.setText("Due: " + date);
					}
				} else {
					dueDateField.setText("Click to add due date");
				}
			}
		}
		return convertView;
	}
}