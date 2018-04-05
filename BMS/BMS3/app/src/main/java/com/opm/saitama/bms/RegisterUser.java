package com.opm.saitama.bms;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by saitama on 8/12/2016.
 */
public class RegisterUser extends AsyncTask<String,Void,String> {

    private Context context;
    public RegisterUser(Context context){
        this.context = context;

    }
    @Override
    protected String doInBackground(String... arg) {

        try{
            String name = (String)arg[0];
            String email = (String)arg[1];
            String username = (String)arg[2];
            String password = (String)arg[3];

            //String link = "http://bmspune.6te.net/register.php";
            String link = "http://192.168.0.104/my-site/register.php";

            String data = URLEncoder.encode("name","utf-8") + "=" + URLEncoder.encode(name,"utf-8");
            data += "&" + URLEncoder.encode("email","utf-8") + "=" + URLEncoder.encode(email,"utf-8");
            data += "&" + URLEncoder.encode("username","utf-8") + "=" + URLEncoder.encode(username,"utf-8");
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

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }

            return sb.toString();

        }catch(Exception e) {
            return "Failure! Connection to host failed!";
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        Toast.makeText(this.context,result,Toast.LENGTH_SHORT).show();
    }
}
