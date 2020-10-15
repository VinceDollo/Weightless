package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_registration extends AppCompatActivity {

    private EditText userName, userMail, userPassword;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;

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
                                Toast.makeText(Activity_registration.this, "Registration Successful",Toast.LENGTH_SHORT).show();
                                openPage();
                            }
                            else {
                                Toast.makeText(Activity_registration.this, "Registration Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //openPage();
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
    }
    private Boolean validate(){
        Boolean result = false;
        String name = userName.getText().toString();
        String mail = userMail.getText().toString();
        String password = userPassword.getText().toString();
        if((!name.isEmpty()) &&(!mail.isEmpty())&&(!password.isEmpty())){
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
    }
    private void openMainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}