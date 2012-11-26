package csci498.jpigg.lunchlist;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

	private Context ctxt = null;
	private RestaurantHelper helper = null;
	private Cursor restaurants = null;
	
	public ListViewsFactory(Context ctxt, Intent intent) {
		this.ctxt = ctxt;
	}
	
	public int getCount() {
		return restaurants.getCount();
	}

	public long getItemId(int position) {
		restaurants.moveToPosition(position);
		return restaurants.getInt(0);
	}

	public RemoteViews getLoadingView() {
		return null;
	}

	public RemoteViews getViewAt(int position) {
		RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.widget_row);
		restaurants.moveToPosition(position);
		row.setTextViewText(android.R.id.text1, restaurants.getString(1));
		Intent i = new Intent();
		Bundle extras = new Bundle();
		
		extras.putString(LunchList.ID_EXTRA, String.valueOf(restaurants.getInt(0)));
		
		i.putExtras(extras);
		row.setOnClickFillInIntent(android.R.id.text1, i);
		
		return row;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void onCreate() {
		helper = new RestaurantHelper(ctxt);
		restaurants = helper.getReadableDatabase().rawQuery("SELECT _ID, name FROM restaurants", null);
	}

	public void onDataSetChanged() {
		// no-op
	}

	public void onDestroy() {
		restaurants.close();
		helper.close();
	}

}
