package com.example.simpletodolist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ToDoAdapter extends ArrayAdapter<Item> {
	private TextView dueDate;
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
			dueDate = (TextView) convertView.findViewById(R.id.dueDate);

			if (todoListName != null) {
				todoListName.setText(item.getItemName().toString());
			}

			if (dueDate != null) {
				String date = item.getDueDate().toString();
				if (date.length() > 0) {
					dueDate.setText("Due: " + date);
				} else {
					dueDate.setText("Click to add priority date");
				}
			}
		}
		return convertView;
	}
}