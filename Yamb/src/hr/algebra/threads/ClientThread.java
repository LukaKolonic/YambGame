/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threads;

import hr.algebra.controller.MainController;
import hr.algebra.model.Move;
import hr.algebra.model.Player;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import utilities.ByteUtils;

/**
 *
 * @author lukak
 */
public class ClientThread extends Thread {

    private List<Move> moves = new ArrayList<>();
    private final MainController controller;
    private Player player;


    public ClientThread(String name, MainController controller, Player player) {
        super(name);
        this.controller = controller;
        this.player = player;
    }

    public static final String PROPERTIES_FILE = "configuration.properties";
    public static final String PORT = "PORT";
    public static final String GROUP = "GROUP";

    public static final Properties PROPERTIES = new Properties();

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    static {
        try {
            PROPERTIES.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void trigger(List<Move> movesToSend) {
        moves = movesToSend;
    }

    @Override
    public void run() {

        try {
            MulticastSocket clientSocket = new MulticastSocket(Integer.valueOf(PROPERTIES.getProperty(PORT)));

            InetAddress groupAddress = InetAddress.getByName(PROPERTIES.getProperty(GROUP));
            System.err.println(getName() + " joining group");
            clientSocket.joinGroup(groupAddress);
            
            sendPacketToServer(moves, clientSocket, groupAddress);

            
            try {
                byte[] numberOfBytes = new byte[64];
                DatagramPacket packet = new DatagramPacket(numberOfBytes, numberOfBytes.length);
                clientSocket.receive(packet);
                int length = ByteUtils.byteArrayToInt(numberOfBytes);

                byte[] bytes = new byte[length];
                packet = new DatagramPacket(bytes, bytes.length);
                clientSocket.receive(packet);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                        ObjectInputStream ois = new ObjectInputStream(bais)) {
                    List<Move> movesReceived = (List<Move>) ois.readObject();

                    if (movesReceived.size() > 0) {
                        Platform.runLater(() -> {
                            controller.showMove(movesReceived);
                        });
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }

                Thread.sleep(1000);
                sendPacketToServer(moves, clientSocket, groupAddress);


                Thread.sleep(1000);


                new Timer().scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        try {
                            byte[] numberOfOpponentBytes = new byte[64];
                            DatagramPacket packetReceivedOpponent = new DatagramPacket(numberOfOpponentBytes, numberOfOpponentBytes.length);
                            clientSocket.receive(packetReceivedOpponent);
                            int lengthOpponent = ByteUtils.byteArrayToInt(numberOfOpponentBytes);

                            byte[] receivedOpponentBytes = new byte[lengthOpponent];
                            packetReceivedOpponent = new DatagramPacket(receivedOpponentBytes, receivedOpponentBytes.length);
                            clientSocket.receive(packetReceivedOpponent);
                            try (ByteArrayInputStream baisOpponent = new ByteArrayInputStream(receivedOpponentBytes);
                                    ObjectInputStream oisOpponent = new ObjectInputStream(baisOpponent)) {
                                List<Move> movesReceived2 = (List<Move>) oisOpponent.readObject();

                                Platform.runLater(() -> {
                                    controller.showMove(movesReceived2);
                                });
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        } catch (Exception ex) {
                            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }, 0, 500);

               
            } catch (IOException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);

            } catch (InterruptedException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private void sendPacketToServer(List<Move> moves, MulticastSocket clientSocket, InetAddress groupAddress) {

        if (moves.size() > 0) {

            byte[] bytes;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(moves);
                bytes = baos.toByteArray();

                byte[] numberOfWordBytes = ByteUtils.intToByteArray(bytes.length);
                DatagramPacket packet = new DatagramPacket(
                        numberOfWordBytes,
                        numberOfWordBytes.length,
                        groupAddress, Integer.valueOf(PROPERTIES.getProperty(PORT))
                );
                clientSocket.send(packet);

                packet = new DatagramPacket(
                        bytes,
                        bytes.length,
                        groupAddress, Integer.valueOf(PROPERTIES.getProperty(PORT)));
                clientSocket.send(packet);
                System.out.println(getName() + " sent move! ");
            } catch (IOException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}