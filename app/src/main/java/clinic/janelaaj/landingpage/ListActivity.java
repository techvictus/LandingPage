package clinic.janelaaj.landingpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String DM_Role, cityName, selectedParam, who, specialitySelectedId, whoRadioButton=null;
    double longitude = 0, latitude = 0;
    private Button btnLoadMore;
    private ListView profileListView;
    private String[] localities, specialities;
    private Spinner locationSpinner, specialitySpinner;
    private ImageButton searchButton;
    private HashMap<String, String> specialityHash;
    private LatLng point;
    private HashMap<String, LatLng> pointHash;
    private boolean selectedLocationSpinner, selectedSpecialitySpinner;
    private RadioButton doctorsButton, testLabsButton, pharmaciesButton, vitalsButton;

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

//        ImageView inbox = (ImageView) findViewById(R.id.inbox);
//        inbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent landingPageIntent = new Intent(ListActivity.this, MainActivity.class);
//                startActivity(landingPageIntent);
//            }
//        });

        Intent intent = getIntent();
        cityName = intent.getExtras().getString("CityName");
        selectedParam = intent.getExtras().getString("paramSelectedLocality");
        who = intent.getExtras().getString("who");
        specialitySelectedId = intent.getExtras().getString("SpecialitySelectedId");
        localities = intent.getExtras().getStringArray("localities");
        specialities = intent.getExtras().getStringArray("specialities");
        specialityHash = (HashMap<String, String>) intent.getSerializableExtra("specialityHash");
        //Log.d("abcdhash", String.valueOf(specialityHash.keySet()));
        pointHash = (HashMap<String, LatLng>) intent.getSerializableExtra("pointHash");
        //Log.d("abcdhash", "???" + String.valueOf(pointHash.keySet()));
        locationSpinner = (Spinner) findViewById(R.id.search);
        specialitySpinner = (Spinner) findViewById(R.id.spinner_one);
        locationSpinner = Helper.SetSpinner(ListActivity.this, localities, locationSpinner);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                // Notify the selected item text
                Toast.makeText
                        (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                        .show();
                selectedParam = selectedItemText;
                if (position > 0) {
                    selectedLocationSpinner = true;
                    point = pointHash.get(selectedParam);
                    longitude = point.longitude;
                    latitude = point.latitude;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        specialitySpinner = Helper.SetSpinner(ListActivity.this, specialities, specialitySpinner);
        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    selectedLocationSpinner = true;
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    Log.d("id_speciality_who", "hash " + specialityHash);
                    specialitySelectedId = specialityHash.get(selectedItemText);
                    Log.d("id_speciality_who", "hash " + specialitySelectedId);

                    //Showing the queries according to the chosen filters {@link ConnectionAsyncTask}, {@link Endpoints}

//                        JSONArray info = null;
//                        try {
//                            assert jsonResponse != null;
//                            info = jsonResponse.getJSONArray("info");
//                            lists = info;
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        String[] localities = new String[info.length() + 1];
//                        if (info.length() == 0) {
//                            localities[0] = "Coming Soon!";
//                        } else {
//                            localities[0] = "Select nearest locality";
//                        }
//                        for (int i = 0; i < info.length(); i++) {
//                            JSONObject localityList = null;
//                            try {
//                                localityList = info.getJSONObject(i);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            String localityName = null;
//                            try {
//                                localityName = localityList.getString("llocalityname");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            localities[i + 1] = localityName;
//                        }
//                        final List<String> LocalitySpinnerList = new ArrayList<>(Arrays.asList(localities));
//
//                        // Initializing an ArrayAdapter
//                        final ArrayAdapter<String> LocalitySpinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, LocalitySpinnerList) {
//                            @Override
//                            public boolean isEnabled(int position) {
//                                return position != 0;
//                            }
//
//                            @Override
//                            public View getDropDownView(int position, View convertView,
//                                                        ViewGroup parent) {
//                                View view = super.getDropDownView(position, convertView, parent);
//                                TextView tv = (TextView) view;
//                                if (position == 0) {
//                                    // Set the hint text color gray
//                                    tv.setTextColor(Color.GRAY);
//                                } else {
//                                    tv.setTextColor(Color.BLACK);
//                                }
//                                return view;
//                            }
//                        };
//                        LocalitySpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//                        locationSpinner.setAdapter(LocalitySpinnerArrayAdapter);
//
//                        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                String selectedItemText = (String) parent.getItemAtPosition(position);
//                                // If user change the default selection
//                                // First item is disable and it is used for hint
//                                // Notify the selected item text
//                                Toast.makeText
//                                        (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
//                                        .show();
//                                paramSelectedLocality = selectedItemText;
//                                for (int i = 0; i < lists.length(); i++) {
//                                    JSONObject localityList = null;
//                                    try {
//                                        localityList = lists.getJSONObject(i);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    String localityName = null;
//                                    try {
//                                        assert localityList != null;
//                                        localityName = localityList.getString("llocalityname");
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if(paramSelectedLocality.equals(localityName)){
//                                        latitude = Double.parseDouble(localityList.optString("llocality_lat"));
//                                        longitude = Double.parseDouble(localityList.optString("llocality_long"));
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });

//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }


//                        URL url = null;
//                        try {
//                            url = new URL("http://35.200.243.43:3000/getlocality");
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        }
//                        HttpURLConnection conn = null;
//                        try {
//                            assert url != null;
//                            conn = (HttpURLConnection)url.openConnection();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        if ( conn != null ) {
//                            //Whatever you wants to post...
//                            String strPostData = "CityName="+"New Delhi";
//
//                            try {
//                                conn.setRequestMethod("POST");
//                            } catch (ProtocolException e) {
//                                e.printStackTrace();
//                            }
//                            conn.setRequestProperty("User-Agent", USER_AGENT);
//                            conn.setRequestProperty("Accept-Language", "en-GB,en;q=0.5");
//                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                            conn.setRequestProperty("Content-length", Integer.toString(strPostData.length()));
//                            conn.setRequestProperty("Content-Language", "en-GB");
//                            conn.setRequestProperty("charset", "utf-8");
//                            conn.setUseCaches(false);
//                            conn.setDoOutput(true);
//
//                            DataOutputStream dos = null;
//                            try {
//                                dos = new DataOutputStream(conn.getOutputStream());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            try {
//                                assert dos != null;
//                                dos.writeBytes(strPostData);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            try {
//                                dos.flush();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            try {
//                                dos.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            int intResponse = 0;
//                            try {
//                                intResponse = conn.getResponseCode();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            System.out.println("\nSending 'POST' to " + url.toString() +
//                                    ", data: " + strPostData + ", rc: " + intResponse);;
//                        }
//                    }
//                        String city = null;
//                        try {
//                            city = URLEncoder.encode("New Delhi", "UTF-8");
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//
//                        URL url = null;
//                        try {
//                            url = new URL("http://35.200.243.43:3000/getlocality");
//                            Log.d("Main","Ok");
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        }
//                        URLConnection connection = null;
//                        try {
//                            assert url != null;
//                            connection = url.openConnection();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        assert connection != null;
//                        connection.setDoOutput(true);
//
//                        OutputStreamWriter out = null;
//                        try {
//                            out = new OutputStreamWriter(
//                                    connection.getOutputStream());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            out.write("CityName=" + city);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            out.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        BufferedReader in = null;
//                        try {
//                            in = new BufferedReader(
//                                    new InputStreamReader(
//                                            connection.getInputStream()));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        String decodedString=null;
//                        assert in != null;
//                        try {
//                            while ((decodedString = in.readLine()) != null) {
//                                System.out.println(decodedString);
//                            }
//                        }catch (IOException){
//
//                        }
//                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView summary = (TextView) findViewById(R.id.summary);

        LatLng locationPoint;
        String address;
        longitude = 0;
        latitude = 0;
        if (selectedParam != null && !selectedParam.equals("Select your location")) {
            if (selectedParam.equals("Current Location")) {
                Log.d("abcde", "naa");
                try {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GpsTracker gpsTracker = new GpsTracker(ListActivity.this);
                if (gpsTracker.canGetLocation()) {
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();
                } else {
                    gpsTracker.showSettingsAlert();
                }
            } else {
                longitude = intent.getExtras().getDouble("paramLongitude");
                latitude = intent.getExtras().getDouble("paramLatitude");
            }

            summary.setText(Html.fromHtml("Searching for " + "<b>" + who + "</b>" + " by their " + "<b>" + "Location near: " + "</b>" + "<br>" + "<b>" + selectedParam + "</b>"));
        } else {
            Log.d("abcde", "haan");
            address = cityName;
            locationPoint = getLocationFromAddress(ListActivity.this, address);
            longitude = locationPoint.longitude;
            latitude = locationPoint.latitude;
            summary.setText(Html.fromHtml("Searching for " + "<b>" + who + "</b>" + "<b>" + " around: " + "</b>" + "<br>" + "<b>" + cityName + "</b>"));
            selectedParam = "Not Selected";
        }
        DM_Role = null;
        if (who.equals("Doctors"))
            DM_Role = "DOC";
        if (who.equals("Vitals"))
            DM_Role = "VIT";
        if (DM_Role == null)
            DM_Role = "1";
        Log.d("res1234", DM_Role);
        Log.d("res1234", who);

        searchButton = findViewById(R.id.search_button_list_activity);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedLocationSpinner && !selectedSpecialitySpinner){
                    Toast.makeText(getApplicationContext(), "Please select your locality or speciality!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(doctorsButton!=null)
                    who = whoRadioButton;
                Intent profileListIntent = new Intent(ListActivity.this, ListActivity.class);
                profileListIntent.putExtra("CityName", cityName);
                profileListIntent.putExtra("SpecialitySelectedId", specialitySelectedId);
                profileListIntent.putExtra("paramSelectedLocality", selectedParam);
                profileListIntent.putExtra("paramLatitude", latitude);
                profileListIntent.putExtra("paramLongitude", longitude);
                profileListIntent.putExtra("who", who);
                profileListIntent.putExtra("localities", localities);
                profileListIntent.putExtra("specialities", specialities);
                profileListIntent.putExtra("specialityHash", specialityHash);
                profileListIntent.putExtra("pointHash", pointHash);
                localities = new String[0];
                specialities = new String[0];
                specialityHash = new HashMap<>();
                pointHash = new HashMap<>();
                startActivity(profileListIntent);
            }
        });
