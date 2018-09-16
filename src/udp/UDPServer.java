package udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * 使用UDP协议进行通讯，Server端
 * server端需要绑定端口，监听入站的数据，
 * 绑定端口和地址可以在new时创建，也可以使用bind方法绑定
 */
public class UDPServer {

    public static void main(String[] args){
        try {
            //绑定一个端口监听
            DatagramSocket server = new DatagramSocket(8080);
            //server.bind(new InetSocketAddress("127.0.0.1", 8080));
            while(true){

                DatagramPacket receive = new DatagramPacket(new byte[1024], 1024);

                //receive是阻塞的
                server.receive(receive);
                //获取接收的数据的发送地址和端口，可以对它进行消息回复
                InetAddress requestAddress = receive.getAddress();
                int requestPort = receive.getPort();
                byte[] data = receive.getData();
                System.out.println("接受到的数据是:"+new String(data, "utf-8"));

                String backMessage = "server is ready";
                DatagramPacket backMessagePacket = new DatagramPacket(backMessage.getBytes() , backMessage.getBytes().length, requestAddress, requestPort);
                server.send(backMessagePacket);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
