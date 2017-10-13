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


//        // Selector: multiplexor of SelectableChannel objects
//        Selector selector = Selector.open(); // selector is open here
//
//        // ServerSocketChannel: selectable channel for stream-oriented listening sockets
//        ServerSocketChannel crunchifySocket = ServerSocketChannel.open();
//        InetSocketAddress crunchifyAddr = new InetSocketAddress("localhost", 10523);
//
//        // Binds the channel's socket to a local address and configures the socket to listen for connections
//        crunchifySocket.bind(crunchifyAddr);
//
//        // Adjusts this channel's blocking mode.
//        crunchifySocket.configureBlocking(false);
//
//        int ops = crunchifySocket.validOps();
//        SelectionKey selectKy = crunchifySocket.register(selector, ops, null);
//
//        // Infinite loop..
//        // Keep server running
//        while (true) {
//
//            log("i'm a server and i'm waiting for new connection and buffer select...");
//            // Selects a set of keys whose corresponding channels are ready for I/O operations
//            selector.select();
//
//            // token representing the registration of a SelectableChannel with a Selector
//            Set<SelectionKey> crunchifyKeys = selector.selectedKeys();
//            Iterator<SelectionKey> crunchifyIterator = crunchifyKeys.iterator();
//
//            while (crunchifyIterator.hasNext()) {
//                SelectionKey myKey = crunchifyIterator.next();
//
//                // Tests whether this key's channel is ready to accept a new socket connection
//                if (myKey.isAcceptable()) {
//                    SocketChannel crunchifyClient = crunchifySocket.accept();
//
//                    // Adjusts this channel's blocking mode to false
//                    crunchifyClient.configureBlocking(false);
//
//                    // Operation-set bit for read operations
//                    crunchifyClient.register(selector, SelectionKey.OP_READ);
//                    log("Connection Accepted: " + crunchifyClient.getLocalAddress() + "\n");
//
//                    // Tests whether this key's channel is ready for reading
//                } else if (myKey.isReadable()) {
//
//                    SocketChannel crunchifyClient = (SocketChannel) myKey.channel();
//                    ByteBuffer crunchifyBuffer = ByteBuffer.allocate(256);
//                    crunchifyClient.read(crunchifyBuffer);
//                    String result = new String(crunchifyBuffer.array()).trim();
//
//                    log("Message received: " + result);
//
//                    if (result.equals("Crunchify")) {
//                        crunchifyClient.close();
//                        log("\nIt's time to close connection as we got last company name 'Crunchify'");
//                        log("\nServer will keep running. Try running client again to establish new connection");
//                    }
//                }
//                crunchifyIterator.remove();
//            }
//
//        }
//    }

//    private static void log(String str) {
//        System.out.println(str);
//    }


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

//        String name = "welcome 1";
//        byte[] bName = name.getBytes();
//        String name2 = new String(bName);
//        ByteBuffer buffer = ByteBuffer.wrap(bName);
//        System.out.println(name2);
//        byte[] array = buffer.array();
//        String array2 = new String(array);
//        System.out.println("That is converted second byte array: " + array2);

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
                       }
                System.out.println("Cейчас мы должны отправлять!");
                      if (key.isWritable()){
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
        buffer.clear();
        String name = new String(bytes);
        System.out.println(name);
//        channel.close();
    }

    public void handleWrite(SelectionKey key) throws IOException {
        System.out.println("Сейчас что то отправим!");
        SocketChannel channel = (SocketChannel) key.channel();
        buffer.clear();
        String welcome = "Welcome to Server";
        byte[] bytes = welcome.getBytes();
        buffer.put(Byte.parseByte(welcome));
        channel.write(ByteBuffer.wrap(bytes));
        System.out.println(bytes + " bytes written");

    }

    public void handleConnect(SelectionKey key){

    }
}
