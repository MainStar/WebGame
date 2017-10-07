package main;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Server2 extends Thread {

    private String ip = "127.0.0.1";
    private int port = 10523;
    private ServerSocket ss;
    private Socket socket;
    private boolean checkSockekt = true;

    public void server2() throws IOException {

        System.out.println("Client Server Started!");

        /** Открываем серверный канал */
        ServerSocketChannel serverChenel = ServerSocketChannel.open();
        /** Создаем Селектор */
        Selector selector = Selector.open();
        /**Убираем блокировку сокета, что бы сокет не блокировался при коннекте юзеров */
        serverChenel.configureBlocking(false);
        InetSocketAddress inetAddress = new InetSocketAddress(ip, port);
        /** Биндим серверный канал на порту */
        serverChenel.socket().bind(inetAddress);
        /** Регистрация в селекторе */
        serverChenel.register(selector, SelectionKey.OP_ACCEPT);

        Iterator<SelectionKey> iterator;

        while (serverChenel.isOpen()){
            selector.select();
            iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isValid()){
                    if (key.isAcceptable()){
                        handleAccept(key);
                    }
                    if (key.isReadable()){
                        handleRead(key);
                    }
                }
            }
        }
    }

    public void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel sc = (ServerSocketChannel) key.channel();
        Selector sel = Selector.open();
        SocketChannel socketChannel = sc.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(sel, SelectionKey.OP_READ);

        System.out.println("Someone connected: " + socketChannel.getLocalAddress());
    }

    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        StringBuilder builder = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int read = 0;

        while ((read = ch.read(buffer)) > 0){
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            builder.append(new String(bytes));
            buffer.clear();
        }
        String msg = null;
        if (read <= 0){
            msg = key.attachment() + " left that chat. \n";
            ch.close();
        }
        System.out.println(msg);
    }

    public void handleWrite(SelectionKey key){

    }

    public void handleConnect(SelectionKey key){

    }
}
