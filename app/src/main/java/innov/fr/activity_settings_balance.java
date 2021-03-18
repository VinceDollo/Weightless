package innov.fr;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;

public class activity_settings_balance extends AppCompatActivity {

    private Button button;
    private Button btnSend;
    private EditText nomWifi;
    private EditText mdpWifi;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_balance);

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

        nomWifi = findViewById(R.id.textwifi);
        mdpWifi = findViewById(R.id.textWifi);

        btnSend = findViewById(R.id.btnwifi);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        String ip = Shared.getIpAddress(getApplicationContext());
                        //System.out.println("My ip addresss is : " + ip);
                        String[] temp = ip.split("\\.");
                        String target = temp[0]+"."+temp[1]+"."+temp[2]+".1";
                        //System.out.println(target);
                        JSONObject object = new JSONObject();
                        try {
                            object.put("ssid", nomWifi.getText().toString());
                            object.put("passWiFi", mdpWifi.getText().toString());
                            object.put("nameAp", "ere");
                            object.put("passAp", "rr");

                            System.out.println(object);

                            ByteBuffer buffer = ByteBuffer.allocate(4096);
                            buffer.clear();
                            Charset charset = Charset.forName("UTF-8");
                            SocketChannel client = SocketChannel.open();
                            client.connect(new InetSocketAddress(target, 52000));
                            client.write(charset.encode(String.valueOf(object)));

                            while(true)
                                if(client.read(buffer)>0){
                                    buffer.flip();
                                    String res = new String(buffer.array());
                                    //System.out.println(res);
                                    buffer.clear();
                                    break;
                                }
                            client.close();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (UnknownHostException e){
                            System.out.println("Target is unrecheable");
                            e.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }



    public void openActivityWeight() {
        Intent intent = new Intent(this, activity_weight.class);
        startActivity(intent);
        activity_settings_balance.this.finish();
    }

    public void openActivityResults() {
        Intent intent = new Intent(this, activity_results.class);
        startActivity(intent);
        activity_settings_balance.this.finish();
    }

    public void openActivitySettings() {
        Intent intent = new Intent(this, activity_settings.class);
        startActivity(intent);
        activity_settings_balance.this.finish();
    }

    public void openActivityConnexion() {
        Intent intent = new Intent(this, activity_connexion.class);
        startActivity(intent);
        activity_settings_balance.this.finish();
    }

    public void openActivityProfil() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        activity_settings_balance.this.finish();
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

