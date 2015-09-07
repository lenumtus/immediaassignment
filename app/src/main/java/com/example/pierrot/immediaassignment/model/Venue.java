package com.example.pierrot.immediaassignment.model;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.example.pierrot.immediaassignment.MainActivity;
import com.example.pierrot.immediaassignment.R;
import com.example.pierrot.immediaassignment.picActivity;


/**
 * This class serves as both a model object as well as a view holder for the object.
 */
public class Venue implements Comparable<Venue> {
    private String mName;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    private int mDistance;
    private boolean mReady = false;
    private String mImg ;
    private TextView txtDistance;

    ImageView imageView ;

    boolean isImageFitToScreen;
    Bitmap mIcon_val ;
    public Venue(String name, String address ,String img , int distance, double latitude, double longitude) {
        mName = name;
        mAddress = address;
        mImg = img ;
        mDistance = distance;
        mLatitude = latitude;
        mLongitude = longitude;

    }

    public void populate(View view) {

        ((TextView)view.findViewById(R.id.txtName))
                .setText(mName);
        (txtDistance = (TextView)view.findViewById(R.id.txtDistance))
                .setText(formatDistance());


       imageView = (ImageView) view.findViewById(R.id.txtimg);

        UrlImageViewHelper.setUrlDrawable(imageView, mImg, R.mipmap.apple);





        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View view) {
                //Toast.makeText(view.getContext() , "test" , Toast.LENGTH_LONG).show();

                  /**if(isImageFitToScreen) {
                 isImageFitToScreen=false;
                 imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                 imageView.setAdjustViewBounds(true);
                 }else{
                 isImageFitToScreen=true;
                 imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                 imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                 }**/

                Intent intent = new Intent(view.getContext(), picActivity.class);
                intent.putExtra("image", mImg);
                intent.putExtra("name" , mName);
                intent.putExtra("address", mAddress);
                intent.putExtra("distance" ,mDistance);
                view.getContext().startActivity(intent);

            }
        });
    mReady = true;

    }

    @Override
    public int compareTo(Venue another) {
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
        mDistance = (int)results[0];
        if (mReady)
            txtDistance.setText(formatDistance());
    }

}
