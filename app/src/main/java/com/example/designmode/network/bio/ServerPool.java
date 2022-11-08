package com.example.designmode.network.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerPool {

    private static ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() *2);
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(10001));
        System.out.println("start server");
        try {
            while (true) {
                executorService.execute(new Server.ServerTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }
    }

    static class ServerTask implements Runnable {
        Socket socket;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream inputStream =
                        new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream =
                        new ObjectOutputStream(socket.getOutputStream());
                String userName = inputStream.readUTF();
                System.out.println("accept client message");
                outputStream.writeUTF(userName);
                outputStream.flush();
            } catch (Exception e) {e.printStackTrace();}
            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
