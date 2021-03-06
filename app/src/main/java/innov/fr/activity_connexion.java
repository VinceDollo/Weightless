package innov.fr;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class activity_connexion extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private TextView info;
    private Button login,registration,passwordForgot;
    private int counter = 5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        name = (EditText) findViewById(R.id.identifiant);
        password = (EditText) findViewById(R.id.Password);
        info = (TextView)findViewById(R.id.info);
        registration = (Button)findViewById(R.id.buttonreg);
        passwordForgot = (Button)findViewById(R.id.forgotPassword) ;
        login = (Button)findViewById(R.id.btnconnexion);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            openHome();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(),password.getText().toString());
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReg();
            }
        });
        passwordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPF();
            }
        });
    }

    private void validate(String username, String password){

        progressDialog.setMessage("Essai de connexion");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailVerification();
                }
                else{
                    Toast.makeText(activity_connexion.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    info.setText("Number of attemps remaining = "+counter);
                    progressDialog.dismiss();
                }
                if(counter==0){
                    login.setEnabled(false);
                }
            }
        });
    }
    private void openReg(){
        Intent intent = new Intent(this, Activity_registration.class);
        startActivity(intent);
        activity_connexion.this.finish();
    }
    private void openHome(){
        Intent intent = new Intent(this, Activity_home.class);
        startActivity(intent);
        activity_connexion.this.finish();
    }
    private void openPF(){
        Intent intent = new Intent(this, PasswordActivity.class);
        startActivity(intent);
        activity_connexion.this.finish();
    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        if (emailflag){
            openHome();
        }
        else{
            Toast.makeText(this, "verify your email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}



