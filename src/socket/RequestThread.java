package socket;

import java.io.File;
import java.net.Socket;

public class RequestThread implements Runnable {

    private Socket socket;

    public RequestThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        //基本文件路径
        String basicDirectory = "C:\\Users\\36285\\Desktop\\a";
        MyHttpWeb web = new MyHttpWeb(new File(basicDirectory),socket);
        web.start();
    }
}
