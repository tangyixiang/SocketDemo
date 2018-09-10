package bio;

import java.io.*;
import java.net.Socket;

/**
 * 监听socke，发送消息
 */
public class BlockingThread implements Runnable {

    private Socket socket;
    private static BufferedReader receiveReader;  //接收消息
    private static BufferedReader keyboardReader;  //读取键盘输入
    private static BufferedWriter writer;  //写出消息

    public BlockingThread(Socket socket){

        this.socket = socket;
        try {
            receiveReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        //首先启动读的监听线程，再运行写事件
        new Thread(new WriterThread(receiveReader)).start();
        String str = "";
        try {
            while((str = keyboardReader.readLine()) != null){
                //一定不能少了这个"\n"，否则不能发送消息接收不到
                writer.write(str +"\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


/**
 * 监听socket，接收消息
 */
class WriterThread implements Runnable{
    private BufferedReader reader;
    private String threadName;

    public WriterThread(BufferedReader receiveReader){
        this.reader = receiveReader;
        this.threadName = Thread.currentThread().getName();
    }


    @Override
    public void run() {
        String str = "";
        try {
            while ((str = reader.readLine()) != null) {
                str = new String(str.getBytes(), "utf-8");
                System.out.println(threadName +"接收到消息：" + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
