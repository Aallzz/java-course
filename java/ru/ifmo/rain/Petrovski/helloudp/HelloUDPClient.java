package ru.ifmo.rain.Petrovski.helloudp;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import static ru.ifmo.rain.Petrovski.helloudp.HelloUDPUtility.*;

public class HelloUDPClient implements HelloClient{

    private List<Thread> threads;
    private InetAddress host;

    public void run(String hostName, int port, String prefix, int threadsCount, int queries) {
        try {
            host = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            System.out.println("Problem with getting host address: " + e.getMessage());
            return ;
        }

        Function<Integer, Runnable> generateWorker = (final Integer n) ->
            () -> {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    DatagramPacket packet = createReceiveDatagram(socket);
                    socket.setSoTimeout(100);
                    for (int i = 0; i < queries; ++i) {
                        try {
                            String request = prefix + n + "_" + i;
                            send(socket, this.host, port, request);
                            socket.receive(packet);
                            String received = new String(packet.getData(), packet.getOffset(), packet.getLength(), Charset.forName("UTF-8"));
                            if (received.contains(request)) {
                                System.out.println(received);
                            } else {
                                --i;
                            }
                        } catch (Exception e) {
                            --i;
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            };

        threads = IntStream.range(0, threadsCount).mapToObj((int i) -> new Thread(generateWorker.apply(i))).collect(Collectors.toList());
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });
    }

    public static void main(String[] args) {
        if (args == null || args.length != 5) {
            System.out.println("Usage: java HelloUDPServer <Server name or ip> <port> <prefix query> " +
                    "<number of parallel queries> <number of queries in thread>");
            return;
        }
        if (!isNumber(args[1])) {
            System.out.println("Expected integer number for the port");
            return;
        }
        if (!isNumber(args[3])) {
            System.out.println("Expected integer number for the number of parallel queries");
            return ;
        }
        if (!isNumber(args[4])) {
            System.out.println("Expected integer number for the number of queries in thread");
            return ;
        }
        new HelloUDPClient().run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));

    }
}