package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ContactWeightless extends AppCompatActivity {
    private Button buttonSend;
    private EditText textMessage;
    private String message= "Undefined";
    private static final String TAG = "ContactWeightless";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_weightless);


        buttonSend = (Button) findViewById(R.id.buttonsendcontact);
        textMessage = (EditText) findViewById(R.id.etavisW);

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String message = textMessage.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "weightless.contact@gmail.com"});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, "contact Weightless");
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Envoyer l'avis :"));
            }
        });
    }
    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        ContactWeightless.this.finish();
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
    private void openAcceuil(){
        Intent intent = new Intent(this, Activity_home.class);
        startActivity(intent);
        ContactWeightless.this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutMenu:{
                Logout();
            }
            case R.id.acceuilMenu:{
                openAcceuil();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}