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
 * Created by saitama on 9/4/2016.
 */
public class ResetPassword extends AsyncTask<String,Void,String> {

    private Context context;
    public ResetPassword(Context context){
        this.context = context;
    }
    @Override
    protected String doInBackground(String... arg) {

        try{
            String username = (String)arg[0];

            //String link = "http://bmspune.6te.net/resetpwd.php";
            String link = "http://192.168.0.104/my-site/resetpwd.php";

            String data = URLEncoder.encode("username","utf-8") + "=" + URLEncoder.encode(username,"utf-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine())!=null){
                sb.append(line);
                break;  //as we read only one line
            }
            return sb.toString();

        }catch (Exception e){
            return "Exception" + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("success"))
        {
            Toast.makeText(context,"Password mailed to registered EMAIL ID.",Toast.LENGTH_SHORT).show();
        }
        else if(result.equals("failure"))
        {
            Toast.makeText(context,"Incorrect userid!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
        }
    }
}
