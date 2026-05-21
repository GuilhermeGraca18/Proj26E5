import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static final int PORTA = 5001;

    public static final List<ObjectOutputStream> monitores = new ArrayList<>();
    public static final GerirCantina gerir = new GerirCantina();

     static void main() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {

            System.out.println("Servidor ligado na porta " + PORTA);

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Novo cliente ligado: " + socketCliente.getInetAddress());

                new ClienteHandler(socketCliente).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // METODO PARA ATUALIZAR O MONITOR

    public static void atualizarMonitores() {
        ArrayList<Pedido> pedidosPendentes = new ArrayList<>();

        synchronized (gerir.getPedidos()) {
            for (Pedido pedido : gerir.getPedidos()) {
                if (pedido.getEstado() == EstadoPedido.A_FAZER ||
                        pedido.getEstado() == EstadoPedido.A_ENTREGAR) {

                    pedidosPendentes.add(pedido);
                }
            }
        }

        synchronized (monitores) {
            for (ObjectOutputStream monitor : monitores) {
                try {
                    monitor.writeObject(new Mensagem("LISTA_PEDIDOS", pedidosPendentes));
                    monitor.flush();
                    monitor.reset();
                } catch (IOException e) {
                    System.out.println("Erro ao avisar monitor.");
                }
            }
        }
    }
}