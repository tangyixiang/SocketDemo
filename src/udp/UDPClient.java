package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 使用UDP协议进行通讯 client端
 * receive方法是阻塞的，没有接收到数据会一直阻塞
 */
public class UDPClient {

    public static void main(String[] args) {

        try {
            DatagramSocket client = new DatagramSocket(); //不绑定端口，由系统自动分配一个随机端口
            client.connect(new InetSocketAddress("127.0.0.1", 8080));

            String message = "i am client";
            DatagramPacket sendMessage = new DatagramPacket(message.getBytes(), message.getBytes().length);

            //DatagramPacket sendMessage = new DatagramPacket(message.getBytes(), message.getBytes().length, new Ineta, )

            //DatagramPacket sendMessage = new DatagramPacket(message.getBytes(),
            //       message.getBytes().length, new InetSocketAddress("127.0.0.1", 8080));
            client.send(sendMessage);
            DatagramPacket receive = new DatagramPacket(new byte[1024], 1024);
            client.receive(receive);
            String backMessge = new String(receive.getData(), "utf-8");
            System.out.println(backMessge);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
