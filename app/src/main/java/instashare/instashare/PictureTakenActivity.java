package instashare.instashare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PictureTakenActivity extends AppCompatActivity {

    String imagepath;
    ImageView iv;
    Button sendimagebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_taken);
        imagepath = getIntent().getStringExtra("myimage");
        iv = findViewById(R.id.myPicture);
        Bitmap bm = BitmapFactory.decodeFile(imagepath);


        iv.setImageBitmap(rotateBitmap(bm));

        sendimagebutton = findViewById(R.id.InstashareButton);
        sendimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public Bitmap rotateBitmap(Bitmap b)
    {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, b.getWidth(), b.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }
}
