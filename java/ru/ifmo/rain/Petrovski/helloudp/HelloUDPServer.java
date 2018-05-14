package ru.ifmo.rain.Petrovski.helloudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.kgeorgiy.java.advanced.hello.HelloServer;
import static ru.ifmo.rain.Petrovski.helloudp.HelloUDPUtility.*;

public class HelloUDPServer implements HelloServer {

    private DatagramSocket socket;
    private List<Thread> threads;

    public void start(int port, int threadsCount) {
        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            System.out.println("Problem with socket opening: " + e.getMessage());
            return;
        }

        Runnable worker = () -> {
            try {
                byte[] buf = new byte[socket.getReceiveBufferSize()];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                while (!socket.isClosed()) {
                    socket.receive(packet);
                    send(socket, packet.getAddress(), packet.getPort(),
                            "Hello, " + new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8)
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        threads = Stream.generate(() -> new Thread(worker))
                .limit(threadsCount)
                .collect(Collectors.toList());
        threads.forEach(Thread::start);
    }

    public void close() {
        if (socket != null) {
            socket.close();
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
