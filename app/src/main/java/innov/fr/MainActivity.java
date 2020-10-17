
package innov.fr;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private int SLEEP_TIMER=3;
    private ProgressBar pb;
    int counter =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
        progressBar();
    }
    private class LogoLauncher extends Thread{
        public void run(){
            try{
                sleep(1000*SLEEP_TIMER);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this, activity_connexion.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
    private void progressBar(){
        pb = (ProgressBar) findViewById(R.id.progressBar);

        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
                pb.setProgress(counter);
                if(counter == 100){
                    t.cancel();
                }
            }
        };
        t.schedule(tt,0,10*SLEEP_TIMER);
    }
}



