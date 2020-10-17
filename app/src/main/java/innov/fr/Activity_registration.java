package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_registration extends AppCompatActivity {

    private EditText userName, userMail, userPassword, userPhone;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private ImageView userProfilePic;
    String name, phone, password, mail;

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
    }
    private void setUpUIViews(){
        userName = (EditText)findViewById(R.id.editTextRegusername);
        userMail = (EditText)findViewById(R.id.editTextRegmail);
        userPassword= (EditText)findViewById(R.id.editTextRegPassword);
        regButton= (Button)findViewById(R.id.buttonReginscription);
        userLogin= (TextView)findViewById(R.id.textViewRegconnexion);
        userPhone = (EditText)findViewById(R.id.editTextPhone);
        userProfilePic = (ImageView) findViewById(R.id.imageViewProfile);
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
        UserProfile userProfile = new UserProfile(name, mail, phone);
        myRef.setValue(userProfile);
    }
}