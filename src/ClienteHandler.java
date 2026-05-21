import java.io.*;
import java.net.*;
import java.time.LocalDate;

public class ClienteHandler extends Thread {
    private final Socket socket;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;

    private Utilizador user = null;

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

                if (msg.getTipo().equalsIgnoreCase("LOGIN")) {
                    user = (Utilizador) msg.getDados();

                    saida.writeObject(new Mensagem("INFO", user.getNome() + " - Bem-vindo"));
                    saida.flush();

                    System.out.println(user.getTipo() + " | " + user.getCodigo() + " - LOGIN");

                }
                else if (msg.getTipo().equalsIgnoreCase("CRIAR_PEDIDO")) {
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

                        System.out.println(user.getTipo() + " | " + user.getCodigo() + " - LOGIN");

                        Servidor.atualizarMonitores();
                    }

                }
                else if (msg.getTipo().equalsIgnoreCase("SAIR")) {
                    if (user != null) {
                        System.out.println(user.getCodigo() + " - TERMINOU A SESSÃO");
                    }

                    saida.writeObject(new Mensagem("INFO", "Ligação terminada."));
                    saida.flush();
                    break;
                }
                else if (msg.getTipo().equalsIgnoreCase("ENTREGAR_PEDIDO")){

                    int numPedido = (int) msg.getDados();
                    boolean value = false;
                    for (Pedido pedido : Servidor.pedidos){
                        System.out.println("TESTE - " + pedido.getCliente().getCodigo());
                        if(pedido.getCliente().getCodigo() == numPedido && pedido.getData().equals(LocalDate.now()) && pedido.getEstado() == EstadoPedido.A_FAZER){
                            value = true;
                            pedido.entregarPedido();
                            break;
                        }
                    }

                    if(value) {
                        Servidor.atualizarMonitores();
                        System.out.println(numPedido + " | A ENTREGAR");
                        saida.writeObject(new Mensagem("INFO", "True"));
                        saida.flush();

                    } else {

                        saida.writeObject(new Mensagem("INFO", "[ERRO] Nenhum pedido pendente com esse número!"));
                        saida.flush();
                    }

                }
                else if (msg.getTipo().equalsIgnoreCase("PEDIDO_ENTREGUE")){

                    int numPedido = (int) msg.getDados();
                    for (Pedido pedido : Servidor.pedidos){
                        if(pedido.getCliente().getCodigo() == numPedido && pedido.getData().equals(LocalDate.now()) && pedido.getEstado() == EstadoPedido.A_ENTREGAR){
                            pedido.pedidoEntregue();
                            break;
                        }
                    }

                    Servidor.atualizarMonitores();

                    System.out.println("PEDIDO #" + numPedido + " | ENTREGUE");

                    saida.writeObject(new Mensagem("INFO", "Pedido entregue!"));
                    saida.flush();

                }
                else if (msg.getTipo().equalsIgnoreCase("PEDIDO_NAO_ENTREGUE")){

                    int numPedido = (int) msg.getDados();

                    for (Pedido pedido : Servidor.pedidos){
                        if(pedido.getCliente().getCodigo() == numPedido && pedido.getData().equals(LocalDate.now()) && pedido.getEstado() == EstadoPedido.A_ENTREGAR){
                            pedido.pedidoNaoEntregue();
                            break;
                        }
                    }

                    Servidor.atualizarMonitores();

                    System.out.println("PEDIDO #" + numPedido + " | NÃO ENTREGUE");

                    saida.writeObject(new Mensagem("INFO", "Pedido não entregue!"));
                    saida.flush();
                }
                else if (msg.getTipo().equalsIgnoreCase("VER_PEDIDOS")){
                    saida.writeObject(new Mensagem("INFO", Servidor.pedidos));
                    saida.flush();
                }

                else if(msg.getTipo().equalsIgnoreCase("VER PEDIDOS PENDENTES")){
                    if (user == null){
                        saida.writeObject((new Mensagem("ERRO", "ATENÇÃO: Primeiro faça login.")));
                        saida.flush();
                    }else if(user.getTipo() != TipoUtilizador.FUNCIONARIO){
                        saida.writeObject(new Mensagem("ERRO", "ATENÇÃO: Sem permissões para esta operação."));
                        saida.flush();
                    }else{
                        java.util.ArrayList<Pedido> pendentes = new java.util.ArrayList<>();
                        for (Pedido pedido : Servidor.pedidos){
                            if (pedido.getData().equals(LocalDate.now()) && pedido.getEstado() == EstadoPedido.A_FAZER ){
                                pendentes.add(pedido);
                            }
                        }
                        saida.writeObject(new Mensagem("LISTA_PEDIDOS_PENDENTES", pendentes));
                        saida.flush();

                        System.out.println(user.getCodigo() + " | FUNCIONÁRIO CONSULTOU PEDIDOS PENDENTES");
                    }
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