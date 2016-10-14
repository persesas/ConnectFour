package ch.epfl.power_four.game.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.epfl.power_four.game.WorldController;

public class Server {
    WorldController worldController;

    private Thread serverThread;
    private DatagramSocket serverSocket;
    private ServerRunnable serverTask;

    // This class receives data from the other player-client
    public Server(WorldController worldController){
        this.worldController = worldController;
    }


    class ServerRunnable implements Runnable {

        private boolean isRunning = true;
        private int port;
        private final ExecutorService clientProcessingPool;

        public ServerRunnable(int port, ExecutorService clientProcessingPool){
            this.port = port;
            this.clientProcessingPool = clientProcessingPool;
        }

        @Override
        public void run() {
            try {
                serverSocket = new DatagramSocket(port);
                byte[] recv_data = new byte[1024];
                System.out.println("(SERVER) Server up and running on port " + port);
                while (isRunning) {
                    DatagramPacket recv_packet = new DatagramPacket(recv_data, recv_data.length);
                    serverSocket.receive(recv_packet);
                    clientProcessingPool.submit(new ClientTask(recv_packet));
                }

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void terminate(){
            serverSocket.close();
            isRunning = false;
        }
    }

    public void startServer(final int port) {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);
        serverTask = new ServerRunnable(port, clientProcessingPool);
        serverThread = new Thread(serverTask);
        serverThread.start();
    }


    private class ClientTask implements Runnable {
        private final DatagramPacket recv_packet;

        private ClientTask(DatagramPacket packet) {
            this.recv_packet = packet;
        }

        @Override
        public void run() {
            String data = new String(this.recv_packet.getData(), 0, this.recv_packet.getLength());
            String[] table = data.split(",");
            String command = table[0];
            if (command.equals("connect")) {
                try {
                    String name = table[1];
                    InetAddress ip_address = InetAddress.getByName(table[2]);
                    int port = Integer.parseInt(table[3]);
                    worldController.otherPlayer = new Player(Integer.parseInt(name), ip_address, port);
                    System.out.println("(SERVER) Player connected:" + worldController.otherPlayer.toString());
                    BroadcasterMediator bm = new BroadcasterMediator(worldController);
                    bm.connect_ack(ip_address.getHostAddress(), Integer.toString(port));
                    //TODO start new game
                    worldController.connected = true;
                    worldController.clearGame();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            else if(command.equals("connect_ack")){
                String name = table[1];
                System.out.println("(SERVER) Player ack connect:" + worldController.otherPlayer.toString());
                worldController.connected = true;
                worldController.clearGame();
            }
            else if(command.equals("play_col_y")){
                int col_played = Integer.parseInt(table[1]);
                System.out.println("(SERVER) Player played " + col_played);
                BroadcasterMediator bm = new BroadcasterMediator(worldController);
                bm.ack_play_col_y(col_played);
                //TODO play corr. col
                worldController.playCol(col_played, true);
            }
            else if(command.equals("ack_play_col_y")){
                int col_played = Integer.parseInt(table[1]);
                System.out.println("(SERVER) ack Player played " + col_played);
                worldController.timer.clear();
            }
            else if(command.equals("clear_game")){
                System.out.println("(SERVER) clear game");
                worldController.clearGame();
            }
        }
    }
}
