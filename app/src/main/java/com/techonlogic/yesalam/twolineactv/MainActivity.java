package com.techonlogic.yesalam.twolineactv;

import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    AutoCompleteTextView actv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] from = {"stop_name","vicinity"};
        int[] to = {R.id.text1,R.id.secondline};
        final DatabaseHelper dbhelper = new DatabaseHelper(this);
        try{
            dbhelper.createDataBase();

        }catch (IOException ie){
            throw new Error("Unable to create Database");
        }
        try{
            dbhelper.openDataBase();
        }catch (SQLException sqle){
            throw sqle;
        }

        //Cursor cursor = dbhelper.query();
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.twoline,null,from,to);

        // This will provide the labels for the choices to be displayed in the AutoCompleteTextView
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                final int colIndex = cursor.getColumnIndexOrThrow("stop_name");
                return cursor.getString(colIndex);
            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                Cursor cursor=null;
                int count = constraint.length();
                if(count>=3){
                    String constrains = constraint.toString();
                    cursor = dbhelper.query(constrains);
                }
                return cursor;
            }
        });


        actv.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
