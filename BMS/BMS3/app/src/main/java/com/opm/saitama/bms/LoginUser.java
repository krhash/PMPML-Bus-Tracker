package com.opm.saitama.bms;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


/**
 * Created by saitama on 8/10/2016.
 */
public class LoginUser extends AsyncTask<String,Void,String>{

    private Context context;

    public LoginUser(Context context) {
        this.context = context;

    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String username = (String) arg0[0];
            String password = (String) arg0[1];

            //String link = "http://bmspune.6te.net/login.php";
            String link = "http://192.168.0.104/my-site/login.php";

            String data = URLEncoder.encode("username", "utf-8") + "=" +URLEncoder.encode(username,"utf-8");
            data += "&" + URLEncoder.encode("password","utf-8") + "=" + URLEncoder.encode(password,"utf-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine())!= null) {

                sb.append(line);
                break;
            }
            //wr.close();
            //reader.close();
            return sb.toString();
        }
        catch(Exception e) {
            return "Exception: "+e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result){    //result is string returned by doinbackground function
       if(result.equals("success")) {
           Intent intent = new Intent(context, TrackingActivity.class);
           context.startActivity(intent);
       }
    }
}
