package innov.fr;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.graph.Graph;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;

public class activity_results extends AppCompatActivity {
    private Button button;
    private FirebaseAuth firebaseAuth;
    private TextView tvres1, tvres2,tvres3, tvres4,tvres5, tvresMoy,tvresnbm;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "activity_results";
    private static final String KEY_DAY = "day", KEY_MONTH = "month", KEY_POIDS="poids", KEY_YEAR="year";
    private FirebaseDatabase firebaseDatabase;
    private String username = "Undefined",uid="Undefined";
    private String numberPoubelle = "Undefined";
    private int compteur =0;
    private int sommePoids=0;
    private int nbMesures=0;
    private int value0=0,value1=0,value2=0,value3=0,value4=0;
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results); button = findViewById(R.id.btn_weight);
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
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Id de mesure");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Poids( en kg)");
        uid=firebaseAuth.getUid();
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

                            tvres1.setText("Poubelle 1 : Date = "+day+" "+month+" "+year+", poids = "+poids+"kg \n Poubelle ID : "+"poubellen"+String.valueOf(compteur));
                            sommePoids+=Integer.parseInt(poids);
                            value0=Integer.parseInt(poids);
                            nbMesures++;
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

                            tvres2.setText("Poubelle 2: Date = "+day+" "+month+" "+year+", poids = "+poids+"kg \n Poubelle ID : "+"poubellen"+String.valueOf(compteur-1));
                            sommePoids+=Integer.parseInt(poids);
                            value1=Integer.parseInt(poids);
                            nbMesures++;
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

                            tvres3.setText("Poubelle 3: Date = "+day+" "+month+" "+year+", poids = "+poids+"kg \n Poubelle ID : "+"poubellen"+String.valueOf(compteur-2));
                            sommePoids+=Integer.parseInt(poids);
                            value2=Integer.parseInt(poids);
                            nbMesures++;
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

                            tvres4.setText("Poubelle 4: Date = "+day+" "+month+" "+year+", poids = "+poids+"kg \n Poubelle ID : "+"poubellen"+String.valueOf(compteur-3));
                            sommePoids+=Integer.parseInt(poids);
                            value3=Integer.parseInt(poids);
                            nbMesures++;
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

                            tvres5.setText("Poubelle 5: Date = "+day+" "+month+" "+year+", poids = "+poids+"kg \n Poubelle ID : "+"poubellen"+String.valueOf(compteur-4));
                            sommePoids+=Integer.parseInt(poids);
                            value4=Integer.parseInt(poids);
                            nbMesures++;
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

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, value0),
                new DataPoint(1, value1),
                new DataPoint(2, value2),
                new DataPoint(3, value3),
                new DataPoint(4, value4)
        });
        series.setBackgroundColor(Color.GREEN);
        graph.addSeries(series);
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



