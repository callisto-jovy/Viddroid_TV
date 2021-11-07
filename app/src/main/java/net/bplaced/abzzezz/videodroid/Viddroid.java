package net.bplaced.abzzezz.videodroid;

import android.app.Activity;
import android.content.Context;
import net.bplaced.abzzezz.videodroid.util.themoviedb.WatchableList;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class Viddroid {

    public static final Viddroid INSTANCE = new Viddroid();

    private WatchableList watchableList;

    public Viddroid() {
    }

    public void setup(final Activity activity) {
        setupHandlers(activity.getApplicationContext());
        registerShutdownHook();
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert sc != null;
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);


    }

    private void setupHandlers(final Context context) {
        this.watchableList = new WatchableList(context);
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            getWatchableList().saveFavorites();
        }));
    }


    public WatchableList getWatchableList() {
        return watchableList;
    }
}
