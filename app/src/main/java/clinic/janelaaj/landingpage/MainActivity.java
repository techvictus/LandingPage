package clinic.janelaaj.landingpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Welcome! Add yourself in chronological order.
 *
 * @author Sambit Mallick (sambit-m)
 * Experience level: n00b
 * Created by Sambit Mallick on 13.08.2018
 * Initial commit by sambit-m on 17.08.2018
 */

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private String paramSelectedLocality, cityName, who = "Doctors", specialitySelectedId;
    private JSONArray lists;
    private double latitude, longitude;
    private JSONObject jsonResponse;
    private ProgressDialog progressDialog;
    private Spinner locationSpinner, specialitySpinner, citySpinner;
    private TextView by;
    private ImageView collapseDropDown;
    private LinearLayout expandDropDown, searchView;
    private RadioButton doctorsButton, testLabsButton, pharmaciesButton, vitalsButton;
    private ImageButton searchButton;
    private RadioGroup groupOne, groupTwo;
    private String[] localities, specialities;
    private boolean locality = false;
    LatLng point;
    private HashMap<String, String> specialityHash;
    private HashMap<String, LatLng> pointHash;
    private boolean getSpecialityFlag = false;
    private int cityPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        cityPosition = 0;

        //Implementation of Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ImageView menu = (ImageView) findViewById(R.id.ham_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //Implementation  of Ad-View using RecyclerView {@link ImageModel}, {@link HorizontalRecyclerViewAdapter}
        RecyclerView mHorizontalRecyclerView = (RecyclerView) findViewById(R.id.horizontalRecyclerView);
        mHorizontalRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL));
        mHorizontalRecyclerView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.horizontalmargin));
        HorizontalRecyclerViewAdapter horizontalAdapter = new HorizontalRecyclerViewAdapter(fillWithData(), getApplication());
        //LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager = new CustomLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mHorizontalRecyclerView.setLayoutManager(horizontalLayoutManager);
        mHorizontalRecyclerView.setAdapter(horizontalAdapter);

        //Showing the list of Cities, where the service is available
        citySpinner = (Spinner) findViewById(R.id.city_spinner);
        final String[] city = new String[]{
                "Select City",
                "NEW DELHI",
                "GURUGRAM",
                "NOIDA",
                "FARIDABAD",
                "MEERUT",
                "JAIPUR",
                "BENGULURU",
                "CHENNAI",
                "PUNE",
                "KOLKATA",
                "HYDERABAD",
        };
        citySpinner = Helper.SetSpinner(MainActivity.this, city, citySpinner);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);
                    // If user change the default selection
                    // First item is disable and it is used for hint
                    // Notify the selected item text
                    locality = true;
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                    cityName = selectedItemText;
                    JSONObject js = new JSONObject();
                    try {
                        js.put("cityname", cityName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    cityPosition = position;
                    String jsString = js.toString();
                    String url;
                    url = Endpoints.BASE_URL + Endpoints.GET_LOCALITY;
                    DoingNetworkStuff(url);
//                    ConnectionAsyncTask localityTask = new ConnectionAsyncTask();
//                    localityTask.execute(url, jsString);
//                    //locality = false;
                    locationSpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Something
            }

        });
        //Check functions for buttons with 2*2 layout
        by = (TextView) findViewById(R.id.by);
        locationSpinner = (Spinner) findViewById(R.id.search);
        locationSpinner.setVisibility(View.GONE);

        collapseDropDown = (ImageView) findViewById(R.id.custom_up_arrow);
        specialitySpinner = (Spinner) findViewById(R.id.spinner_one);
        expandDropDown = (LinearLayout) findViewById(R.id.drop_down);
        searchButton = (ImageButton) findViewById(R.id.search_button);
        searchView = (LinearLayout) findViewById(R.id.search_view);

        groupOne = (RadioGroup) findViewById(R.id.radio_button);
        groupTwo = (RadioGroup) findViewById(R.id.radio_button_one);
        doctorsButton = (RadioButton) findViewById(R.id.doctors_button);
        testLabsButton = (RadioButton) findViewById(R.id.test_labs_button);
        pharmaciesButton = (RadioButton) findViewById(R.id.pharmacies_button);
        vitalsButton = (RadioButton) findViewById(R.id.vitals_button);

        doctorsButton.setChecked(true);  // Default setting
        doctorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                who = "Doctors";
                by.setVisibility(View.VISIBLE);
                specialitySpinner.setVisibility(View.VISIBLE);
                collapseDropDown.setVisibility(View.VISIBLE);
                //locationSpinner.setVisibility(View.VISIBLE);
                expandDropDown.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                groupOne.clearCheck();
                groupTwo.clearCheck();
                doctorsButton.setChecked(true);
                pharmaciesButton.setChecked(false);
                vitalsButton.setChecked(false);
