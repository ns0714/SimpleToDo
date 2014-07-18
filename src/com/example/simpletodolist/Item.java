package com.example.simpletodolist;

import android.os.Parcel;
import android.os.Parcelable;

	public class Item implements Parcelable{
		private String itemName;
		private String dueDate;
		
		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public String getDueDate() {
			return dueDate;
		}

		public void setDueDate(String dueDate) {
			this.dueDate = dueDate;
		}

		public Item(String itemName, String dueDate) {
			this.itemName = itemName;
			this.dueDate = dueDate;
		}
		
		public Item() {
			
		}
		public Item(String itemName) {
			this.itemName = itemName;
		}
		
		private Item(Parcel in) {
	        // This order must match the order in writeToParcel()
	        itemName = in.readString();
	        dueDate = in.readString();
	        // Continue doing this for the rest of your member data
	    }

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeString(itemName);
			dest.writeString(dueDate);
		}
		
		public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
	        public Item createFromParcel(Parcel in) {
	            return new Item(in);
	        }

	        public Item[] newArray(int size) {
	            return new Item[size];
	        }
	    };
	}
