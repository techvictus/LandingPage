package clinic.janelaaj.landingpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends ArrayAdapter<Profile>{

    public ProfileAdapter(Context context, ArrayList<Profile> profiles) {
        super(context, 0, profiles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.profile_list_item, parent, false);
        }

        Profile currentProfile = getItem(position);

        TextView profileName = (TextView) listItemView.findViewById(R.id.profile_name);

        profileName.setText(currentProfile.getDoctorName());

        final RelativeLayout collapse = (RelativeLayout) listItemView.findViewById(R.id.first);
        final LinearLayout expand = (LinearLayout) listItemView.findViewById(R.id.list_expand);
        ImageView upCollapse = (ImageView) listItemView.findViewById(R.id.up_collapse);
        ImageView downExpand = (ImageView) listItemView.findViewById(R.id.down_expand);
        downExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collapse.setVisibility(View.GONE);
                expand.setVisibility(View.VISIBLE);
            }
        });
        upCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapse.setVisibility(View.VISIBLE);
                expand.setVisibility(View.GONE);
            }
        });

        return listItemView;
    }
}
