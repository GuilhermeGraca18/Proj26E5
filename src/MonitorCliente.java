import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MonitorCliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5001)) {

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            saida.writeObject(new Mensagem("MONITOR", null));
            saida.flush();

            while (true) {
                Mensagem msg = (Mensagem) entrada.readObject();

                if (msg.getTipo().equals("LISTA_PEDIDOS")) {
                    ArrayList<Pedido> pedidos = (ArrayList<Pedido>) msg.getDados();

                    limparConsola();

                    System.out.println("=== PEDIDOS PENDENTES ===");

                    if (pedidos.isEmpty()) {
                        System.out.println("Sem pedidos pendentes.");
                    } else {
                        for (Pedido pedido : pedidos) {
                            System.out.println(pedido);
                            System.out.println("----------------------");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void limparConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}