package clinic.janelaaj.landingpage;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {

    public static Spinner SetSpinner(Context context, String[] stringArray, Spinner spinner) {
        final List<String> SpinnerList = new ArrayList<>(Arrays.asList(stringArray));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> SpinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, SpinnerList) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        SpinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(SpinnerArrayAdapter);
        return spinner;
    }
}
