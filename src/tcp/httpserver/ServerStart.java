package httpserver;

/**
 * HTTP服务启动入口类
 * @author sky
 *
 */
public class ServerStart {

    public static void main(String[] args){
        AcceptorServer acceptorServer = new AcceptorServer(8082);
        NioExecutor nio = new NioExecutor();
        new Thread(acceptorServer).start();
        new Thread(nio).start();
    }
}
