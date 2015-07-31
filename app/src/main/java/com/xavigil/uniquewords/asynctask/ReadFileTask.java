package com.xavigil.uniquewords.asynctask;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xavigil.uniquewords.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads a local or remote text file and passes the words to a @link{IReadFileTask} listener
 * during the process.
 */
public class ReadFileTask extends AsyncTask<String, ReadFileTask.ProgressWrapper, String> {

    private static final String TAG = "ReadFileTask";

    private Context mCtx;
    private ProgressBar mProgressBar;
    private IReadFileTask mListener;

    // We use this wrapper to pass multiple values to the progress update
    public class ProgressWrapper{
        Integer progress;
        String word;

        public ProgressWrapper(Integer progress, String word){
            this.progress = progress;
            this.word = word;
        }
    }

    public ReadFileTask(Context context, ProgressBar pb, IReadFileTask listener){
        if (context == null || listener == null)
            throw new NullPointerException("ReadFileTask requires a non null Context and listener");
        mCtx = context;
        mProgressBar = pb;
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String resource = params[0];
        if(resource == null) return null;

        if(BuildConfig.DEBUG)
            Log.d(TAG, "resource = " + resource);

        InputStream input = null;
        HttpURLConnection connection = null;
        BufferedReader br = null;

        URL url;
        long fileLength = -1;
        try{
            try {
                url = new URL(resource);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    if(BuildConfig.DEBUG) {
                        Log.e(TAG, "Server response code " + connection.getResponseCode()
                                + " | " + connection.getResponseMessage());
                    }
                    return "There was a problem with the url. Please try again.";
                }
                fileLength = connection.getContentLength();
                input = connection.getInputStream();
            }
            catch(MalformedURLException ignored){
                // This exception means the resource is a local file
                // It is difficult to read to content length of a Uri in Android.
                Uri uri = Uri.parse(resource);
                input = mCtx.getContentResolver().openInputStream(uri);
            }

            br = new BufferedReader(new InputStreamReader(input));
            long bytesRead = 0;
            while(true) {
                String line = br.readLine();
                if(line != null) {
                    bytesRead = bytesRead + line.getBytes().length + 2;

                    int progress = (int) (((double) bytesRead / (double) fileLength) * 100);

                    // Regex that matches word characters [A-Za-z0-9_]
                    Pattern pattern = Pattern.compile("\\w+");
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        publishProgress(new ProgressWrapper(progress, matcher.group()));
                    }
                } else {
                    break;
                }
            }
            publishProgress(new ProgressWrapper(100, null));

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(input != null)
                    input.close();
                if(br != null)
                    br.close();
            }catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(ProgressWrapper... values) {
        // Runs on the UI thread
        super.onProgressUpdate(values);
        if(mProgressBar != null)
            mProgressBar.setProgress(values[0].progress);
        if(values[0].word != null)
            mListener.onNextWord(values[0].word);
    }

    @Override
    protected void onPostExecute(String message) {
        if (message != null)
            Toast.makeText(mCtx, message, Toast.LENGTH_LONG).show();
    }
}
