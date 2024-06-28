import java.io.*;
import java.net.*;

public class Client {

    Socket socket;
    BufferedReader in;
    PrintWriter out;

    public Client() {

        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7714);
            System.out.println("Connection established");

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
                        System.out.println("Server terminated the chat");
                        break;
                    }
                    System.err.println("Server : " + message);
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
        System.out.println("Hey I'm a client");
        new Client();
    }
}
