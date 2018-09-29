package httpserver;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * NIO线程池处理IO读写
 */
public class NioExecutor implements Runnable {

	private ExecutorService pool = Executors.newFixedThreadPool(50);
	private LinkedBlockingQueue<SocketChannel> taskQueue = TaskQueue.getInstance();
	// 处理失败了再添加到队列中

	@Override
	public void run() {
		while (true) {
			if (taskQueue.size() > 0) {
				try {
					ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) pool;
					SocketChannel channel = (SocketChannel) taskQueue.take();
					pool.execute(new IOHandlerThread(channel));
					System.out.println("活动线程数量" + threadPoolExecutor.getActiveCount() + "-------->阻塞数量"
							+ threadPoolExecutor.getQueue().size());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