//                Log.d("who","doc "+doctorsButton.isChecked());
//                Log.d("who","ProfileAdapter "+testLabsButton.isChecked());
//                Log.d("who","phar "+pharmaciesButton.isChecked());
//                Log.d("who","vit "+vitalsButton.isChecked());
            }
        });
        testLabsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                who = "Test-Labs";
                by.setVisibility(View.VISIBLE);
                specialitySpinner.setVisibility(View.VISIBLE);
                collapseDropDown.setVisibility(View.VISIBLE);
                //locationSpinner.setVisibility(View.VISIBLE);
                expandDropDown.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                groupOne.clearCheck();
                groupTwo.clearCheck();
                testLabsButton.setChecked(true);
                pharmaciesButton.setChecked(false);
                vitalsButton.setChecked(false);
//                Log.d("who","doc "+doctorsButton.isChecked());
//                Log.d("who","ProfileAdapter "+testLabsButton.isChecked());
//                Log.d("who","phar "+pharmaciesButton.isChecked());
//                Log.d("who","vit "+vitalsButton.isChecked());
            }
        });
        pharmaciesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                who = "Pharmacies";
                by.setVisibility(View.VISIBLE);
                specialitySpinner.setVisibility(View.VISIBLE);
                collapseDropDown.setVisibility(View.VISIBLE);
                //locationSpinner.setVisibility(View.VISIBLE);
                expandDropDown.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                groupOne.clearCheck();
                groupTwo.clearCheck();
                doctorsButton.setChecked(false);
                testLabsButton.setChecked(false);
                pharmaciesButton.setChecked(true);
//                Log.d("who","doc "+doctorsButton.isChecked());
//                Log.d("who","ProfileAdapter "+testLabsButton.isChecked());
//                Log.d("who","phar "+pharmaciesButton.isChecked());
//                Log.d("who","vit "+vitalsButton.isChecked());
            }
        });
        vitalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                who = "Vitals";
                by.setVisibility(View.GONE);
                specialitySpinner.setVisibility(View.GONE);
                collapseDropDown.setVisibility(View.VISIBLE);
                //locationSpinner.setVisibility(View.VISIBLE);
                expandDropDown.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                groupOne.clearCheck();
                groupTwo.clearCheck();
                doctorsButton.setChecked(false);
                testLabsButton.setChecked(false);
                vitalsButton.setChecked(true);

//                Log.d("who","doc "+doctorsButton.isChecked());
//                Log.d("who","ProfileAdapter "+testLabsButton.isChecked());
//                Log.d("who","phar "+pharmaciesButton.isChecked());
//                Log.d("who","vit "+vitalsButton.isChecked());
            }
        });
        collapseDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specialitySpinner.setVisibility(View.GONE);
                collapseDropDown.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                locationSpinner.setVisibility(View.GONE);
                by.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                groupOne.clearCheck();
                groupTwo.clearCheck();
