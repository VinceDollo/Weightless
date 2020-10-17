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

public class UpdateMail extends AppCompatActivity {

    private Button update;
    private EditText newMail;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        update = (Button)findViewById(R.id.buttoneditmail);
        newMail = (EditText)findViewById(R.id.etnewmail);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = newMail.getText().toString();
                firebaseUser.updatePassword(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UpdateMail.this, "Password changed",Toast.LENGTH_SHORT).show();
                            openActivityProfile();
                        }
                        else{
                            Toast.makeText(UpdateMail.this, "Password changes failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    public void openActivityProfile( ) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        UpdateMail.this.finish();
    }
}