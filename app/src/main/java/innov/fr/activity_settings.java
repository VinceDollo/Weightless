package innov.fr;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class activity_settings extends AppCompatActivity {
    private Button button;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth=FirebaseAuth.getInstance();

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
                openActivityProfil();
            }
        });

        button = findViewById((R.id.language_settings_button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLanguages();
            }
        });

        button = findViewById((R.id.btnhelp));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAide();
            }
        });

        button = findViewById((R.id.Themes_button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityTheme();
            }
        });

        button = findViewById(R.id.buttondeco);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        button = findViewById(R.id.balance_settings_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySettingBalance();
            }
        });

    }
    public void openActivityWeight() {
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
        activity_settings.this.finish();
    }
    public void openActivityResults() {
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
        activity_settings.this.finish();
    }
    public void openActivitySettings() {
        finish();
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
    }

    public void openActivityLanguages() {
        Intent intent = new Intent(this, activity_settings_languages.class);
        startActivity(intent);
        activity_settings.this.finish();
    }

    public void openActivityAide() {
        Intent intent = new Intent(this, activity_settings_aide.class);
        startActivity(intent);
        activity_settings.this.finish();
    }

    public void openActivityTheme() {
        Intent intent = new Intent(this, activity_settings_themes.class);
        startActivity(intent);
        activity_settings.this.finish();
    }
    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        activity_settings.this.finish();
    }
    public void openActivityProfil() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        activity_settings.this.finish();
    }

    public void openActivitySettingBalance() {
        Intent intent = new Intent(this, activity_settings_balance.class);
        startActivity(intent);
        activity_settings.this.finish();
    }

    private void Logout() {
        firebaseAuth.signOut();
        openActivityConnexion();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}



