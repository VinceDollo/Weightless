package innov.fr;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class activity_weight extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "activity_weight";
    private EditText etpoids;
    private static final String KEY_DAY = "day", KEY_MONTH = "month", KEY_POIDS="poids", KEY_YEAR="year";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username = "Undefined", uid="Undefined";
    private String phone = "Undefined";
    private String numberPoubelle = "Undefined";
    private int cpb=0;
    private static final String KEY_t5 = "trophy5",KEY_t6 = "trophy6",KEY_t7 = "trophy7",KEY_t8 = "trophy8";
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

        textView = findViewById(R.id.test);

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
        button = findViewById(R.id.buttonESP);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightFormEsp();
            }
        });


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
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void saveNote(View v) {
        String poids = etpoids .getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_DAY,day);
        note.put(KEY_MONTH,month);
        note.put(KEY_YEAR,year);
        note.put(KEY_POIDS,poids );

        db.collection(uid).document(numberPoubelle).set(note)
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
    // Creates and displays a notification
    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity_weight.this,"My Notification");
        builder.setContentTitle("Weightless");
        builder.setContentText("Trophé débloqué");
        builder.setSmallIcon(R.drawable.logo);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(activity_weight.this);
        managerCompat.notify(1,builder.build());
    }
    public void addPoubelle(View v) {
        cpb++;
        if((cpb==5)||(cpb==20)||(cpb==50)||(cpb==100)){
            addNotification();
            Map<String, Object> note = new HashMap<>();
            if(cpb==5){
                note.put(KEY_t5,"Recycleur débutant");
            }
            else if(cpb==20){
                note.put(KEY_t6,"Recycleur occasionel");
            }
            else if(cpb==50){
                note.put(KEY_t7,"Recycleur accompli");
            }
            else {
                note.put(KEY_t8,"Légende vivante");
            }
            db.collection(uid).document("badges").update(note)
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
        UserProfile userProfile = new UserProfile(username,firebaseUser.getEmail(),phone,cpb);
        databaseReference.setValue(userProfile);
    }


    private void getWeightFormEsp()  {
        new Thread(){
            public void run(){
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer buffer = ByteBuffer.allocate(2045);
                int port = 51000;
                String[] temp = Shared.getIpAddress(getApplicationContext()).split("\\.");
                String target = temp[0]+"."+temp[1]+"."+temp[2]+"."+"253";
                try{
                    SocketChannel client = SocketChannel.open();
                    client.connect(new InetSocketAddress(target, 52000));
                    client.write(charset.encode("get"));
                    while(true)
                        if(client.read(buffer)>0){
                            buffer.flip();
                            String res = new String(buffer.array());
                            final double weight = Double.valueOf(res);
                            //System.out.println(weight);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(String.valueOf(weight));
                                }
                            });
                            buffer.clear();
                            break;
                        }
                    client.close();
                }catch (UnknownHostException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }


        }.start();


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



