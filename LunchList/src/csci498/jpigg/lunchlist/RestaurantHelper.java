package csci498.jpigg.lunchlist;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;

public class RestaurantHelper extends SQLiteOpenHelper
{
	
	private static final String DATABASE_NAME = "lunchlist.db";
	private static final int SCHEMA_VERSION = 1;
	
	public RestaurantHelper(Context context)
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	public void insert(String name, String address, String type, String notes)
	{
		ContentValues cv = new ContentValues();
		
		cv.put("name",  name);
		cv.put("address",  address);
		cv.put("type",  type);
		cv.put("notes", notes);
		
		getWritableDatabase().insert("restaurants",  "name",  cv);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//nothing to do until another schema version is created
	}

}
