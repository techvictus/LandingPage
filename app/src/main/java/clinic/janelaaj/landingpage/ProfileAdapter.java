package clinic.janelaaj.landingpage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder>{
    private ArrayList<Profile> mProfiles;

    public ProfileAdapter(Context context, ArrayList<Profile> profiles){
        Context mContext = context;
        mProfiles = profiles;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.docPhoto.setImageBitmap(mProfiles.get(position).getDoctorPhoto());
        holder.docName.setText(Html.fromHtml( "<b>" + "Dr. "+ mProfiles.get(position).getDoctorName()+"</b>"));

    }

    @Override
    public int getItemCount() {
        return mProfiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView docPhoto;
        TextView docName;
        RelativeLayout collapse;
        LinearLayout expand;

        MyViewHolder(View listItemView) {
            super(listItemView);
            // get the reference of item view's
            docPhoto = (ImageView) listItemView.findViewById(R.id.profile_pic);
            docName = (TextView) listItemView.findViewById(R.id.doc_name);
            collapse = (RelativeLayout) listItemView.findViewById(R.id.first);
            expand = (LinearLayout) listItemView.findViewById(R.id.list_expand);
            collapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    collapse.setVisibility(View.GONE);
                    expand.setVisibility(View.VISIBLE);
                }
            });
            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    collapse.setVisibility(View.VISIBLE);
                    expand.setVisibility(View.GONE);
                }
            });
        }
    }
}
