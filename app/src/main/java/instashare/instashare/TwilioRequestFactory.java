package instashare.instashare;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TwilioRequestFactory {

    public static Call post(String url, String toPhoneNumber,
              String messageText, OkHttpClient mClient, List<String> urls, Callback callback) throws IOException {

        String imageUrls = urls.get(0);
        for(int i = 1; i < urls.size(); i++) {
            imageUrls += "TWILIO_URL" + urls.get(i);
        }

        RequestBody formBody = new FormBody.Builder()
                .add("To", toPhoneNumber)
                .add("Body", messageText)
                .add("imageUrls", imageUrls)
                .build();


        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call response = mClient.newCall(request);
        response.enqueue(callback);
        return response;
    }
}
