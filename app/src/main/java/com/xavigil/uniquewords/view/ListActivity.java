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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ListAdapter adapter = setupRecyclerView();

        ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar);
        String resource = getIntent().getExtras().getString(EXTRA_RESOURCE);
        (new ReadFileTask(this, pb, adapter)).execute(resource);
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

    private ListAdapter setupRecyclerView(){
        ListAdapter adapter = new ListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        return adapter;
    }

//    private List<UniqueWord> getMockList(){
//        List<UniqueWord> list = new ArrayList<>();
//        list.add(new UniqueWord("Abracadabra pata de cabra que me voy", 131));
//        list.add(new UniqueWord("Beacomb", 101));
//        list.add(new UniqueWord("Conrwall", 98));
//        list.add(new UniqueWord("Dámaris", 98));
//        list.add(new UniqueWord("Escorial", 53));
//        list.add(new UniqueWord("Feo", 12));
//        list.add(new UniqueWord("Gramática", 8));
//        list.add(new UniqueWord("Hielo", 8));
//        list.add(new UniqueWord("Indignados", 3));
//        list.add(new UniqueWord("Jovellanos", 1));
//        return list;
//    }
}
