package tcp.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8080);
            while (true){
                Socket connection = server.accept();
                Thread t = new Thread(new RequestThread(connection));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
