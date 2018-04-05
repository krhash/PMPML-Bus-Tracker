package com.opm.saitama.bmsnode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText et_bid;
    Button button_submit;
    LocationListener ll;
    LocationManager lm;
    Thread t;
    String bid;
    int toggleServiceFlag,serviceStatus;
    static double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVars();
        initThread();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bid = et_bid.getText().toString();
                if(toggleServiceFlag == 0)
                {
                    initThread();
                    t.start();
                    toggleServiceFlag = 1;
                    Log.e("Service: ","ON");
                    if(serviceStatus == 1)
                    {
                        Toast.makeText(MainActivity.this,"Service started successfully! ;)",Toast.LENGTH_SHORT).show();
                    }
                    else if(serviceStatus == 0)
                    {
                        Toast.makeText(MainActivity.this,"Check bus id! :|)",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Error in connection! ;_;",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(toggleServiceFlag == 1)
                {
                    t.interrupt();
                    toggleServiceFlag = 0;
                    Log.e("Service: ","OFF");
                    Toast.makeText(MainActivity.this,"Service stopped! Bye :)",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    void initVars() {
        et_bid = (EditText) findViewById(R.id.et_busId);
        button_submit = (Button) findViewById(R.id.button_submit);
        ll = new MyLocListener();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        toggleServiceFlag = serviceStatus = 0;
    }

    void initThread(){
        t = new Thread() {
            @Override
            public void run() {
                try{

                    while(true) {
                        new updateToDb().execute();
                        sleep(5000);
                        if(t.isInterrupted()) {
                            t = null;
                            break;
                        }

                    }

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
    }


    class MyLocListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {

            if(location != null)
            {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    class updateToDb extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... arg) {
            try {

                URL url = new URL("http://192.168.0.104/my-site/locationupdate.php");
                URLConnection conn = url.openConnection();
                String data = URLEncoder.encode("bid","utf-8") + "=" + URLEncoder.encode(bid,"utf-8");
                data +="&" + URLEncoder.encode("lat","utf-8") + "=" + URLEncoder.encode(Double.toString(lat),"utf-8");
                data +="&" + URLEncoder.encode("lng","utf-8") + "=" + URLEncoder.encode(Double.toString(lng),"utf-8");
                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = reader.readLine())!=null)
                {
                    sb.append(line);
                }
                return sb.toString();

            }catch(Exception e){
                e.printStackTrace();
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("Post: ",s);
            if(s.equals("success"))
                serviceStatus = 1;
            else if(s.equals("failure"))
                serviceStatus = 0;
            else
                serviceStatus = -1;
        }
    }

    @Override
    public void onBackPressed() {

        if(t != null)
            t.interrupt();
        super.onBackPressed();

    }
}
