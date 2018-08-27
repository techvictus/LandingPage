package clinic.janelaaj.landingpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This {@link ListActivity} is to show the list of doctors using
 * ListView with the help of {@link Profile} and {@link ProfileAdapter}
 *
 * @author Sambit Mallick (sambit-m)
 * Created by Sambit Mallick on 16.08.2018
 */

public class ListActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ArrayList<Profile> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_list);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ImageView inbox = (ImageView) findViewById(R.id.inbox);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent landingPageIntent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(landingPageIntent);
            }
        });

        Intent intent = getIntent();
        String cityName = intent.getExtras().getString("CityName");
        String selectedParam = intent.getExtras().getString("paramSelectedLocality");
        String who = intent.getExtras().getString("who");
        String specialitySelectedId = intent.getExtras().getString("SpecialitySelectedId");

        TextView summary = (TextView) findViewById(R.id.summary);

        LatLng locationPoint;
        String address;
        double longitude;
        double latitude;
        if (selectedParam != null && !selectedParam.equals("Select your location")) {
                longitude = intent.getExtras().getDouble("paramLongitude");
                latitude = intent.getExtras().getDouble("paramLatitude");

            summary.setText(Html.fromHtml("Searching for " + "<b>" + who + "</b>" + " by their " + "<b>" +"Location near: " + "</b>" + "<br>" + "<b>" + selectedParam + "</b>"));
        } else {
            address = cityName;
            locationPoint = getLocationFromAddress(ListActivity.this, address);
            longitude = locationPoint.longitude;
            latitude = locationPoint.latitude;
            summary.setText(Html.fromHtml("Searching for " + "<b>" + who + "</b>" + "<b>" + " around: " + "</b>" + "<br>" + "<b>" + cityName + "</b>"));
            selectedParam = "Not Selected";
        }
        String DM_Role = null;
        if (who.equals("Doctors"))
            DM_Role = "DOC";
        if (who.equals("Vitals"))
            DM_Role = "VIT";
        if(DM_Role==null)
            DM_Role="1";
        Log.d("res1234",DM_Role);
        Log.d("res1234",who);
        JSONObject js = new JSONObject();
        try {
            js.put("dmrole", DM_Role);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            js.put("cityname", cityName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            js.put("localityname", selectedParam);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            js.put("localitylat", latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            js.put("localitylong", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            js.put("localitylat", latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            js.put("localitylong", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            js.put("specialityid",specialitySelectedId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsString = js.toString();
        String url;
        url = Endpoints.BASE_URL + Endpoints.GET_DOCTORS_BY_LOCATION;
        ConnectionAsyncTask task = new ConnectionAsyncTask();
        task.execute(url, jsString);
//        JSONObject doctorsList = null;
//        try {
//            doctorsList = ConnectionUtil.postMethod(url, js.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        JSONArray ldoctorid = null;
//        try {
//            assert doctorsList != null;
//            ldoctorid = doctorsList.getJSONArray("info");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ArrayList<Profile> profiles = new ArrayList<>();
//        for (int i = 0; i < ldoctorid.length(); i++) {
//            JSONObject result = null;
//            try {
//                result = ldoctorid.getJSONObject(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            assert result != null;
//            String profileName = result.optString("ldoctorname");
//            profiles.add(new Profile(profileName));
//        }
//        ImageView inbox = (ImageView) findViewById(R.id.inbox);
//        inbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent landingPageIntent = new Intent(ListActivity.this, MainActivity.class);
//                startActivity(landingPageIntent);
//            }
//        });
//
////        profiles.add(new Profile("a"));
////        profiles.add(new Profile("b"));
////        profiles.add(new Profile("c"));
////        profiles.add(new Profile("d"));
////        profiles.add(new Profile("e"));
////        profiles.add(new Profile("f"));
////        profiles.add(new Profile("g"));
//        ProfileAdapter adapter = new ProfileAdapter(this, profiles);
//        ListView profileListView = (ListView) findViewById(R.id.list);
//        profileListView.setAdapter(adapter);

        final ImageView expandListButton = (ImageView) findViewById(R.id.custom_down_arrow);
        final LinearLayout expandList = (LinearLayout) findViewById(R.id.drop_down_two);
        final ImageView collapseListButton = (ImageView) findViewById(R.id.custom_up_arrow_two);
        final TextView filterBy = (TextView) findViewById(R.id.filter_by);
        expandListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandList.setVisibility(View.VISIBLE);
                expandListButton.setVisibility(View.GONE);
                collapseListButton.setVisibility(View.VISIBLE);
                filterBy.setVisibility(View.GONE);
            }
        });

        collapseListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandList.setVisibility(View.GONE);
                expandListButton.setVisibility(View.VISIBLE);
                collapseListButton.setVisibility(View.GONE);
                filterBy.setVisibility(View.VISIBLE);
            }
        });

        filterBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
//                final EditText etUsername = alertLayout.findViewById(R.id.et_username);
//                final EditText etEmail = alertLayout.findViewById(R.id.et_email);
//                final CheckBox cbToggle = alertLayout.findViewById(R.id.cb_show_pass);

//                cbToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            // to encode password in dots
//                            etEmail.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                        } else {
//                            // to display the password in normal text
//                            etEmail.setTransformationMethod(null);
//                        }
//                    }
//                });

                AlertDialog.Builder alert = new AlertDialog.Builder(ListActivity.this);
                alert.setTitle("Info");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
//                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        String user = etUsername.getText().toString();
////                        String pass = etEmail.getText().toString();
////                        Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();
//                    }
//                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            if (address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    private class ConnectionAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            if (progressDialog != null)
                progressDialog.cancel();
            progressDialog = ProgressDialog.show(ListActivity.this, "", "Please wait...", true, true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject result = null;
            try {
                result = ConnectionUtil.postMethod(strings[0], strings[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            JSONArray ldoctorid = null;
            try {
                assert result != null;
                ldoctorid = result.getJSONArray("info");
                Log.d("res1234", "han "+ldoctorid);
            } catch (JSONException e) {
                Log.d("res1234", "nah");
                e.printStackTrace();
            }
            profiles = new ArrayList<>();
            for (int i = 0; i < ldoctorid.length(); i++) {
                JSONObject obj = null;
                obj = ldoctorid.optJSONObject(i);
                Log.d("res1234", "hai kya "+String.valueOf(obj));
                assert obj != null;
                String doctorId = obj.optString("ldoctorid");
                String doctorName = obj.optString("ldoctorname");
                byte[] decodedString = Base64.decode(obj.optString("ldoctorphoto"), Base64.DEFAULT);
                Bitmap doctorphoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                //doctorImage.setImageBitmap(decodedByte);
//                String doctorPhotoString = obj.optString("ldoctorphoto");
//                Log.d("img",doctorPhotoString);
//                byte[] byteData = new byte[0];//Better to specify encoding
//                try {
//                    byteData = doctorPhotoString.getBytes("UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                Blob doctorPhoto = null;
//                try {
//                    doctorPhoto.setBytes(1, byteData);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
                String mbbsflag = obj.optString("lmbbsflag");
                String mdflag = obj.optString("lmdflag");
                String msflag = obj.optString("lmsflag");
                String cliniclocationname = obj.optString("lcliniclocationname");
                String addressline1 = obj.optString("laddressline1");
                String addressline2 = obj.optString("laddressline2");
                String city = obj.optString("lcity");
                String pincode = obj.optString("lpincode");
                String rating = obj.optString("lrating");
                String normalamount = obj.optString("lnormalamount");
                String discountedamount = obj.optString("ldiscountedamount");
                String discountflag = obj.optString("ldiscountflag");
                profiles.add(new Profile(doctorId, doctorName, doctorphoto, mbbsflag, mdflag, msflag, cliniclocationname, addressline1, addressline2, city, pincode, rating, normalamount, discountedamount, discountflag));
            }
            ProfileAdapter adapter = new ProfileAdapter(ListActivity.this, profiles);
            ListView profileListView = (ListView) findViewById(R.id.list);
            profileListView.setAdapter(adapter);

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
