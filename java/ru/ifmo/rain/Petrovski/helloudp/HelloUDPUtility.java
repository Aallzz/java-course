package ru.ifmo.rain.Petrovski.helloudp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class HelloUDPUtility {

    public static DatagramPacket createReceiveDatagram(DatagramSocket socket) throws SocketException {
        byte[] buf;
        DatagramPacket packet;
        buf = new byte[socket.getReceiveBufferSize()];
        packet = new DatagramPacket(buf, buf.length);
        return packet;
    }

    public static void send(DatagramSocket socket, InetAddress host, int port, String request) throws IOException {
        byte[] buf = request.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, host, port);
        socket.send(packet);
    }

    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
