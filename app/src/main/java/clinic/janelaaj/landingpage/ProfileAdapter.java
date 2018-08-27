package clinic.janelaaj.landingpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        ImageView doccPhoto = (ImageView) listItemView.findViewById(R.id.profile_pic);
        doccPhoto.setImageBitmap(currentProfile.getDoctorPhoto());

        TextView docName = (TextView) listItemView.findViewById(R.id.doc_name);
        docName.setText(currentProfile.getDoctorName());

        TextView docQualification = (TextView) listItemView.findViewById(R.id.profile_qualification);
        String qualification = "";
        if(currentProfile.getMbbsflag().equals("Y"))
            qualification+="M.B.B.S";
        if(currentProfile.getMdflag().equals("Y")) {
            if(!qualification.equals(""))
                qualification+=", ";
            qualification += "M.D";
        }
        if(currentProfile.getMsflag().equals("Y")) {
            if (!qualification.equals(""))
                qualification += ", ";
            qualification += "M.S";
        }
        if(qualification.equals(""))
            qualification+="No Information Available";
        docQualification.setText(qualification);

        TextView docAddress = (TextView) listItemView.findViewById(R.id.profile_address);
        String address = "";
        address+=currentProfile.getCliniclocationname();
        if(!address.equals(""))
            address+=", ";
        address+=currentProfile.getAddressline1();
        if(!address.equals(""))
            address+=", ";
        address+=currentProfile.getAddressline2();
        if(!address.equals(""))
            address+=", ";
        address+=currentProfile.getCity();
        if(!address.equals(""))
            address+=", ";
        address+=currentProfile.getPincode();
        if(address.equals(""))
            address+="No Information Available";
        docAddress.setText(address);

        TextView priceTag = (TextView) listItemView.findViewById(R.id.price_tag);
        int price = Integer.valueOf(currentProfile.getNormalamount());
        if(price<=300)
            priceTag.setText("₹");
        else if(price>300 && price<=600)
            priceTag.setText("₹₹");
        else if(price>600)
            priceTag.setText("₹₹₹");


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
