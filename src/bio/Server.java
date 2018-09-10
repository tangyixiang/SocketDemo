package bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args){
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(8080));
            while (true){
                Socket socket = server.accept();
                System.out.println("服务器启动");
                Thread t = new Thread(new BlockingThread(socket), "服务器");
                t.start();

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
