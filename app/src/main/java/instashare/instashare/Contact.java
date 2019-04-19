package instashare.instashare;

import android.graphics.Bitmap;

public class Contact {

    Bitmap image;
    String name;
    String number;
    String id;

    public Contact(Bitmap image, String name, String number, String id)
    {
        this.image = image;
        this.name = name;
        this.number = number;
        this.id = id;
    }

}
