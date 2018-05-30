package ru.ifmo.rain.Petrovski.helloudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.kgeorgiy.java.advanced.hello.HelloServer;
import static ru.ifmo.rain.Petrovski.helloudp.HelloUDPUtility.*;

public class HelloUDPServer implements HelloServer {

    private DatagramSocket socket;
    private ExecutorService accepter;
    private ExecutorService executor;

    public void start(int port, int threadsCount) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Problem with socket opening: " + e.getMessage());
            return;
        }

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(155555);
        accepter = Executors.newSingleThreadExecutor();
        executor = new ThreadPoolExecutor(threadsCount, threadsCount, 5, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.DiscardPolicy());

        Runnable accepter_worker = () -> {
            try {
                while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
                    byte[] buf = new byte[socket.getReceiveBufferSize()];
                    final DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    executor.submit(() -> {
                        try {
                            send(socket, packet.getAddress(), packet.getPort(),
                                    "Hello, " + new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8)
                            );
                        } catch (IOException e) {
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        accepter.submit(accepter_worker);
    }

    public void close() {
        accepter.shutdownNow();
        executor.shutdownNow();
        if (socket != null) {
            socket.close();
        }
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }

    public static void main(String... args) {
        if (args == null || args.length != 2) {
            System.out.println("Usage: java HelloUDPServer <port> <number of threads>");
            return ;
        }
        if (!isNumber(args[0])) {
            System.out.println("Expected integer number for the port");
            return ;
        }
        if (!isNumber(args[1])) {
            System.out.println("Expected integer number for the number of threads");
            return ;
        }
        new HelloUDPServer().start(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
