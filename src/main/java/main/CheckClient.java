package main;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CheckClient {

    static InetAddress inetAddress;
    static String ip = "127.0.0.1";
    static int port = 10523;
    static Socket socket;

    public static void main(String[] args) throws IOException, InterruptedException {

        InetSocketAddress inetAddress = new InetSocketAddress(ip, port);
        SocketChannel channel = SocketChannel.open(inetAddress);
        //channel.connect(inetAddress);
        //channel.configureBlocking(false);

        while (channel.isOpen()){
            System.out.println("We connected to server: " + channel.getLocalAddress());
            String name = "That is Client!";
            byte[] bytes = new String(name).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            channel.write(buffer);

            byte[] bytes2 = buffer.array();
            String cs = new String(bytes2);
            System.out.println(cs);

            buffer.clear();
            Thread.sleep(2000);

            System.out.println("Сейчас что то примем");
            channel.read(buffer);
            byte[] bytes1 = buffer.array();
            String fromServ = new String(bytes1);
            System.out.println(fromServ);

            channel.close();
        }

        //channel.close();
        System.out.println("Connect is closed!");
    }
}


