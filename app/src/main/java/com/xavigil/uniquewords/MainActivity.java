package com.xavigil.uniquewords;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab = getSupportActionBar();
        if(ab!=null)
            ab.setTitle("Choose source:");
    }

    public void onFileClick(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, PICK_FILE);
    }

    public void onUrlClick(View view){
        showDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FILE){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
//                File file = new File(uri.getPath());
                startListActivity(uri.toString());
            }
        }
    }

    public void startListActivity(String resource){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListActivity.EXTRA_RESOURCE, resource);
        startActivity(intent);
    }

    private void showDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(R.string.enter_url);

        View dialogView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
        final EditText editText = (EditText)dialogView.findViewById(R.id.editText);
        dialog.setView(dialogView);

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.cancel),(DialogInterface.OnClickListener) null);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = editText.getText().toString();
                        if(text.length() == 0) return;
                        try {
                            URL url = new URL(text);
                            startListActivity(url.toString());
                        }
                        catch (MalformedURLException e) {
                            Toast.makeText(MainActivity.this,"Malformed url",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
        dialog.show();
    }
}
