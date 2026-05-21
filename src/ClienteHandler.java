import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
                    ArrayList<Object> dados = (ArrayList<Object>) msg.getDados();

                    int codigoUser = (int) dados.get(0);
                    String senha = (String) dados.get(1);
                    user = Servidor.gerir.pesquisarCliente(codigoUser);
                    if(user != null) {

                        if (Servidor.gerir.verificarPassword(user, senha)) {

                            ArrayList<Object> dadosCliente = new ArrayList<>();
                            String resposta = user.getNome() + " - Bem-vindo";
                            dadosCliente.add(resposta);
                            dadosCliente.add(user);
                            saida.writeObject(new Mensagem("TRUE", dadosCliente));
                            saida.flush();

                            System.out.println(user.getTipo() + " | " + user.getCodigo() + " - LOGIN");
                        } else {

                            user = null;
                            saida.writeObject(new Mensagem("FALSE", "Login Falhou - Senha errada!"));
                            saida.flush();

                            System.out.println(socket.getInetAddress() + " - LOGIN INVÁLIDO");
                        }
                    } else {
                        saida.writeObject(new Mensagem("FALSE", "Login Falhou - Código errado!"));
                        saida.flush();
                    }



                }
                else if (msg.getTipo().equalsIgnoreCase("REGISTO")){
                    user = (Utilizador) msg.getDados();

                    Servidor.gerir.getUtilizadores().add(user);

                    saida.writeObject(new Mensagem("INFO", user.getNome() + " - Bem-vindo"));
                    saida.flush();

                    System.out.println(user.getTipo() + " | " + user.getCodigo() + " - REGISTADO");

                }
                else if (msg.getTipo().equalsIgnoreCase("CRIAR_PEDIDO")) {
                    if (user == null) {
                        saida.writeObject(new Mensagem("ERRO", "[ATENÇÃO] Primeiro faça login!"));
                        saida.flush();
                    } else {

                        Pedido pedido = (Pedido) msg.getDados();

                        synchronized (Servidor.gerir.getPedidos()) {
                            Servidor.gerir.getPedidos().add(pedido);
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
                    for (Pedido pedido : Servidor.gerir.getPedidos()){
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
                    for (Pedido pedido : Servidor.gerir.getPedidos()){
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

                    for (Pedido pedido : Servidor.gerir.getPedidos()){
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
                    saida.writeObject(new Mensagem("INFO", Servidor.gerir.getPedidos()));
                    saida.flush();
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