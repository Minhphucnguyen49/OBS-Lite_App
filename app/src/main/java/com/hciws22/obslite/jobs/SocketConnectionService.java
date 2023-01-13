package com.hciws22.obslite.jobs;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketConnectionService {



    public static boolean hasInternet = false;



    public static boolean isConnected(){

        checkNetworkState();

        return hasInternet;
    }



    public static void checkNetworkState(){

        Thread t1 = new Thread(() -> {
            try (Socket sock = new Socket()){

                int timeoutMs = 1500;
                SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

                sock.connect(sockaddr, timeoutMs);
                Log.d("Socket: Local Port", String.valueOf(sock.getLocalPort()));
                hasInternet = true;

            } catch (IOException e) {
                Log.d("Socket: Local Port", "No port");
                hasInternet = false;
            }
        });

        t1.start();

        while (t1.isAlive());


    }
}
