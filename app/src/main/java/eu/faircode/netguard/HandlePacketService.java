package eu.faircode.netguard;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandlePacketService extends Service {

    private Handler handlerQuery = new Handler();
    private Runnable runQueryServer = new Runnable() {
        @Override
        public void run() {
            // case 1:
            //new doBackgroundTask().execute("http://www.google.com.tw/search?q=httpclinet");
            // case 2:
            new doBackgroundTask().execute("https://www.example.com/?search=ruby&results=10");
            handlerQuery.postDelayed(this,1000);
        }
    };

    public static final String LOG_TAG = "HandlePacketService && ";
    public class LocalBinder extends Binder
    {
        HandlePacketService getService()
        {
            return  HandlePacketService.this;
        }
    }
    private LocalBinder mLocBin = new LocalBinder();

    public void myMethod()
    {
        Log.d(LOG_TAG, "myMethod() begin");
        handlerQuery.post(runQueryServer);
        Log.d(LOG_TAG, "myMethod() finish");
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return mLocBin;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        // TODO Auto-generated method stub
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO Auto-generated method stub

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // TODO Auto-generated method stub
    }


    private class doBackgroundTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Log.d(LOG_TAG, "Query to " + url);
            HttpURLConnection conn = null;
            int responseCode = 0;
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                responseCode = conn.getResponseCode();

                InputStream inputStream;
                if (200 <= responseCode && responseCode <= 299) {
                    inputStream = conn.getInputStream();
                    Log.d(LOG_TAG, "Get response success. => " + inputStream);
                } else {
                    inputStream = conn.getErrorStream();
                    Log.d(LOG_TAG, "Get response fail. ErrorCode: " + Integer.toString(responseCode));
                }

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                inputStream));

                StringBuilder response = new StringBuilder();
                String currentLine;

                while ((currentLine = in.readLine()) != null)
                    response.append(currentLine);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}