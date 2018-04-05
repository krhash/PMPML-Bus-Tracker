package com.opm.saitama.bms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback {

    BitmapDescriptor bitmapDescriptor;
    String myJSON = null;
    JSONArray markers = null;
    private GoogleMap mMap;
    ArrayList<HashMap<String,String>> markerslist;
    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //Bitmap bitmap_nm = BitmapFactory.decodeResource(this.getResources(),R.drawable.busmarker);
        //Bitmap bitmap = bitmap_nm.copy(Bitmap.Config.ARGB_8888, true);
        //bitmap.setWidth(20);
        //bitmap.setHeight(20);
        //bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        markerslist = new ArrayList<HashMap<String, String>>();
        t = new Thread(){
            public void run(){
                try{
                    while(true)
                    {
                        getdata();
                        sleep(5000);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
        mapFragment.getMapAsync(this);  //call mapready once we retrieve all data

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng location = new LatLng(18.453423,70.431256);  //move camera to pune

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
    public void getdata(){
        class GetDataJSON extends AsyncTask<String,Void,String>{
            private Context context;
            public GetDataJSON(Context context) {
                this.context = context;
            }
            @Override
            protected String doInBackground(String... arg) {
                try {
                    URL url = new URL("http://192.168.0.104/my-site/trackjson.php");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    String data = URLEncoder.encode("busid","utf-8") + "=" + URLEncoder.encode("1","utf-8");
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();
                    wr.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine())!=null)
                    {
                        sb.append(line);
                    }
                    reader.close();
                    return sb.toString();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,"do in bg: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result) {
                //Toast.makeText(context,"ResultJSON: "+result,Toast.LENGTH_SHORT).show();
                myJSON = result;

                mMap.clear();   //cannot clear map in thread  as this function should be called by main thread

                markerslist.clear();
                setMarkers();   //repopulate markers list

                Log.e("JSON STRING: ",result);
            }

        }
        new GetDataJSON(this).execute("arg");
    }

    public void setMarkers() {
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            markers = jsonObj.getJSONArray("markers");

            for(int i=0; i<markers.length(); i++)
            {
                JSONObject c = markers.getJSONObject(i);
                String bid = c.getString("bid");
                Log.e("Bus id: ",bid);
                String lat = c.getString("lat");
                String lng = c.getString("lng");

                HashMap<String,String> pins = new HashMap<>();

                pins.put("bid",bid);
                pins.put("lat",lat);
                pins.put("lng",lng);

                markerslist.add(pins);

                //Log.e("Marker: ",markerslist.get(i).get("bid"));

            }



            for (int i = 0; i < markerslist.size(); i++) {
                HashMap<String, String> op_pin = markerslist.get(i);
                double lat = Double.parseDouble(op_pin.get("lat"));
                double lng = Double.parseDouble(op_pin.get("lng"));
                LatLng location = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(location).title("Bus no: "+op_pin.get("bid")));    //set markers on map
            }

        }catch (Exception e){
            Toast.makeText(TrackingActivity.this,"ShowMarkers: "+e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(null != t){
            t.interrupt();
        }
        super.onBackPressed();

    }
}
