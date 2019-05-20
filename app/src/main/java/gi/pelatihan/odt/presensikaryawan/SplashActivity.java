package gi.pelatihan.odt.presensikaryawan;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private String tag = this.getClass().getSimpleName();
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        actSkip();
    }

    private void actSkip(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(activity, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