//        JSONObject js = new JSONObject();
//        try {
//            js.put("dmrole", DM_Role);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            js.put("cityname", cityName);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            js.put("localityname", selectedParam);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            js.put("localitylat", latitude);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            js.put("localitylong", longitude);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            js.put("localitylat", latitude);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            js.put("localitylong", longitude);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            js.put("specialityid",specialitySelectedId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String jsString = js.toString();
        String url;
        url = Endpoints.BASE_URL + Endpoints.GET_DOCTORS_BY_LOCATION;
//        ConnectionAsyncTask task = new ConnectionAsyncTask();
        NetworkStuff(url);

        doctorsButton = findViewById(R.id.doctors_button);
        testLabsButton = findViewById(R.id.test_labs_button);
        pharmaciesButton = findViewById(R.id.pharmacies_button);
        vitalsButton = findViewById(R.id.vitals_button);

        doctorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whoRadioButton = "Doctors";
            }
        });

        vitalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whoRadioButton = "Vitals";
                specialitySpinner.setVisibility(View.GONE);
            }
        });

        final ImageView expandListButton = (ImageView) findViewById(R.id.custom_down_arrow);
        final LinearLayout expandList = (LinearLayout) findViewById(R.id.drop_down_two);
        final ImageView collapseListButton = (ImageView) findViewById(R.id.custom_up_arrow_two);
        final TextView filterBy = (TextView) findViewById(R.id.filter_by);
        expandListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whoRadioButton = "Doctors";
                expandList.setVisibility(View.VISIBLE);
                expandListButton.setVisibility(View.GONE);
                collapseListButton.setVisibility(View.VISIBLE);
                filterBy.setVisibility(View.GONE);
            }
        });

        collapseListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whoRadioButton = null;
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
                Log.d("res1234", "han " + ldoctorid);
            } catch (JSONException e) {
                Log.d("res1234", "nah");
                e.printStackTrace();
            }
            profiles = new ArrayList<>();
            for (int i = 0; i < ldoctorid.length(); i++) {
                JSONObject obj = null;
                obj = ldoctorid.optJSONObject(i);
                Log.d("res1234", "hai kya " + String.valueOf(obj));
                assert obj != null;
                String doctorId = obj.optString("ldoctorid");
                String doctorName = obj.optString("ldoctorname");
                String doctorGender = obj.optString("lgender");
                String doctorExperience = obj.optString("lexperience");
                String doctorSpeciality = obj.optString("lspecialityname");
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
                profiles.add(new Profile(doctorId, doctorName, doctorGender, doctorExperience, doctorSpeciality, doctorphoto, mbbsflag, mdflag, msflag, cliniclocationname, addressline1, addressline2, city, pincode, rating, normalamount, discountedamount, discountflag));
            }
        }
    }

    private void NetworkStuff(String url) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            profiles = new ArrayList<>();
                            JSONArray ldoctorid = new JSONObject(response).getJSONArray("info");
                            Log.d("abcde", "info " + ldoctorid);
                            for (int i = 0; i < ldoctorid.length(); i++) {
                                JSONObject obj = null;
                                obj = ldoctorid.optJSONObject(i);
                                Log.d("abcde", "hai kya " + String.valueOf(obj));
                                assert obj != null;
                                String doctorId = obj.optString("ldoctorid");
                                String doctorName = obj.optString("ldoctorname");
                                String doctorGender = obj.optString("lgender");
                                String doctorExperience = obj.optString("lexperience");
                                String doctorSpeciality = obj.optString("lspecialityname");
                                String image = obj.optString("ldoctorphoto");
                                //image = resizeBase64Image(image);
                                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                                Bitmap doctorphoto = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                //doctorImage.setImageBitmap(decodedByte);
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
                                Log.d("abcde", doctorId + doctorName + doctorGender + doctorExperience + doctorSpeciality + doctorphoto + mbbsflag + mdflag + msflag + cliniclocationname + addressline1 + addressline2 + city + pincode + rating + normalamount + discountedamount + discountflag);
                                profiles.add(new Profile(doctorId, doctorName, doctorGender, doctorExperience, doctorSpeciality, doctorphoto, mbbsflag, mdflag, msflag, cliniclocationname, addressline1, addressline2, city, pincode, rating, normalamount, discountedamount, discountflag));
                            }
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
                            // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            // call the constructor of CustomAdapter to send the reference and data to Adapter
                            ProfileAdapter customAdapter = new ProfileAdapter(ListActivity.this, profiles);
                            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("dmrole", DM_Role);
                params.put("cityname", cityName);
                if (selectedParam != null)
                    params.put("localityname", selectedParam);
                params.put("localitylat", String.valueOf(latitude));
                params.put("localitylong", String.valueOf(longitude));
//                params.put("localitylat", latitude);
//                params.put("localitylong", longitude);
                if (specialitySelectedId != null)
                    params.put("specialityid", specialitySelectedId);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
    public String resizeBase64Image(String base64image) {
        byte[] encodeByte = Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap img = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length, options);


        if (img.getHeight() <= 400 && img.getWidth() <= 400) {
            return base64image;
        }
        img = Bitmap.createScaledBitmap(img, 50, 50, false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] b = baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }
}