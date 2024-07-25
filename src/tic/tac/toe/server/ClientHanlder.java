package tic.tac.toe.server;

import DataAccessLayer.DAL;
import DataAccessLayer.MoveDTO;
import DataAccessLayer.PlayerDTO;
import DataAccessLayer.RequestDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHanlder extends Thread {

    private ObjectInputStream dis;
    private ObjectOutputStream dos;
    private static Vector<ClientHanlder> clients = new Vector<>();
    private static ArrayList<PlayerDTO> onlinePlayers = new ArrayList<>();
    private String username;
    Socket socket;

    public ClientHanlder(Socket s, PlayerDTO playerDTO) {
        try {
            dis = new ObjectInputStream(s.getInputStream());
            dos = new ObjectOutputStream(s.getOutputStream());
            this.username = playerDTO.getUsername();
            start();
        } catch (IOException ex) {
            Logger.getLogger(ClientHanlder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object receivedObject = dis.readObject();

                if (receivedObject instanceof PlayerDTO) {
                    PlayerDTO p = (PlayerDTO) receivedObject;
                    int screenIndicator = p.getScreenIndicator();
                    DAL dal = new DAL();

                    switch (screenIndicator) {
                        case -1: // case sign in 
                            if (dal.checdAndLogin(p)) {
                                dal.updateIpAndStatusOnline(p);
                                this.username = p.getUsername();
                                //System.out.println(p.getUsername());
                                synchronized (clients) {
                                    clients.add(this);
                                    logConnectedClients();
                                }

                                dos.writeObject("true"); // sussefully log in 

                            } else {
                                dos.writeObject("false"); // failed log in 
                            }
                            break;
                        case 0: // sign up 
                            if (dal.isUsernameExists(p.getUsername())) {
                                dos.writeObject("This user already exists!"); // already exists user 
                            } else {
                                dal.insert(p);
                                dos.writeObject("Registered successfully"); // sussefully register 
                            }
                            break;
                        case 3:
                            onlinePlayers = dal.getOnlinePlayers();
                            for (PlayerDTO pp : onlinePlayers) {
                                dos.writeObject(pp); // send to all online player 
                            }
                            break;
                        case 4: // case signout 
                            System.out.println(p.getUsername());
                            dal.updateStatusOffline(p);
                            break;
                        default:
                            break;
                    }

                }else if (receivedObject instanceof MoveDTO)
                {
                    System.out.println("ana moveDTO w gwa el server ");
                    MoveDTO move = (MoveDTO) receivedObject;
                    
                    System.out.println("ana gwa el moveDTO fel server w da esm elli 3ayez awsallo el move " +move.getReciver_userName());
                       
                       
                     synchronized (clients) {
            for (ClientHanlder client : clients) {
                if (move.getReciver_userName().equals(client.username)) {
                    
                       
                        client.dos.writeObject(move);
                        client.dos.flush();
                        System.out.println("Forwarded move to client: " + move.getReciver_userName());
                        break;
                    
                }
            }
        }
                    
                    
                }
                else if (receivedObject instanceof RequestDTO) {
                    RequestDTO request = (RequestDTO) receivedObject;
                    System.out.println("Received RequestDTO with screenIndicator: " + request.getScreenIndicator());
                    String receiverUsername = request.getReciver_username();
                    String senderUsername = request.getSender_username();
                    switch (request.getScreenIndicator()) {

                        case 5: // sending sequest to another player 

                            System.out.println(senderUsername + " sent a request to play with " + receiverUsername);
                            forwardRequestToClient(request);
                            break;
                        case 6: // accepting the request 

                            //String senderUsername = request.getReciver_username();
                            System.out.println("Accepted request from: " + senderUsername);
                            for (ClientHanlder client : clients) {
                                System.out.println("Checking client: " + client.username);
                                if (senderUsername.equals(client.username)) {
                                    request.setScreenIndicator(6);
                                    client.dos.writeObject(request);
                                    client.dos.flush();
                                    System.out.println("Sent acceptance to client: " + senderUsername);
                                    break;
                                }
                            }
                            break;
                        default:
                            System.out.println("Unhandled screenIndicator: " + request.getScreenIndicator());
                            break;
                    }
                }

                dos.flush();
            }
        } catch (SocketException ex) {
            Logger.getLogger(ClientHanlder.class.getName()).log(Level.SEVERE, "SocketException: Connection reset", ex);
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClientHanlder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
            synchronized (clients) {
                clients.remove(this);
            }
            // Update the online players list or notify other clients about disconnection
        }
    }

    private void forwardRequestToClient(RequestDTO request) {
        String receiverUsername = request.getReciver_username();
        synchronized (clients) {
            for (ClientHanlder client : clients) {
                if (receiverUsername.equals(client.username)) {
                    try {
                        request.setScreenIndicator(8);
                        client.dos.writeObject(request);
                        client.dos.flush();
                        System.out.println("Forwarded request to client: " + receiverUsername);
                        return;
                    } catch (IOException ex) {
                        Logger.getLogger(ClientHanlder.class.getName()).log(Level.SEVERE, "Error forwarding request", ex);
                    }
                }
            }
        }
        System.out.println("Client not found: " + receiverUsername);
    }

    public void closeConnection() {
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }

        } catch (IOException e) {
            Logger.getLogger(ClientHanlder.class.getName()).log(Level.SEVERE, "Error closing connection", e);
        }
    }

    private static void logConnectedClients() {
        System.out.println("Connected clients:");
        for (ClientHanlder client : clients) {
            System.out.println("- " + client.username);
        }
    }

}