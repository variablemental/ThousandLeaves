package com.example.coder_z.thousandleaves;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by coder-z on 17-4-17.
 */

public class BotanyFetcher {
    public static String TAG="BotanyFetcher";

    byte[] getUrlByte(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = connection.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "given wrong response code");
                    return null;
                }
                int byteRead = 0;
                byte[] buffer = new byte[1024];
                while ((byteRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, byteRead);
                }
                out.close();
                return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    public String getUrl(String path) throws IOException {
        return new String(getUrlByte(path));
    }
}
