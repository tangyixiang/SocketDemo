package tcp.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {


    public static void main(String[] args){
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(8080));
            Thread t = new Thread(new BlockingThread(socket),"client");
            t.start();
            System.out.println("客户端已经启动");
            t.join();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
