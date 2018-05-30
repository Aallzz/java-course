package ru.ifmo.rain.Petrovski.helloudp;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import static ru.ifmo.rain.Petrovski.helloudp.HelloUDPUtility.*;

public class HelloUDPClient implements HelloClient{

    private InetAddress host;
    private ExecutorService worker;

    private boolean checkReceivedMessage(String request, String response) {
        if (!response.contains(request) || request.equals(response)) {
            return false;
        }
        if (response.endsWith(request)) {
            return true;
        }
        Pattern pattern = Pattern.compile(request + "\\s");
        Matcher matcher = pattern.matcher(response);
        return matcher.find();
    }

    public void run(String hostName, int port, String prefix, int threadsCount, int queries) {
        try {
            host = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            System.out.println("Problem with getting host address: " + e.getMessage());
            return ;
        }

        IntFunction<Runnable> generateWorker = n -> () -> {
            try {
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packet = createReceiveDatagram(socket);
                socket.setSoTimeout(100);
                for (int i = 0; i < queries; ++i) {
                    try {
                        String request = prefix + n + "_" + i;
                        send(socket, this.host, port, request);
                        socket.receive(packet);
                        String received = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                        if (checkReceivedMessage(request, received)) {
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

        worker = Executors.newFixedThreadPool(threadsCount);
        IntStream.range(0, threadsCount).mapToObj(generateWorker).forEach(worker::submit);
        worker.shutdown();
        try {
            worker.awaitTermination(threadsCount * queries, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length != 5) {
            System.out.println("Usage: java HelloUDPServer <Server name or ip> <port> <prefix query> " +
                    "<number of parallel queries> <number of queries in thread>");
        } else if (!isNumber(args[1])) {
            System.out.println("Expected integer number for the port");
        } else if (!isNumber(args[3])) {
            System.out.println("Expected integer number for the number of parallel queries");
        } else if (!isNumber(args[4])) {
            System.out.println("Expected integer number for the number of queries in thread");
        } else {
            new HelloUDPClient().run(args[0], Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        }
    }
}