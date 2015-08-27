package tn.amineghabi.acquawatchface;

import android.app.Activity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Amin Ghabi on 25/08/15.
 */
public class WatchfaceActivity extends Activity {

    @Bind(R.id.water_view)
    AcquaView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.updateTime();
                    }
                });
            }
        }, 0, 200);
    }
}
