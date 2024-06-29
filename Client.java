import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);
    private Font font1 = new Font("Roboto", Font.BOLD, 20);

    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7778);
            System.out.println("Connection established");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            createGUI();
            handleEvents();
            startReading();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Handle key typed event
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Handle key pressed event
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Handle key released event
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String contentToSend = messageInput.getText();
                    sendMessage(contentToSend);
                    messageInput.setText("");
                }
            }
        });
    }

    private void createGUI() {
        // Set up JFrame properties
        setTitle("Client Messenger [END]");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon originalIcon = new ImageIcon("logo1.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize to desired
                                                                                          // dimensions
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setIcon(resizedIcon);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        heading.setFont(font1);
        messageArea.setFont(font);
        messageInput.setFont(font);

        // Set layout manager for JFrame
        setLayout(new BorderLayout());

        // Add components to JFrame
        add(heading, BorderLayout.NORTH);
        add(new JScrollPane(messageArea), BorderLayout.CENTER); // Wrap messageArea in a JScrollPane
        add(messageInput, BorderLayout.SOUTH);

        // Set JFrame visibility
        setVisible(true);
    }

    private void startReading() {
        // thread to read
        Runnable r1 = () -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("exit")) {
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    showMessage("Server: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        new Thread(r1).start();

    }

    private void sendMessage(String message) {
        out.println(message);
        out.flush();
        showMessage("Me: " + message);
    }

    private void showMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append(message + "\n");
            messageArea.setCaretPosition(messageArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Hey, I'm a client");
            new Client();
        });
    }
}
