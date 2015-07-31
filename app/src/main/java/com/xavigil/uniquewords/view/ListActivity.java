package com.xavigil.uniquewords.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xavigil.uniquewords.R;
import com.xavigil.uniquewords.asynctask.ReadFileTask;
import com.xavigil.uniquewords.adapter.ListAdapter;

public class ListActivity extends AppCompatActivity {

    public static final String EXTRA_RESOURCE = "com.xavigil.uniquewords.extra_resource";

    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;
    private ReadFileTask mReadFileTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mAdapter = setupRecyclerView();

        ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        String resource = getIntent().getExtras().getString(EXTRA_RESOURCE);
        mReadFileTask = new ReadFileTask(this, pb, mAdapter);
        mReadFileTask.execute(resource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!isTaskFinished()){
            Toast.makeText(this, "Please wait until the file has been read.", Toast.LENGTH_SHORT)
                    .show();
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_default) {
            mAdapter.setSortOrder(ListAdapter.SortOrder.DEFAULT);
            return true;
        }
        else if(id == R.id.action_alphab){
            mAdapter.setSortOrder(ListAdapter.SortOrder.ALPHABETICALLY);
            return true;
        }
        else if(id == R.id.action_count){
            mAdapter.setSortOrder(ListAdapter.SortOrder.APPEARANCES);
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

    private boolean isTaskFinished(){
        return (mReadFileTask.getStatus() == AsyncTask.Status.FINISHED);
    }
}
