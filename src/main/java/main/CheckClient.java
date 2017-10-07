package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class CheckClient {

    static InetAddress inetAddress;
    static String ip = "127.0.0.1";
    static int port = 10523;
    static Socket socket;

    public static void main(String[] args) throws IOException {

        InetSocketAddress inetAddress = new InetSocketAddress(ip, port);
        SocketChannel channel = SocketChannel.open();
        channel.connect(inetAddress);
        channel.configureBlocking(false);

        Selector selector = Selector.open();
        if (channel.isConnected()){
            System.out.println("We connected!");
            System.out.println(channel.getLocalAddress());
        }


//        inetAddress = InetAddress.getByName(ip);
//        socket = new Socket(inetAddress, port);
//        if (socket.isConnected()){
//            System.out.println("Connect");
//        }

    }
}
