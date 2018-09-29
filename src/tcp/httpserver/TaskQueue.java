package httpserver;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建单例模式的queue
 */
public class TaskQueue {

    private static final LinkedBlockingQueue<SocketChannel> taskQueue = new LinkedBlockingQueue<SocketChannel>();

    private static final LinkedList<SocketChannel> list = new LinkedList<SocketChannel>();

    private TaskQueue(){}

    public static LinkedBlockingQueue<SocketChannel> getInstance(){
        return taskQueue;
    }

}
