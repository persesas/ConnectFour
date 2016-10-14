package ch.epfl.power_four.game.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Broadcaster implements Runnable{
    private String msg;
    private String receiver;


    public Broadcaster(String receiver, String msg){
        this.receiver = receiver;
        this.msg = msg;
    }


    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            String[] split = receiver.split(" ");
            String ip = split[0];
            int port = Integer.parseInt(split[1]);
            InetAddress addr = InetAddress.getByName(ip);

            DatagramPacket packet = new DatagramPacket(msg.getBytes(),
                    msg.getBytes().length, addr, port);
            socket.send(packet);
            System.out.println("(BROADCASTER) sent: " + msg  + " to: " + addr.toString() +","+ port);
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("(ERROR) Unknown host" + " " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
