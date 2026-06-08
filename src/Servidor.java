import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor que conecta todos os clientes entre si e partilha os dados com os mesmos.
 * @author Grupo 5
 * @version 07/07/2026
 */

public class Servidor {
    private static final int PORTA = 5001;

    public static final Map<Integer, ObjectOutputStream> clientesLigados = new ConcurrentHashMap<>(); // UTILIZADORES LIGADOS

    public static final List<ObjectOutputStream> monitores = new ArrayList<>();

    public static final GerirCantina gerir = GerirCantina.getInstance();

     public static void main(String[] args) {
        gerir.carregarDados();

         Runtime.getRuntime().addShutdownHook(new Thread(() -> {
             System.out.println("A guardar dados...");
             gerir.guardarDados();
         }));


        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            mostrarBanner();

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[SERVIDOR] Novo cliente ligado: " + socketCliente.getInetAddress());

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

    public static void avisarClientePedidoAEntregar(int codigoCliente) {
        ObjectOutputStream cliente = clientesLigados.get(codigoCliente);

        if (cliente != null) {
            try {
                synchronized (cliente) {
                    cliente.writeObject(new Mensagem("NOTIFICACAO", "PEDIDO A ENTREGAR"));
                    cliente.flush();
                    cliente.reset();
                }
            } catch (IOException e) {
                System.out.println("Erro ao avisar cliente #" + codigoCliente);
                clientesLigados.remove(codigoCliente);
            }
        }
    }

    public static void mostrarBanner() {
        System.out.println("""
    
     ██████╗ █████╗ ███╗   ██╗████████╗██╗███╗   ██╗ █████╗
    ██╔════╝██╔══██╗████╗  ██║╚══██╔══╝██║████╗  ██║██╔══██╗
    ██║     ███████║██╔██╗ ██║   ██║   ██║██╔██╗ ██║███████║
    ██║     ██╔══██║██║╚██╗██║   ██║   ██║██║╚██╗██║██╔══██║
    ╚██████╗██║  ██║██║ ╚████║   ██║   ██║██║ ╚████║██║  ██║
     ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝   ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝

              🍽️  SERVIDOR DA CANTINA INICIADO  🍽️
                   Porta: """ + PORTA + """
    """);
    }
}