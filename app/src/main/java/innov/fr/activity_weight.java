package innov.fr;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class activity_weight extends AppCompatActivity {
    private Button button;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "activity_weight";
    private EditText etpoids;
    private static final String KEY_DAY = "day", KEY_MONTH = "month", KEY_POIDS="poids", KEY_YEAR="year";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username = "Undefined", uid="Undefined";
    private String phone = "Undefined";
    private String numberPoubelle = "Undefined";
    private int cpb=0;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    String currentTime = Calendar.getInstance().getTime().toString();
    String[] valueTime = currentTime.split(" ");
    String day = valueTime[2];
    String month =valueTime[1];
    String year = valueTime[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);
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

        etpoids = findViewById(R.id.etwheightpoids );

        button = findViewById(R.id.buttonweightGO);
        button = findViewById(R.id.buttonweightADDPB);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                username =userProfile.getUserName();
                phone = userProfile.getUserPhone();
                cpb=userProfile.getCompteurPoubelle();
                numberPoubelle= "poubellen"+String.valueOf(cpb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_weight.this, error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
        uid=firebaseAuth.getUid();
    }

    public void saveNote(View v) {
        String poids = etpoids .getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_DAY,day);
        note.put(KEY_MONTH,month);
        note.put(KEY_YEAR,year);
        note.put(KEY_POIDS,poids );

        db.collection(username).document(numberPoubelle).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity_weight.this, "Sucess", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_weight.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
    public void addPoubelle(View v) {
        cpb++;
        UserProfile userProfile = new UserProfile(username,firebaseUser.getEmail(),phone,cpb);
        databaseReference.setValue(userProfile);
    }
    public void openActivityWeight() {
        finish();
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
    }
    public void openActivityResults() {
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
        activity_weight.this.finish();
    }
    public void openActivitySettings() {
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
        activity_weight.this.finish();
    }
    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        activity_weight.this.finish();
    }
    public void openActivityProfil() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        activity_weight.this.finish();
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



