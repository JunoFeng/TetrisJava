import java.io.*;
import java.net.Socket;

public class ExternalPlayerHandler {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean connected = false;

    public ExternalPlayerHandler() {
        try {
            socket = new Socket("localhost", 3000);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
        } catch (IOException e) {
            System.err.println("Failed to connect to TetrisServer on localhost:3000. Server might not be running.");
            connected = false;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public String receiveCommand() {
        if (connected) {
            try {
                return input.readLine(); // Read command from server
            } catch (IOException e) {
                System.err.println("Connection to TetrisServer lost.");
                connected = false;
            }
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }
}
