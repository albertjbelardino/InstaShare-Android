package instashare.instashare;

import android.app.Activity;
import android.graphics.Canvas;
import android.hardware.camera2.CameraAccessException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button takepicbutton;
    SurfaceView sv;
    CameraHandler ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpPictureTaking();

    }

    public void setUpPictureTaking()
    {
        takepicbutton = findViewById(R.id.pictureButton);
        sv = (SurfaceView)findViewById(R.id.backgroundCamera);

        try {

            sv.setVisibility(SurfaceView.VISIBLE);
            sv.draw(new Canvas());
            ch = new CameraHandler(sv, (Activity)this);
            takepicbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ch.takePictureNow();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        ch.endCapture();
        super.onDestroy();
    }
}
