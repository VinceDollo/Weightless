package innov.fr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Activity_home extends AppCompatActivity {

    private Button button, btnGo;
    private FirebaseAuth firebaseAuth;
    private String resultat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth=FirebaseAuth.getInstance();

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

        btnGo = findViewById(R.id.btn_Go1);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityWeight();
            }
        });
    }

    private void getWeightFormEsp()  {
        new Thread(){
            public void run(){
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer buffer = ByteBuffer.allocate(2045);
                int port = 51000;
                String[] temp = Shared.getIpAddress(getApplicationContext()).split("\\.");
                String target = temp[0]+"."+temp[1]+"."+temp[2]+"."+"253";
                try{
                    SocketChannel client = SocketChannel.open();
                    client.connect(new InetSocketAddress(target, 52000));
                    client.write(charset.encode("get"));
                    while(true)
                        if(client.read(buffer)>0){
                            buffer.flip();
                            String res = new String(buffer.array());
                            final double weight = Double.valueOf(res);
                            //System.out.println(weight);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    resultat = String.valueOf(weight);
                                }
                            });
                            buffer.clear();
                            break;
                        }
                    client.close();
                }catch (UnknownHostException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }


        }.start();


    }
    public void openActivityWeight() {
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
        Activity_home.this.finish();
    }
    public void openActivityResults() {
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
        Activity_home.this.finish();
    }
    public void openActivitySettings() {
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
        Activity_home.this.finish();
    }
    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        Activity_home.this.finish();
    }
    public void openActivityProfil() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        Activity_home.this.finish();
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