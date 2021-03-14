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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Activity_registration extends AppCompatActivity {

    private EditText userName, userMail, userPassword, userPhone;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    String name, phone, password, mail,uid;
    private static final String TAG = "Activity_registration";
    private static final String KEY_t1 = "trophy1", KEY_t2 = "trophy2",KEY_t3 = "trophy3", KEY_t4 = "trophy4"
            ,KEY_t5 = "trophy5", KEY_t6 = "trophy6",KEY_t7 = "trophy7", KEY_t8 = "trophy8";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setUpUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    String user_email =userMail.getText().toString().trim();
                    String user_password =userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                uid=firebaseAuth.getUid();
                                sendEmailVerification();
                            }
                            else {
                                Toast.makeText(Activity_registration.this, "Registration Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    openMainPage();
                }
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    private void setUpUIViews(){
        userName = (EditText)findViewById(R.id.editTextRegusername);
        userMail = (EditText)findViewById(R.id.editTextRegmail);
        userPassword= (EditText)findViewById(R.id.editTextRegPassword);
        regButton= (Button)findViewById(R.id.buttonReginscription);
        userLogin= (TextView)findViewById(R.id.textViewRegconnexion);
        userPhone = (EditText)findViewById(R.id.editTextPhone);
    }
    private Boolean validate(){
        Boolean result = false;
        name = userName.getText().toString();
        mail = userMail.getText().toString();
        password = userPassword.getText().toString();
        phone = userPhone.getText().toString();
        if((!name.isEmpty()) &&(!mail.isEmpty())&&(!password.isEmpty())&&(!phone.isEmpty())){
            result=true;
        }
        else{
            Toast.makeText(this, "Veuillez tout renseigner",Toast.LENGTH_SHORT);
        }
        return result;
    }
    private void openPage(){
        Intent intent = new Intent(this, Activity_home.class);
        startActivity(intent);
        Activity_registration.this.finish();
    }
    private void openCoPage(){
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        Activity_registration.this.finish();
    }
    private void openMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Activity_registration.this.finish();
    }
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserdata();
                        Toast.makeText(Activity_registration.this, "Successfully register",Toast.LENGTH_SHORT).show();
                        saveNote();
                        firebaseAuth.signOut();
                        openCoPage();
                    }
                    else{
                        Toast.makeText(Activity_registration.this, "Failed registration",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserdata(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(name, mail, phone,0);
        myRef.setValue(userProfile);
    }
    public void saveNote() {
        addNotification();
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_t1,"Un bon départ");
        note.put(KEY_t2,"false");
        note.put(KEY_t3,"false");
        note.put(KEY_t4,"false");
        note.put(KEY_t5,"false");
        note.put(KEY_t6,"false");
        note.put(KEY_t7,"false");
        note.put(KEY_t8,"false");
        db.collection(uid).document("badges").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Activity_registration.this, "Sucess", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Activity_registration.this, "Fail", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    // Creates and displays a notification
    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Activity_registration.this,"My Notification");
        builder.setContentTitle("Weightless");
        builder.setContentText("Trophé débloqué");
        builder.setSmallIcon(R.drawable.logo);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Activity_registration.this);
        managerCompat.notify(1,builder.build());
    }
}