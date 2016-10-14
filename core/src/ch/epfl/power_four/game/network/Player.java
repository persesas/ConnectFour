package ch.epfl.power_four.game.network;

import java.net.InetAddress;

public class Player {
    public int name;
    InetAddress ip_address;
    int port;

    public Player(int name, InetAddress ip_address, int port){
        this.name = name;
        this.ip_address = ip_address;
        this.port = port;
    }

    @Override
    public String toString(){
        return name + " " + ip_address.toString() + "," + port;
    }
}
