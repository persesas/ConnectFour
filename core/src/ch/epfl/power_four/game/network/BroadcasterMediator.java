package ch.epfl.power_four.game.network;

import ch.epfl.power_four.game.WorldController;

public class BroadcasterMediator {
    private WorldController worldController;

    public BroadcasterMediator(WorldController worldController){
        this.worldController = worldController;
    }

    public void connect(String dest_ip_addr, String dest_port, String ip_addr, int port) {

        if(dest_ip_addr.equals(ip_addr) && dest_port.equals(Integer.toString(port)) ) {
            System.out.println("(WARN) You trying to connect to yourself.");
            return;
        }

        String msg = "connect," + 1 + "," + ip_addr + "," + port;        // connect,1,ip,port
        String dest = dest_ip_addr + " " + dest_port;                    // Translates to: player 1 sends a connection req from ip, port
        Broadcaster b = new Broadcaster(dest, msg);
        (new Thread(b)).start();
        worldController.waitingAck = true;
    }

    public void connect_ack(String dest_ip_addr, String dest_port){
        String msg = "connect_ack," + 2;                                // connect_ack,2
        String dest = dest_ip_addr + " " + dest_port;                   // Translates to: player 2 ack connection
        Broadcaster b = new Broadcaster(dest, msg);
        (new Thread(b)).start();
    }

    public void play_col_y(int col){
        String msg = "play_col_y," + col;       // play_col_y,col
        String dest = worldController.otherPlayer.ip_address.getHostAddress() + " " + worldController.otherPlayer.port;
        Broadcaster b = new Broadcaster(dest, msg);
        (new Thread(b)).start();
        worldController.waitingAck = true;
    }

    public void ack_play_col_y(int col){
        String msg = "ack_play_col_y," + col;       // ack_play_col_y,col
        String dest = worldController.otherPlayer.ip_address.getHostAddress() + " " + worldController.otherPlayer.port;
        Broadcaster b = new Broadcaster(dest, msg);
        (new Thread(b)).start();
    }


    public void clear_game() {
        String msg = "clear_game,";
        String dest = worldController.otherPlayer.ip_address.getHostAddress() + " " + worldController.otherPlayer.port;
        Broadcaster b = new Broadcaster(dest, msg);
        (new Thread(b)).start();
    }
}
