package com.example.simpletodolist;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqlLiteHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1; //database version
	private static final String DATABASE_NAME = "ItemsDB"; //database name

	public MySqlLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// SQL statement to create item table
		String CREATE_ITEM_TABLE = "CREATE TABLE items ( "
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT, "
				+ "dueDate TEXT )";

		// create items table
		db.execSQL(CREATE_ITEM_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS items");

		// create fresh items table
		this.onCreate(db);
	}
	
    // Items table name
    private static final String TABLE_ITEMS = "items";
 
    // Items Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DUEDATE = "dueDate";
 
    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_DUEDATE};
 
    public void addItem(Item item){
    	
        Log.d("addItem", item.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getItemName()); // get title 
        values.put(KEY_DUEDATE, item.getDueDate()); // get dueDate
 
        db.insert(TABLE_ITEMS,
                null, 
                values); 
        // 4. close
        db.close(); 
    }
 
    public Item getItem(int id){
 
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = 
                db.query(TABLE_ITEMS, 
                COLUMNS, 
                " id = ?", 
                new String[] { String.valueOf(id) }, 
                null, 
                null, 
                null, 
                null);
 
        if (cursor != null)
            cursor.moveToFirst();
 
        Item item = new Item();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setItemName(cursor.getString(1));
        item.setDueDate(cursor.getString(2));
 
        Log.d("getItem("+id+")", item.toString());
 
        return item;
    }
 
    // Get All items
    public List<Item> getAllItems() {
        List<Item> items = new LinkedList<Item>();
 
        String query = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        Item item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setItemName(cursor.getString(1));
                item.setDueDate(cursor.getString(2));
                items.add(item);
            } while (cursor.moveToNext());
        }
 
        Log.d("getAllItems()", items.toString());
 
        return items;
    }
 
     // Updating single item
    public int updateItem(Item item) {
 
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put("title", item.getItemName()); // get title 
        values.put("dueDate", item.getDueDate()); // get duedate
 
        int i = db.update(TABLE_ITEMS, 
                values, 
                KEY_ID+" = ?", 
                new String[] { String.valueOf(item.getId()) }); 
 
        db.close();
        Log.d("updateItem", item.toString());
 
        return i;
 
    }
 
    // Deleting single item
    public void deleteItem(Item item) {
 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_ITEMS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(item.getId()) });
 
        db.close();
 
        Log.d("deleteItem", item.toString());
 
    }
    public void deleteItems() {
    	 List<Item> allItems = getAllItems();
        SQLiteDatabase db = this.getWritableDatabase();
 
        for(Item i : allItems){
        db.delete(TABLE_ITEMS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(i.getId()) });
        }
        db.close();
    }
}
