package httpserver;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.util.Date;

/**
 * 具体处理线程
 * @author sky
 *
 */
public class IOHandlerThread implements Runnable {

    private File webappDirectory = new File("C:\\Users\\36285\\Desktop\\a");
    private SocketChannel channel;
    private String requestData;

    public IOHandlerThread(SocketChannel channel){
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            buffer.flip();
            byte[] data = buffer.array();
            requestData = new String(data, "utf-8").trim();

            if(requestData != null && !requestData.equals("")){
                responseInfo(requestData);
            }
            //System.out.println("数据写出完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	try {
				channel.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


    }


    private void responseInfo(String data) throws IOException {
        String[] msgArray = data.split("\r\n");
        String[] methodAndUri = msgArray[0].split("\\s");

        // 如果是GET请求
        if (methodAndUri[0].equals("GET")) {
            // 处理最简单的请求
            String fileName = methodAndUri[1];
            if (fileName.endsWith("/")) {
                fileName = fileName + "index.html";
            }

            /*if(fileName.equals("/favicon.ico")){
                //throw new NoSuchFileException("该文件不存在");
            }*/
            // 获取请求资源的路径
            String filePath = webappDirectory + fileName;
            File requestIndexFile = new File(filePath);
            String version = null;
            if (methodAndUri.length > 2) {
                version = methodAndUri[2];
            }

            if (requestIndexFile.canRead()) {
                byte[] filedata = Files.readAllBytes(requestIndexFile.toPath());
                String contentType = URLConnection.getFileNameMap().getContentTypeFor(requestIndexFile.getName());
                int contentLength = filedata.length;

                String responseCode = "";
                String serverName = "Simple HttpWebServer";
                if (version.startsWith("HTTP/")) {
                    responseCode = "HTTP/1.0 200 OK";
                }
                StringBuilder msg = new StringBuilder();
                msg.append(responseCode + "\r\n");
                msg.append("Date:" + new Date() + "\r\n");
                msg.append("Server:" + serverName + "\r\n");
                msg.append("Content-length:" + contentLength + "\r\n");
                msg.append("Content-type:" + contentType + "\r\n\r\n");

                ByteBuffer b = ByteBuffer.wrap(msg.toString().getBytes());
                ByteBuffer b2 = ByteBuffer.wrap(filedata);

                channel.write(b);
                int offset = 0;
                int r = 0;
                while (offset < b2.limit()) {
                    r = channel.write(b2);
                    offset += r;
                }

            }
        }
    }

}
