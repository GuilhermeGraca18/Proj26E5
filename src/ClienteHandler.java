import java.io.*;
import java.net.*;

public class ClienteHandler extends Thread {
    private Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;

    private Utilizador user = null;
    private String tipoUser = null;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            saida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Mensagem msg = (Mensagem) entrada.readObject();

                if (msg.getTipo().equalsIgnoreCase("MONITOR")) {
                    System.out.println("Monitor ligado.");

                    synchronized (Servidor.monitores) {
                        Servidor.monitores.add(saida);
                    }

                    saida.writeObject(new Mensagem("INFO", "Monitor ligado com sucesso."));
                    saida.flush();
                    continue;
                }

                if (msg.getTipo().equalsIgnoreCase("LOGIN_CLIENTE")) {
                    tipoUser = "Cliente";
                    user = (Utilizador) msg.getDados();

                    saida.writeObject(new Mensagem("INFO", user.getNome() + " - Bem-vindo"));
                    saida.flush();

                    System.out.println(user.getCodigo() + " - LOGIN");

                } else if (msg.getTipo().equalsIgnoreCase("CRIAR_PEDIDO")) {
                    if (user == null) {
                        saida.writeObject(new Mensagem("ERRO", "[ATENÇÃO] Primeiro faça login!"));
                        saida.flush();
                    } else {
                        Pedido pedido = (Pedido) msg.getDados();

                        synchronized (Servidor.pedidos) {
                            Servidor.pedidos.add(pedido);
                        }

                        saida.writeObject(new Mensagem("INFO", "Pedido criado com sucesso."));
                        saida.flush();

                        Servidor.atualizarMonitores();
                    }

                } else if (msg.getTipo().equalsIgnoreCase("SAIR")) {
                    if (user != null) {
                        System.out.println(user.getCodigo() + " - TERMINOU A SESSÃO");
                    }

                    saida.writeObject(new Mensagem("INFO", "Ligação terminada."));
                    saida.flush();
                    break;
                }
            }

            socket.close();

        } catch (Exception e) {
            System.out.println("Cliente/Monitor desligado.");
            System.out.println("[CONSOLE ERROR] - " + e);
        } finally {
            synchronized (Servidor.monitores) {
                Servidor.monitores.remove(saida);
            }
        }
    }
}