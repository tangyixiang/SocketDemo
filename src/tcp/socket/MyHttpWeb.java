package tcp.socket;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;

/**
 * 利用ServerSocket实现一个简单的HTTP服务器
 */
public class MyHttpWeb {


    private Socket connection;
    private File rootDirectory;
    private int status; //返回请求的状态
    private static final String INDEX_FILE = "index.html";
    private Writer out;
    private InputStream in;
    private OutputStream returnFileStream;


    public MyHttpWeb(File rootDirectory,Socket connection){
        if(rootDirectory.isFile()){
            throw new IllegalArgumentException("rootDirectory must be a Directory,not a file");
        }
        this.rootDirectory = rootDirectory;
        this.connection = connection;
    }

    public void start(){
        try {

            //返回信息和返回文件类型的流，返回文件和图片类型的流需要使用底层的流，不用包装

            //用缓冲区将字节流进行包裹
            returnFileStream = new BufferedOutputStream(connection.getOutputStream());
            //将字节流转为字符流
            out = new OutputStreamWriter(returnFileStream, "utf-8");

            //获取输入流
            in = new BufferedInputStream(connection.getInputStream());

            StringBuilder requestMessage = new StringBuilder();
            int c ;
            while ((c = in.read()) != -1){
                if( c == '\r' || c == '\n' )
                    break;
                requestMessage.append((char) c);
            }

            //获取request中的信息
            String get = requestMessage.toString();
            //匹配所有的空格
            String[] token = get.split("\\s");

            String version = "";

            //如果是GET请求
            if(token[0].equals("GET")){
                //处理最简单的请求
                String fileName = token[1];
                if(fileName.endsWith("/")){
//                    fileName = fileName + INDEX_FILE;
                    fileName = fileName + "background.jpg";
                }


                //获取请求资源的路径
                String filePath = rootDirectory + token[1];
                File requestDirectory = new File(filePath);
                if (!requestDirectory.exists()){
                    throw new NoSuchFieldException("找不到资源路径"+filePath);
                }

                //File requestIndexFile = new File(rootDirectory, token[1]+INDEX_FILE);
                File requestIndexFile = new File(rootDirectory, token[1]+"background.jpg");

                if (!requestIndexFile.exists()){
                    throw new NoSuchFieldException("找不到资源路径"+requestIndexFile.getPath());
                }


                if(token.length >2){
                    version = token[2];
                }

                if(requestIndexFile.canRead()){
                    byte[] data = Files.readAllBytes(requestIndexFile.toPath());
                    String contentType = URLConnection.getFileNameMap().getContentTypeFor(requestIndexFile.getName());
                    int contentLength = data.length;
                    status = 200;
                    String responseCode = "";
                    String serverName = "Simple HttpWebServer";
                    if(version.startsWith("HTTP/")){
                        responseCode = "HTTP/1.0 "+status+" OK";
                    }
                    //返回响应头 数据长度，数据类型，响应状态，日期 服务器名字
                    responseHeader(responseCode,serverName,contentType,contentLength);
                    //返回响应数据
                    returnFileStream.write(data);
                    returnFileStream.flush();
                }else{

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            handleException("NoSuchFile");
            e.printStackTrace();
        }finally {
            try {
                in.close();
                returnFileStream.close();
                out.close();
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 响应消息头
     * @param status 状态码
     * @param serverName 服务器名
     * @param contentType 返回内容类型
     * @param contentLength 返回数据长度
     */
    private void responseHeader(String status, String serverName, String contentType, int contentLength) {
        if(out != null){
            try {
                out.write(status + "\r\n");
                Date now = new Date();
                out.write("Date:"+now+"\r\n");
                out.write("Server:"+serverName+"\r\n");
                out.write("Content-length"+contentLength+"\r\n");
                out.write("Content-type"+contentType+"\r\n\r\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    private void handleException(String exceptionType){



    }


}
