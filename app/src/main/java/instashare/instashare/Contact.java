package instashare.instashare;

import android.graphics.Bitmap;

public class Contact {

    Bitmap image;
    String name;
    String number;

    public Contact(Bitmap image, String name, String number)
    {
        this.image = image;
        this.name = name;
        this.number = number;
    }

}
