import java.io.*;
import java.net.*;

public class ClienteHandler extends Thread {
    private Socket socket;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter saida = new PrintWriter(
                    socket.getOutputStream(), true
            );

            saida.println("Ligado ao servidor da cantina!");

            String mensagem;

            while ((mensagem = entrada.readLine()) != null) {
                System.out.println("Cliente disse: " + mensagem);

                if (mensagem.equalsIgnoreCase("SAIR")) {
                    saida.println("Ligação terminada.");
                    break;
                }

                if (mensagem.equalsIgnoreCase("LOGIN_CLIENTE")) {
                    saida.println("Login cliente recebido.");
                } else if (mensagem.equalsIgnoreCase("LOGIN_FUNCIONARIO")) {
                    saida.println("Login funcionário recebido.");
                } else if (mensagem.equalsIgnoreCase("REGISTAR_CLIENTE")) {
                    saida.println("Registo cliente recebido.");
                } else if (mensagem.equalsIgnoreCase("CRIAR_PEDIDO")) {
                    saida.println("Pedido criado com sucesso.");
                } else {
                    saida.println("Comando recebido: " + mensagem);
                }
            }

            socket.close();
            System.out.println("Cliente desligado.");

        } catch (IOException e) {
            System.out.println("Cliente perdeu ligação.");
        }
    }
}