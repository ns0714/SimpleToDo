package com.example.simpletodolist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ToDoAdapter extends ArrayAdapter<String> {
	TextView dueDate;
	TextView todoListName;

	public ToDoAdapter(Context context, ArrayList<String> items) {
		super(context, R.layout.list_row, items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_row, parent, false);
		}
		
		String item = getItem(position);

		if (item != null) {
			todoListName = (TextView) convertView.findViewById(R.id.itemName);
			dueDate = (TextView) convertView.findViewById(R.id.dueDate);
			if (todoListName != null) {
				todoListName.setText(item);
			}

			if (dueDate != null) {
				dueDate.setText("Due: ");
			}
		}
		return convertView;
	}
}