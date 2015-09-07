package com.example.pierrot.immediaassignment.model;

import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.example.pierrot.immediaassignment.R;
import com.example.pierrot.immediaassignment.VenueListActivity;
import com.example.pierrot.immediaassignment.picActivity;

/**
 * This class serves as both a model object as well as a view holder for the object.
 */
public class location implements Comparable<location>{
    private String mName;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    private int mDistance;

    private String mImg ;
    private TextView txtDistance;
    private boolean mReady = false;

    public location(String name, String address ,String img , int distance, double latitude, double longitude) {
        mName = name;
        mAddress = address;
        mImg = img ;
        mDistance = distance;
        mLatitude = latitude;
        mLongitude = longitude;;

    }

    public void populate(View view) {
        ((TextView)view.findViewById(R.id.txtName))
                .setText(mName);


        ImageView imageView = (ImageView) view.findViewById(R.id.txtimg);

        UrlImageViewHelper.setUrlDrawable(imageView, mImg, R.mipmap.apple);

        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View view) {

                Area.setCity(mName);
                Intent intent = new Intent(view.getContext(), VenueListActivity.class);

                view.getContext().startActivity(intent);

            }
        });

        mReady = true;
    }

    public int compareTo(location another) {
        return this.mDistance - another.mDistance;
    }

    //format the distance
    public String formatDistance() {
        return String.format("%d m", mDistance);
    }

    //update the distance shown in the list
    public void updateDistance(Location newLocation) {
        float[] results = new float[3];
        Location.distanceBetween(newLocation.getLatitude(), newLocation.getLongitude(), mLatitude, mLongitude, results);
        mDistance = (int) results[0];
        //if (mReady)
        //txtDistance.setText(formatDistance());
        // }
    }


}
