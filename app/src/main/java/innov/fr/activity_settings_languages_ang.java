package innov.fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class activity_settings_languages_ang extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_langages_ang);
        button = findViewById(R.id.btn_weight);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityWeight();
            }
        });

        button = findViewById(R.id.btn_results);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityResults();
            }
        });

        button = findViewById(R.id.btn_settings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySettings();
            }
        });

        button = findViewById(R.id.btn_connexion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityConnexion();
            }
        });

        button = findViewById(R.id.buttonANG);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLanguageANG();
            }
        });
    }

    public void openActivityWeight() {
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
    }
    public void openActivityResults() {
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
    }
    public void openActivitySettings() {
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
    }
    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
    }

    public void openActivityLanguageANG() {
        Intent intent = new Intent(this, activity_settings_languages_ang.class);
        startActivity(intent);
    }

}