//                Log.d("who","doc "+doctorsButton.isChecked());
//                Log.d("who","ProfileAdapter "+testLabsButton.isChecked());
//                Log.d("who","phar "+pharmaciesButton.isChecked());
//                Log.d("who","vit "+vitalsButton.isChecked());
            }
        });

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
                if(position==0)
                    return;
                paramSelectedLocality = selectedItemText;
                pointHash = new HashMap<>();
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject localityList = null;
                    try {
                        localityList = lists.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String localityName = null;
                    try {
                        assert localityList != null;
                        localityName = localityList.getString("llocalityname");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (paramSelectedLocality.equals(localityName)) {
                        latitude = Double.parseDouble(localityList.optString("llocality_lat"));
                        longitude = Double.parseDouble(localityList.optString("llocality_long"));
                    }

                    point = new LatLng(Double.parseDouble(localityList.optString("llocality_lat")), Double.parseDouble(localityList.optString("llocality_long")));
                    Log.d("abcdehash", localityName+ String.valueOf(point.latitude)+ String.valueOf(point.longitude));
                    pointHash.put(localityName,point);
                }
                Log.d("abcdehash", "???"+ String.valueOf(pointHash.keySet()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (!getSpecialityFlag) {
            String url;
            url = Endpoints.BASE_URL + Endpoints.GET_SPECIALITY;
            DoingNetworkStuff(url);
//            ConnectionAsyncTask specialityTask = new ConnectionAsyncTask();
//            specialityTask.execute(url);
            getSpecialityFlag = true;
        }
//        final List<String> spinnerList = new ArrayList<>(Arrays.asList(list));
//        // Initializing an ArrayAdapter
//        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                this, R.layout.spinner_item, spinnerList) {
//            @Override
//            public boolean isEnabled(int position) {
//                if (position == 0) {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                } else {
//                    return true;
//                }
//            }
//
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if (position == 0) {
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                } else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//        specialitySpinner.setAdapter(spinnerArrayAdapter);

        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
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

        // Passing values to the {@link ListActivity}
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cityPosition == 0) {
                    Toast.makeText(getApplicationContext(), "Please select your city!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent profileListIntent = new Intent(MainActivity.this, ListActivity.class);
                profileListIntent.putExtra("CityName", cityName);
                profileListIntent.putExtra("SpecialitySelectedId", specialitySelectedId);
                profileListIntent.putExtra("paramSelectedLocality", paramSelectedLocality);
                profileListIntent.putExtra("paramLatitude", latitude);
                profileListIntent.putExtra("paramLongitude", longitude);
                profileListIntent.putExtra("who", who);
                profileListIntent.putExtra("localities",localities);
                profileListIntent.putExtra("specialities",specialities);
                profileListIntent.putExtra("specialityHash",specialityHash);
                profileListIntent.putExtra("pointHash",pointHash);
                //Log.d("abcdehash", String.valueOf(pointHash.size()));
                localities = new String[0];
                specialities = new String[0];
                specialityHash = new HashMap<>();
                pointHash = new HashMap<>();
                startActivity(profileListIntent);
            }
        });
    }


    /**
     * Inserting the pictures using
     * {@link ImageModel} Data Structure
     */
    private ArrayList<ImageModel> fillWithData() {
        ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
        ImageModel imageModel0 = new ImageModel();
        imageModel0.setId(System.currentTimeMillis());
        imageModel0.setImageName("img1");
        imageModel0.setImagePath(R.drawable.testimg);
        imageModelArrayList.add(imageModel0);

        ImageModel imageModel1 = new ImageModel();
        imageModel1.setId(System.currentTimeMillis());
        imageModel1.setImageName("img2");
        imageModel1.setImagePath(R.drawable.testimg);
        imageModelArrayList.add(imageModel1);

        ImageModel imageModel2 = new ImageModel();
        imageModel2.setId(System.currentTimeMillis());
        imageModel2.setImageName("img1");
        imageModel2.setImagePath(R.drawable.testimg);
        imageModelArrayList.add(imageModel2);

        ImageModel imageModel3 = new ImageModel();
        imageModel3.setId(System.currentTimeMillis());
        imageModel3.setImageName("img2");
        imageModel3.setImagePath(R.drawable.testimg);
        imageModelArrayList.add(imageModel3);

        ImageModel imageModel4 = new ImageModel();
        imageModel4.setId(System.currentTimeMillis());
        imageModel4.setImageName("img1");
        imageModel4.setImagePath(R.drawable.testimg);
        imageModelArrayList.add(imageModel4);

        ImageModel imageModel5 = new ImageModel();
        imageModel5.setId(System.currentTimeMillis());
        imageModel5.setImageName("img2");
        imageModel5.setImagePath(R.drawable.testimg);
        imageModelArrayList.add(imageModel5);
        return imageModelArrayList;
    }

    /**
     * Creating another thread to connect Endpoint and receive response
     * {@link ConnectionUtil}
     */
    private class ConnectionAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            if (progressDialog != null)
                progressDialog.cancel();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Please wait...", true, true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject result = null;
            try {
                if (strings.length == 2)
                    result = ConnectionUtil.postMethod(strings[0], strings[1]);
                else
                    result = ConnectionUtil.postMethod(strings[0], null);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            jsonResponse = result;
            JSONArray info = null;
            try {
                assert jsonResponse != null;
                if (locality)
                    info = jsonResponse.getJSONArray("info");
                else
                    info = jsonResponse.getJSONArray("sparr");
                lists = info;
            } catch (JSONException e) {
                //e.printStackTrace();
                Alerts.ServerDown(MainActivity.this);
                return;
            }
            if (locality)
                GetLocalityArray(info);
            else
                GetSpeciality(info);
            locality = false;
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    private class CustomLinearLayoutManager extends LinearLayoutManager {
        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            final LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(recyclerView.getContext()) {
                        private static final float MILLISECONDS_PER_INCH = 200f;

                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return CustomLinearLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        @Override
                        protected float calculateSpeedPerPixel
                                (DisplayMetrics displayMetrics) {
                            return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                        }
                    };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

    private void GetLocalityArray(JSONArray info) {
        localities = new String[info.length() + 2];
        if (info.length() == 0) {
            localities[0] = "Coming Soon!";
        } else {
            localities[0] = "Select your location";
            localities[1] = "Current Location";
        }
        for (int i = 0; i < info.length(); i++) {
            JSONObject localityList = null;
            try {
                localityList = info.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String localityName = null;
            try {
                localityName = localityList.getString("llocalityname");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            localities[i + 2] = localityName;
        }
        locationSpinner = Helper.SetSpinner(MainActivity.this, localities, locationSpinner);
    }

    private void GetSpeciality(JSONArray info) {
        specialityHash = new HashMap<>();
        specialities = new String[info.length() + 1];
        specialities[0] = "Select Speciality";
        for (int i = 0; i < info.length(); i++) {
            JSONObject obj = null;
            try {
                obj = info.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            assert obj != null;
            String id = obj.optString("id");
            String name = obj.optString("name");
            specialityHash.put(name, id);
            specialities[i + 1] = name;
        }
        specialitySpinner = Helper.SetSpinner(MainActivity.this, specialities, specialitySpinner);
    }

    private void DoingNetworkStuff(String url){
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("abcde", "locality " + locality);
                        try {
                            if(locality) {
                                JSONArray jsonResponse = new JSONObject(response).getJSONArray("info");
                                Log.d("abcde", "info " + jsonResponse);
                                lists = jsonResponse;
                                GetLocalityArray(jsonResponse);
                            }
                            else {
                                JSONArray jsonResponse = new JSONObject(response).getJSONArray("sparr");
                                Log.d("abcde", "sparr " + jsonResponse);
                                GetSpeciality(jsonResponse);
                            }
                            locality = false;
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                if (locality)
                    params.put("cityname", cityName);
                else{
                    return null;
                }
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
}