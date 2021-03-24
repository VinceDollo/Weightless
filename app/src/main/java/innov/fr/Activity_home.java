package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Activity_home extends AppCompatActivity {

    private Button button, btnGo;
    private FirebaseAuth firebaseAuth;
    private String resultat = "";
    private TextView textGo;
    private static final String KEY_DAY = "day", KEY_MONTH = "month", KEY_POIDS="poids", KEY_YEAR="year";
    FirebaseFirestore db;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    private String username = "Undefined", uid="Undefined";
    private String phone = "Undefined";
    private String numberPoubelle = "Undefined";
    private int cpb=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        btnGo = findViewById(R.id.btn_Go1);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*openActivityWeight();*/
                getWeightFormEsp();
                //saveOnFireBase();
            }
        });

        textGo = findViewById(R.id.Go);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        db = FirebaseFirestore.getInstance();
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
                //Toast.makeText(activity_weight.this, error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeightFormEsp()  {
        System.out.println("Dans getWeightFormEsp");
        new Thread(){
            public void run(){
                System.out.println("Dans le thread");
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer buffer = ByteBuffer.allocate(2045);
                int port = 51000;
                String[] temp = Shared.getIpAddress(getApplicationContext()).split("\\.");
                String target = temp[0]+"."+temp[1]+"."+temp[2]+"."+"6";
                try{
                    SocketChannel client = SocketChannel.open();
                    client.connect(new InetSocketAddress(target, 51000));
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
                                    resultat = String.valueOf(weight);
                                    //btnGo.setText(resultat);
                                    textGo.setText(resultat);
                                    //String poids = textGo.getText().toString();
                                    String poids = resultat;
                                    String currentTime = Calendar.getInstance().getTime().toString();
                                    String[] valueTime = currentTime.split(" ");
                                    String day = valueTime[2];
                                    String month =valueTime[1];
                                    String year = valueTime[5];


                                    Map<String, Object> note = new HashMap<>();
                                    note.put(KEY_DAY,day);
                                    note.put(KEY_MONTH,month);
                                    note.put(KEY_YEAR,year);
                                    note.put(KEY_POIDS,poids );
                                    ++cpb;
                                    numberPoubelle = "poubellen"+String.valueOf(cpb);

                                    db.collection(firebaseAuth.getUid()).document(numberPoubelle).set(note)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Toast.makeText(activity_weight.this, "Sucess", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Toast.makeText(activity_weight.this, "Fail", Toast.LENGTH_SHORT).show();
                                                    //Log.d(TAG, e.toString());
                                                }
                                            });

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
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
        Activity_home.this.finish();
    }
    public void openActivityResults() {
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
        Activity_home.this.finish();
    }
    public void openActivitySettings() {
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
        Activity_home.this.finish();
    }
    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        Activity_home.this.finish();
    }
    public void openActivityProfil() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        Activity_home.this.finish();
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