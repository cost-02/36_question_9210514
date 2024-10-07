package com.example;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;

public class SSLClientTest {
    public static void main(String[] args) {
        try {
            // Percorso al file keystore (assicurati di usare il percorso corretto)
            String keystorePath = "path_to_keystore/cacerts";
            char[] keystorePassword = "changeit".toCharArray(); // Password del keystore

            // Carica il keystore
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream keystoreStream = new FileInputStream(keystorePath)) {
                keystore.load(keystoreStream, keystorePassword);
            }

            // Inizializza TrustManagerFactory con il keystore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keystore);

            // Inizializza SSLContext
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            // Crea la connessione HTTPS
            URL url = new URL("https://yourserver.com"); // URL del server
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(context.getSocketFactory());

            // Stampa la risposta
            InputStream responseStream = connection.getInputStream();
            int data = responseStream.read();
            while (data != -1) {
                System.out.print((char) data);
                data = responseStream.read();
            }
            responseStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
