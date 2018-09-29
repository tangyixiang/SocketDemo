package httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 监听客户端的连接
 */
public class AcceptorServer implements Runnable {

    private volatile LinkedBlockingQueue<SocketChannel> taskQueue = TaskQueue.getInstance();

    private Selector selector;

    public AcceptorServer(int port){
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                selector.select();

                Set<SelectionKey> keySet = selector.selectedKeys();

                Iterator<SelectionKey> it = keySet.iterator();
                while (it.hasNext()){
                    SelectionKey key = it.next();
                    it.remove();
                    if(key.isValid()){
                        if(key.isAcceptable()){
                            ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                            SocketChannel client = (SocketChannel)ssc.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            //taskQueue.put(client);
                            //client.register(selector, SelectionKey.OP_READ);
                        }
                        if(key.isReadable()) {
                        	System.out.println("key进来了");
                        	//将key的所有注册事件取消
                        	key.interestOps(0);
                        	SocketChannel sc = (SocketChannel)key.channel();
                        	synchronized (taskQueue) {
                            	taskQueue.add(sc);
							}
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
