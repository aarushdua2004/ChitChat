import java.net.*;
import java.io.*;

class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader in;
    PrintWriter out;

    public Server() {
        try {
            server = new ServerSocket(7714);
            System.out.println("The Server is ready to accept...");
            System.out.println("waiting...");
            socket = server.accept();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        // thread to read
        Runnable r1 = () -> {
            System.out.println("reader started");
            while (true) {
                try {
                    String message = in.readLine();
                    if (message.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        break;
                    }
                    System.err.println("Client : " + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread to get data from user and send to client

        Runnable r2 = () -> {
            System.out.println("writer started");
            while (true) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String content = br.readLine();
                    out.println(content);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Hey!, I'm a server");
        new Server();
    }
}