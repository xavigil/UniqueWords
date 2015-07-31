package com.xavigil.uniquewords.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.xavigil.uniquewords.R;
import com.xavigil.uniquewords.asynctask.ReadFileTask;
import com.xavigil.uniquewords.adapter.ListAdapter;


public class ListActivity extends AppCompatActivity {

    public static final String EXTRA_RESOURCE = "com.xavigil.uniquewords.extra_resource";

    private RecyclerView mRecyclerView;
    private ReadFileTask mReadFileTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ListAdapter adapter = setupRecyclerView();

        ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        String resource = getIntent().getExtras().getString(EXTRA_RESOURCE);
        mReadFileTask = new ReadFileTask(this, pb, adapter);
        mReadFileTask.execute(resource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

    @Override
    protected void onStop() {
        super.onStop();
        mReadFileTask.cancel(true);
    }

    private ListAdapter setupRecyclerView(){
        ListAdapter adapter = new ListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        return adapter;
    }


}
