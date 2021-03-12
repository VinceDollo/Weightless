package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    private EditText username,userphone;
    private Button edit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    int cpoubelle=0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String KEY_t4="trophy4", uid ="Undefined";
    private static final String TAG = "UpdateProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        uid=firebaseAuth.getUid();

        username = (EditText)findViewById(R.id.etNameupdate);
        userphone = (EditText)findViewById(R.id.etPhoneupdate);
        edit = (Button) findViewById(R.id.buttonupdate);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                cpoubelle=userProfile.getCompteurPoubelle();
                username.setText(userProfile.getUserName());
                userphone.setText(userProfile.getUserPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfile.this, error.getCode(),Toast.LENGTH_SHORT).show();
            }
         });
     edit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             saveNote();
             String name = username.getText().toString();
             String phone = userphone.getText().toString();

             UserProfile userProfile = new UserProfile(name,firebaseUser.getEmail(),phone,cpoubelle);
             databaseReference.setValue(userProfile);
             openProfileActivity();
         }
     });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
}
    public void openProfileActivity() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        UpdateProfile.this.finish();
    }
    public void saveNote() {
        addNotification();
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_t4,"Incognito");

        db.collection(uid).document("badges").update(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateProfile.this, "Sucess", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
    // Creates and displays a notification
    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UpdateProfile.this,"My Notification");
        builder.setContentTitle("Weightless");
        builder.setContentText("Trophé débloqué");
        builder.setSmallIcon(R.drawable.logo);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(UpdateProfile.this);
        managerCompat.notify(1,builder.build());
    }
}