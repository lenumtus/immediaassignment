package com.example.pierrot.immediaassignment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.example.pierrot.immediaassignment.R;

public class picActivity extends Activity {
    Bitmap bmp = null;
    ImageView imagefull ;
    TextView addresstext , name , distance ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        Intent intent = this.getIntent();
        imagefull = (ImageView)findViewById(R.id.fullimage);
        addresstext=(TextView)findViewById(R.id.resulttext);
        name=(TextView)findViewById(R.id.Name);
        distance =(TextView)findViewById(R.id.Distance);
         String mImg = intent.getStringExtra("image");
        String textaddress = intent.getStringExtra("address");
        String textname = intent.getStringExtra("name");
        String textdistance = intent.getStringExtra("distance");
        addresstext.setText(textaddress);
        name.setText(textname);
        distance.setText(textdistance);
        UrlImageViewHelper.setUrlDrawable(imagefull, mImg, R.mipmap.apple);


    }


}
