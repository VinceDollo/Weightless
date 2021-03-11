package innov.fr;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_trophy extends AppCompatActivity {
    private Button button;
    private static final String TAG = "Activity_trophy";
    private static final String KEY_t1 = "trophy1";
    private ImageView iv1;
    private TextView tv1;
    private String uid="Undefined";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy);
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

        button = findViewById(R.id.btn_Load_badges);

        firebaseAuth = FirebaseAuth.getInstance();
        uid=firebaseAuth.getUid();

        iv1=findViewById(R.id.ivTrophy1);
        tv1=findViewById(R.id.tvTrophy1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection(uid).document("badges").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    if (!documentSnapshot.getString(KEY_t1).equals("false")) {
                                        iv1.setImageResource(R.drawable.logo);
                                        tv1.setText(documentSnapshot.getString(KEY_t1));
                                    }
                                }
                                else{
                                    Toast.makeText(Activity_trophy.this, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Activity_trophy.this, "Fail", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
        });
    }
    public void openActivityWeight() {
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
        Activity_trophy.this.finish();
    }
    public void openActivityResults() {
        finish();
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
    }
    public void openActivitySettings() {
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
        Activity_trophy.this.finish();
    }
    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        Activity_trophy.this.finish();
    }
    public void openActivityProfil() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        Activity_trophy.this.finish();
    }
    private void Logout() {
        firebaseAuth.signOut();
        openActivityConnexion();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    private void openContact(){
        Intent intent = new Intent(this, ContactWeightless.class);
        startActivity(intent);
        Activity_trophy.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
            }
            case R.id.adviceMenu:{
                openContact();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}