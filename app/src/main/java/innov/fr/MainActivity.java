package innov.fr;
import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public void bouton1(view v){
        Button btn1 = (Button) findViewById(R.id.btn1);
        AlphaAnimation alpha1 = new AlphaAnimation(0f, 1f);
        alpha1.setDuration(500);
        btn1.startAnimation(alpha1);
    }
    public void bouton2(view v){
        Button btn2 = (Button) findViewById(R.id.btn2);
        AlphaAnimation alpha2 = new AlphaAnimation(0f, 1f);
        alpha2.setDuration(500);
        btn2.startAnimation(alpha2);
    }
    public void bouton3(view v){
        Button btn3 = (Button) findViewById(R.id.btn3);
        AlphaAnimation alpha3 = new AlphaAnimation(0f, 1f);
        alpha3.setDuration(500);
        btn3.startAnimation(alpha3);
    }
    public void bouton4(view v){
        Button btn4 = (Button) findViewById(R.id.btn4);
        AlphaAnimation alpha4 = new AlphaAnimation(0f, 1f);
        alpha4.setDuration(500);
        btn4.startAnimation(alpha4);
    }

}
