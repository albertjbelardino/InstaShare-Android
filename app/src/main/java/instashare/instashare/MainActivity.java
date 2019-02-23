package instashare.instashare;

import android.app.Activity;
import android.graphics.Canvas;
import android.hardware.camera2.CameraAccessException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            SurfaceView sv = (SurfaceView)findViewById(R.id.backgroundCamera);
            sv.setVisibility(SurfaceView.VISIBLE);
            sv.draw(new Canvas());
            CameraHandler ch = new CameraHandler(sv, (Activity)this);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
