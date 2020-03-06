package innov.fr;
import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ImageView click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        this.click=(ImageView) findViewById(R.id.click);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), ActivityTest.class);
                startActivity(otherActivity);
                finish();
            }
        });


    }
}
