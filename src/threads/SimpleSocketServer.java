package threads;

import controllers.MainController;
import javafx.application.Platform;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleSocketServer extends Thread {
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;
    private MainController mainController;

    public SimpleSocketServer( int port, MainController mainController ) {
        this.port = port;
        this.mainController = mainController;

        try {
            System.out.println("ip address of server is  - " + InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {

        try {

            serverSocket = new ServerSocket( port );

            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopServer() {
        running = false;
        this.interrupt();
    }

    @Override
    public void run() {
        running = true;

        boolean flag = true;
        while( running ) {

            try {

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket, mainController );
                requestHandler.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(flag){
                flag = false;
                Platform.runLater(() -> //switches to GUI Thread
                {
                    mainController.activateProgrInd(false);
                });
            }

        }

    }

}
