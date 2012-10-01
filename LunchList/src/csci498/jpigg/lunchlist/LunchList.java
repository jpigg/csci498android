package csci498.jpigg.lunchlist;

import java.util.*;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.TabHost;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import java.util.concurrent.atomic.AtomicBoolean;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LunchList extends ListActivity {
	
	public final static String ID_EXTRA = "csci498.jpigg.lunchlist._ID";
	Cursor model;
	RestaurantAdapter adapter = null;
    EditText name = null;
    EditText address = null;
    EditText notes = null;
    RadioGroup types = null;
    RestaurantHelper helper;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_lunch_list);
    	helper = new RestaurantHelper(this);
    	prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	initList();
    	prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }
    
    private void initList() {
    	if (model != null) {
    		stopManagingCursor(model);
    		model.close();
    	}
    	

    	model = helper.getAll(prefs.getString("sort_order",  "name"));
    	startManagingCursor(model);
    	adapter = new RestaurantAdapter(model);
    	setListAdapter(adapter);
    }
    
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	
    	helper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(this).inflate(R.menu.option, menu);
    	
    	return(super.onCreateOptionsMenu(menu));
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId()==R.id.add){
    		startActivity(new Intent(LunchList.this, DetailForm.class));
    		
    		return(true);
    	}
    	else if (item.getItemId()==R.id.prefs){
    		startActivity(new Intent(this, EditPreferences.class));
    	}
    	
    	return(super.onOptionsItemSelected(item));
    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
    	Intent i = new Intent(LunchList.this, DetailForm.class);
    	
    	i.putExtra(ID_EXTRA, String.valueOf(id));
    	startActivity(i);
    }
    
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		
		public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
			if (key.equals("sort_order")) {
				initList();
			}
			
		}
	};

	
	class RestaurantAdapter extends CursorAdapter
	{
		RestaurantAdapter(Cursor c)
		{
			super(LunchList.this, c);
		}
		
		@Override
		public void bindView(View row, Context ctxt, Cursor c)
		{
			RestaurantHolder holder = (RestaurantHolder)row.getTag();
			
			holder.populateFrom(c, helper);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent)
		{
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row,  parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			
			return(row);
		}
		
	}
	
	static class RestaurantHolder
	{
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;
		
		RestaurantHolder(View row)
		{
			name = (TextView)row.findViewById(R.id.title);
			address = (TextView)row.findViewById(R.id.address);
			icon = (ImageView)row.findViewById(R.id.icon);
		}
		
		void populateFrom(Cursor c, RestaurantHelper helper)
		{
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));
			
			if (helper.getType(c).equals("sit_down"))
			{
				icon.setImageResource(R.drawable.ball_red);
				//name.setTextColor(0xffff0000);
				//address.setTextColor(0xddff0000);
			}
			else if (helper.getType(c).equals("take_out"))
			{
				icon.setImageResource(R.drawable.ball_yellow);
				//name.setTextColor(0xffffff00);
				//address.setTextColor(0xddffff00);
			}
			else
			{
				icon.setImageResource(R.drawable.ball_green);
				//name.setTextColor(0xff00ff00);
				//address.setTextColor(0xdd00ff00);
			}
		}
	}
	
}