package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {
    private Button update;
    private EditText newPassword;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        update = (Button)findViewById(R.id.buttoneditpassword);
        newPassword = (EditText)findViewById(R.id.etnewpassword);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = newPassword.getText().toString();
                firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UpdatePassword.this, "Password changed",Toast.LENGTH_SHORT).show();
                            openActivityProfile();
                        }
                        else{
                            Toast.makeText(UpdatePassword.this, "Password changes failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    public void openActivityProfile( ) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        UpdatePassword.this.finish();
    }
}