package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Activity_Trophies extends AppCompatActivity {

    private Button button;
    private static final String TAG = "Activity_trophy";
    private static final String KEY_t1 = "trophy1", KEY_t2 = "trophy2";
    private ImageView iv1,iv2;
    private TextView tv1,tv2;
    private String uid="Undefined";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__trophies);
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
        iv2=findViewById(R.id.ivTrophy2);
        tv2=findViewById(R.id.tvTrophy2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
                Toast.makeText(Activity_Trophies.this, "texttt", Toast.LENGTH_SHORT).show();
                db.collection(uid).document("badges").get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    if (!documentSnapshot.getString(KEY_t1).equals("false")) {
                                        iv1.setImageResource(R.drawable.logo);
                                        tv1.setText(documentSnapshot.getString(KEY_t1));
                                    }
                                    if (!documentSnapshot.getString(KEY_t2).equals("false")) {
                                        iv2.setImageResource(R.drawable.trophy2);
                                        tv2.setText(documentSnapshot.getString(KEY_t2));
                                    }
                                }
                                else{
                                    Toast.makeText(Activity_Trophies.this, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Activity_Trophies.this, "Fail", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification",NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    public void openActivityWeight() {
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
        Activity_Trophies.this.finish();
    }
    public void openActivityResults() {
        finish();
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
    }
    public void openActivitySettings() {
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
        Activity_Trophies.this.finish();
    }


    // Creates and displays a notification
    private void addNotification() {
       NotificationCompat.Builder builder = new NotificationCompat.Builder(Activity_Trophies.this,"My Notification");
       builder.setContentTitle("Weightless");
       builder.setContentText("Chargement trophés réalisé");
       builder.setSmallIcon(R.drawable.logo);
       builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Activity_Trophies.this);
        managerCompat.notify(1,builder.build());
    }




    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        Activity_Trophies.this.finish();
    }
    public void openActivityProfil() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        Activity_Trophies.this.finish();
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
        Activity_Trophies.this.finish();
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