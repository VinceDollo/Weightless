package innov.fr;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_results extends AppCompatActivity {
    private Button button;
    private FirebaseAuth firebaseAuth;
    private TextView tvres1, tvres2,tvres3, tvres4,tvres5, tvresMoy,tvresnbm;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "activity_results";
    private static final String KEY_DAY = "day", KEY_MONTH = "month", KEY_POIDS="poids", KEY_YEAR="year",KEY_t3="trophy3";
    private FirebaseDatabase firebaseDatabase;
    private String username = "Undefined",uid="Undefined";
    private String numberPoubelle = "Undefined";
    private int compteur =0;
    private int sommePoids=0;
    private int nbMesures=0;
    private int value0=0,value1=0,value2=0,value3=0,value4=0;
    private String sdate0="...",sdate1="...",sdate2="...",sdate3="...",sdate4="...";
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_results);
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

        button = findViewById(R.id.buttonresultsGO);
        button = findViewById(R.id.buttongraph);


        tvres1 = findViewById(R.id.tvresultsPB1);
        tvres2 = findViewById(R.id.tvresultsPB2);
        tvres3 = findViewById(R.id.tvresultsPB3);
        tvres4 = findViewById(R.id.tvresultsPB4);
        tvres5 = findViewById(R.id.tvresultsPB5);
        tvresMoy = findViewById(R.id.tvresultsPBMoy);
        tvresnbm = findViewById(R.id.tvresultsPBNbMes);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                username =userProfile.getUserName();
                compteur = userProfile.getCompteurPoubelle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_results.this, error.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
        graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Evolution de mes dernières pesées");
        graph.setTitleColor(Color.GREEN);
        graph.getGridLabelRenderer().setHorizontalAxisTitleTextSize(30);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(30);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("\nDate mesure (dd-MM)");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Poids(en kg)");
        graph.getGridLabelRenderer().setTextSize(20);


        uid=firebaseAuth.getUid();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    public void openActivityWeight() {
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
        activity_results.this.finish();
    }
    public void openActivityResults() {
        finish();
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
    }
    public void openActivitySettings() {
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
        activity_results.this.finish();
    }
    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        activity_results.this.finish();
    }
    public void openActivityProfil() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        activity_results.this.finish();
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
    public void loadNote(View v){
        saveNote();
        graph.removeAllSeries();
        db.collection(uid).document("poubellen"+String.valueOf(compteur)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String day = documentSnapshot.getString(KEY_DAY);
                            String month = documentSnapshot.getString(KEY_MONTH);
                            String year = documentSnapshot.getString(KEY_YEAR);
                            String poids = documentSnapshot.getString(KEY_POIDS);
                            tvres1.setText("Poubelle 1 : Date = "+day+" "+month+" "+year+", poids = "+poids+"kg"
                            );
                            if (Integer.parseInt(poids)!=value0){
                                nbMesures++;
                                value0=Integer.parseInt(poids);
                                sommePoids+=Integer.parseInt(poids);
                                sdate0=day+" "+month;
                            }
                        }
                        else{
                            Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

        db.collection(uid).document("poubellen"+String.valueOf(compteur-1)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String day = documentSnapshot.getString(KEY_DAY);
                            String month = documentSnapshot.getString(KEY_MONTH);
                            String year = documentSnapshot.getString(KEY_YEAR);
                            String poids = documentSnapshot.getString(KEY_POIDS);

                            tvres2.setText("Poubelle 2: Date = "+day+" "+month+" "+year+", poids = "+poids+"kg");
                            if (Integer.parseInt(poids)!=value1){
                                nbMesures++;
                                value1=Integer.parseInt(poids);
                                sommePoids+=Integer.parseInt(poids);
                                sdate1=day+" "+month;
                            }
                        }
                        else{
                            Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
        db.collection(uid).document("poubellen"+String.valueOf(compteur-2)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String day = documentSnapshot.getString(KEY_DAY);
                            String month = documentSnapshot.getString(KEY_MONTH);
                            String year = documentSnapshot.getString(KEY_YEAR);
                            String poids = documentSnapshot.getString(KEY_POIDS);

                            tvres3.setText("Poubelle 3: Date = "+day+" "+month+" "+year+", poids = "+poids+"kg");
                            if (Integer.parseInt(poids)!=value2){
                                nbMesures++;
                                value2=Integer.parseInt(poids);
                                sommePoids+=Integer.parseInt(poids);
                                sdate2=day+" "+month;
                            }
                        }
                        else{
                            Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
        db.collection(uid).document("poubellen"+String.valueOf(compteur-3)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String day = documentSnapshot.getString(KEY_DAY);
                            String month = documentSnapshot.getString(KEY_MONTH);
                            String year = documentSnapshot.getString(KEY_YEAR);
                            String poids = documentSnapshot.getString(KEY_POIDS);

                            tvres4.setText("Poubelle 4: Date = "+day+" "+month+" "+year+", poids = "+poids+"kg");
                            if (Integer.parseInt(poids)!=value3){
                                nbMesures++;
                                value3=Integer.parseInt(poids);
                                sommePoids+=Integer.parseInt(poids);
                                sdate3=day+" "+month;
                            }
                        }
                        else{
                            Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
        db.collection(uid).document("poubellen"+String.valueOf(compteur-4)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String day = documentSnapshot.getString(KEY_DAY);
                            String month = documentSnapshot.getString(KEY_MONTH);
                            String year = documentSnapshot.getString(KEY_YEAR);
                            String poids = documentSnapshot.getString(KEY_POIDS);

                            tvres5.setText("Poubelle 5: Date = "+day+" "+month+" "+year+", poids = "+poids+"kg");
                            if (Integer.parseInt(poids)!=value4){
                                nbMesures++;
                                value4=Integer.parseInt(poids);
                                sommePoids+=Integer.parseInt(poids);
                                sdate4=day+" "+month;
                            }
                        }
                        else{
                            Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
        if(nbMesures>0){
            tvresMoy.setText("Moyenne = "+sommePoids/nbMesures);
        }
        tvresnbm.setText("Nombre de mesures  = "+nbMesures);


        DataPoint mes0 =  new DataPoint(4, value0);
        LineGraphSeries<DataPoint> series;
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        if (nbMesures==5){
            series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(0, value4),new DataPoint(1, value3),new DataPoint(2, value2),new DataPoint(3, value1), mes0
            });
            staticLabelsFormatter.setHorizontalLabels(new String[]{sdate0,sdate1,sdate2,sdate3,sdate4});
        }
        else if (nbMesures==4){
            series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(1, value3),new DataPoint(2, value2),new DataPoint(3, value1), mes0
            });
            staticLabelsFormatter.setHorizontalLabels(new String[]{sdate0,sdate1,sdate2,sdate3});
        }
        else if (nbMesures==3){
            series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(2, value2),new DataPoint(3, value1), mes0
            });
            staticLabelsFormatter.setHorizontalLabels(new String[]{sdate0,sdate1,sdate2});
        }
        else if (nbMesures==2){
            series = new LineGraphSeries<>(new DataPoint[] {
                    new DataPoint(3, value1), mes0
            });
            staticLabelsFormatter.setHorizontalLabels(new String[]{sdate0,sdate1});
        }
        else{
            series = new LineGraphSeries<>(new DataPoint[] {
                    mes0
            });
            staticLabelsFormatter.setHorizontalLabels(new String[]{sdate0,"..."});
        }
        series.setBackgroundColor(Color.GREEN);
        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }
    public void saveNote() {
        addNotification();
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_t3,"Analyste");

        db.collection(uid).document("badges").update(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(activity_results.this, "Sucess", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_results.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
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
    // Creates and displays a notification
    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity_results.this,"My Notification");
        builder.setContentTitle("Weightless");
        builder.setContentText("Trophé débloqué");
        builder.setSmallIcon(R.drawable.logo);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(activity_results.this);
        managerCompat.notify(1,builder.build());
    }
}



