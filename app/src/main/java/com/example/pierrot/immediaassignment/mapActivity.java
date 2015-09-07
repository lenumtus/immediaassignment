
package com.example.pierrot.immediaassignment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class mapActivity extends Activity {
    Button start , stop ;
    ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);
        im= (ImageView)findViewById(R.id.img);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });


    stop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            float u = im.getRotationX();
            stopAnimation();
            Toast.makeText(getApplicationContext()," "+u , Toast.LENGTH_LONG).show();
        }
    });

}
    private void startAnimation(){
        Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
        im.startAnimation(rotation);
    }
    private void stopAnimation(){
        im.clearAnimation();
    }
}

