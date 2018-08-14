package clinic.janelaaj.landingpage;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;


import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

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
                Intent landingPageIntent=new Intent(ListActivity.this,MainActivity.class);
                startActivity(landingPageIntent);
            }
        });

        ArrayList<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile("a"));
        profiles.add(new Profile("b"));
        profiles.add(new Profile("c"));
        profiles.add(new Profile("d"));
        profiles.add(new Profile("e"));
        profiles.add(new Profile("f"));
        profiles.add(new Profile("g"));
        ProfileAdapter adapter = new ProfileAdapter(this, profiles);
        ListView profileListView = (ListView) findViewById(R.id.list);
        profileListView.setAdapter(adapter);

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
}
