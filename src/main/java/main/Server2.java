package main;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server2 extends Thread {

    private String ip = "127.0.0.1";
    private int port = 10523;
    private ServerSocket ss;
    private Socket socket;
    private boolean checkSockekt = true;
    private ByteBuffer buffer = ByteBuffer.allocate(8290);

    public void server2() throws IOException, InterruptedException {

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

        /**
         * ServerSocketChanel:
         * OP_ACCEPT - Входящее соединение
         *
         * SocketChanel:
         * OP_READ - на сокете данные или дискннект
         * OP_WRITE - сокет готов к записи или дисконнект
         * OP_CONNECT - соединение установено или нет
         *
         * */

        Iterator<SelectionKey> iterator;

        while (checkSockekt == true){
            selector.select(); //Здеcь сервер начинает ждать подключения

            Set<SelectionKey> listSelector = selector.selectedKeys();
            iterator = listSelector.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();

                if (key.isAcceptable()){
                    handleAccept(key);
                }else if (key.isReadable()) {
                          handleRead(key);
                }else if (key.isWritable()){
                          System.out.println("aslkjgbaklgjbakfjngafn");
                          handleWrite(key);
                }

                iterator.remove();
            }
        }
    }

    public void handleAccept(SelectionKey key) throws IOException {
        SocketChannel sc = ((ServerSocketChannel) key.channel()).accept(); //Принял
        sc.configureBlocking(false); //Делаем socket не блокирующим при передачи даных
        sc.register(key.selector(), SelectionKey.OP_READ); //Добавляем в Селектор

        System.out.println("Someone connected: " + sc.getLocalAddress());
    }

    public void handleRead(SelectionKey key) throws IOException {

        System.out.println("We must read it!");

        SocketChannel channel = (SocketChannel) key.channel();
        channel.read(buffer);
        byte[] bytes = buffer.array();
//        buffer.clear();
        String name = new String(bytes);
        System.out.println(name);
        channel.register(key.selector(), SelectionKey.OP_WRITE);
//        channel.close();
    }

    public void handleWrite(SelectionKey key) throws IOException {
        System.out.println("Сейчас что то отправим!");
        SocketChannel channel = (SocketChannel) key.channel();
        buffer.clear();
        String welcome = "Welcome to Server";
        byte[] bytes = welcome.getBytes();
        buffer.put(bytes);
        channel.write(buffer);
        System.out.println(bytes + " bytes written");
        channel.register(key.selector(), SelectionKey.OP_READ);

    }

    public void handleConnect(SelectionKey key){

        SocketChannel channel = (SocketChannel) key.channel();


    }
}
