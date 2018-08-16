package clinic.janelaaj.landingpage;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.support.v7.app.ActionBarDrawerToggle;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

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
        ImageView menu =(ImageView) findViewById(R.id.ham_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        RecyclerView mHorizontalRecyclerView = (RecyclerView) findViewById(R.id.horizontalRecyclerView);
        mHorizontalRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.HORIZONTAL));
        mHorizontalRecyclerView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.horizontalmargin));

        HorizontalRecyclerViewAdapter horizontalAdapter = new HorizontalRecyclerViewAdapter(fillWithData(), getApplication());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mHorizontalRecyclerView.setLayoutManager(horizontalLayoutManager);
        mHorizontalRecyclerView.setAdapter(horizontalAdapter);

        ImageView inbox = (ImageView) findViewById(R.id.inbox);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileListIntent=new Intent(MainActivity.this,ListActivity.class);
                startActivity(profileListIntent);
            }
        });


        /*
         *  Check functions for buttons with 2*2 layout
         */

        final TextView by = (TextView) findViewById(R.id.by);
        final SearchView searchView = (SearchView) findViewById(R.id.search);
        final ImageView collapseDropDown = (ImageView) findViewById(R.id.custom_up_arrow);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_one);
        final LinearLayout expandDropDown = (LinearLayout) findViewById(R.id.drop_down);


        final RadioButton doctorsButton = (RadioButton) findViewById(R.id.doctors_button);
        final RadioButton testLabsButton = (RadioButton) findViewById(R.id.test_labs_button);
        final RadioButton pharmaciesButton = (RadioButton) findViewById(R.id.pharmacies_button);
        final RadioButton vitalsButton = (RadioButton) findViewById(R.id.vitals_button);

        doctorsButton.setChecked(true);  // Default setting


        doctorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                by.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                collapseDropDown.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                expandDropDown.setVisibility(View.VISIBLE);
                doctorsButton.setChecked(true);
                pharmaciesButton.setChecked(false);
                vitalsButton.setChecked(false);
            }
        });
        testLabsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                by.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                collapseDropDown.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                expandDropDown.setVisibility(View.VISIBLE);
                testLabsButton.setChecked(true);
                pharmaciesButton.setChecked(false);
                vitalsButton.setChecked(false);
            }
        });
        pharmaciesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                by.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                collapseDropDown.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                expandDropDown.setVisibility(View.VISIBLE);
                doctorsButton.setChecked(false);
                testLabsButton.setChecked(false);
                pharmaciesButton.setChecked(true);
            }
        });
        vitalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                by.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                collapseDropDown.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                expandDropDown.setVisibility(View.VISIBLE);
                doctorsButton.setChecked(false);
                testLabsButton.setChecked(false);
                vitalsButton.setChecked(true);
            }
        });



        collapseDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.GONE);
                collapseDropDown.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                by.setVisibility(View.GONE);
            }
        });
        String[] list = new String[]{
                "Select an item...",
                "Location",
                "Experience",
                "Price",
        };
        final List<String> spinnerList = new ArrayList<>(Arrays.asList(list));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,spinnerList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public ArrayList<ImageModel> fillWithData() {
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
}