import java.io.*;
import java.net.*;

public class Servidor {
    private static final int PORTA = 5001;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {

            System.out.println("Servidor ligado na porta " + PORTA);

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Novo cliente ligado: " + socketCliente.getInetAddress());

                ClienteHandler handler = new ClienteHandler(socketCliente);
                handler.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}