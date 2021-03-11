package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    private EditText username,userphone;
    private Button edit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    int cpoubelle=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

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
             String name = username.getText().toString();
             String phone = userphone.getText().toString();

             UserProfile userProfile = new UserProfile(name,firebaseUser.getEmail(),phone,cpoubelle);
             databaseReference.setValue(userProfile);
             openProfileActivity();
         }
     });
}
    public void openProfileActivity() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        UpdateProfile.this.finish();
    }
}