import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

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
                    user = Servidor.gerir.pesquisarUtilizador(codigoUser);
                    if(user != null) {

                        if (Servidor.gerir.verificarPassword(user, senha)) {

                            ArrayList<Object> dadosCliente = new ArrayList<>();
                            String resposta = user.getNome() + " - Bem-vindo";
                            dadosCliente.add(resposta);
                            dadosCliente.add(user);
                            saida.writeObject(new Mensagem("TRUE", dadosCliente));
                            saida.flush();

                            System.out.println("[" + msg.getTipo() + "] " + user.getTipo() + " | " + user.getCodigo() + "");
                        } else {

                            user = null;
                            saida.writeObject(new Mensagem("FALSE", "Login Falhou - Senha errada!"));
                            saida.flush();

                            System.out.println("[" + msg.getTipo() + "] " + socket.getInetAddress() + " - LOGIN INVÁLIDO");
                        }
                    } else {
                        saida.writeObject(new Mensagem("FALSE", "Login Falhou - Código errado!"));
                        saida.flush();
                    }
                }
                else if (msg.getTipo().equalsIgnoreCase("REGISTO")){
                    user = (Utilizador) msg.getDados();

                    if(Servidor.gerir.pesquisarUtilizador(user.getCodigo()) == null){
                        Servidor.gerir.registarUser(user);
                        Servidor.gerir.guardarDados();

                        saida.writeObject(new Mensagem("INFO", user.getNome() + " - REGISTADO"));
                        saida.flush();

                        System.out.println( "[" + msg.getTipo() + "] " + user.getTipo() + " | " + user.getCodigo() + " - UTILIZADOR REGISTADO COM SUCESSO");

                    } else {
                        saida.writeObject(new Mensagem("INFO", "Esse código de utilizador já foi registado!"));
                        saida.flush();
                    }



                }
                else if (msg.getTipo().equalsIgnoreCase("VER_CLIENTES")) {

                    saida.reset();
                    saida.writeObject(new Mensagem("INFO", Servidor.gerir.getUtilizadoresClientes()));
                    saida.flush();

                }
                else if (msg.getTipo().equalsIgnoreCase("VER_FUNCIONARIOS")) {

                    saida.reset();
                    saida.writeObject(new Mensagem("INFO", Servidor.gerir.getUtilizadoresFuncionarios()));
                    saida.flush();

                }
                else if (msg.getTipo().equalsIgnoreCase("ADICIONAR_ITEM")) {
                    Item item = (Item) msg.getDados();

                    if(Servidor.gerir.pesquisarItem(item.getCodigo()) == null){
                        Servidor.gerir.registarItem(item);
                        Servidor.gerir.guardarDados();

                        saida.writeObject(new Mensagem("INFO", item.getTipo().name().toUpperCase() + ": ADICIONADO À LISTA DE ITEMS!"));
                        saida.flush();

                        System.out.println( "[" + msg.getTipo() + "] " + item.getTipo().name().toUpperCase() + " | " + item.getCodigo() + " - ADICIONADO");
                    } else {
                        saida.writeObject(new Mensagem("INFO", "Esse item já existe na lista!"));
                        saida.flush();
                    }
                }
                else if (msg.getTipo().equalsIgnoreCase("VER_LISTA_ITEMS")) {
                    saida.reset();
                    saida.writeObject(new Mensagem("INFO", Servidor.gerir.getListaItems()));
                    saida.flush();

                }
                else if (msg.getTipo().equalsIgnoreCase("ELIMINAR_ITEM")) {
                    int codigoItem = (int) msg.getDados();

                    if(Servidor.gerir.pesquisarItem(codigoItem) != null){
                        Servidor.gerir.eliminarItem(codigoItem);

                        saida.reset();
                        saida.writeObject(new Mensagem("INFO", "[SUCESSO] ITEM " + codigoItem + " eliminado!" ));
                        System.out.println( "[" + msg.getTipo() + "] " + codigoItem + " | - ELIMINADO");
                    } else {
                        saida.reset();
                        saida.writeObject(new Mensagem("INFO", "[ERRO] ITEM " + codigoItem + " não existe!" ));
                    }
                    saida.flush();

                }
                else if (msg.getTipo().equalsIgnoreCase("CRIAR_EMENTA")) {
                    LocalDate data = (LocalDate) msg.getDados();

                    if(Servidor.gerir.pesquisarEmenta(data) == null){
                        Servidor.gerir.criarEmenta(data);

                        saida.reset();
                        saida.writeObject(new Mensagem("TRUE", "[SUCESSO] EMENTA CRIADA | DIA: " + data ));
                        System.out.println( "[" + msg.getTipo() + "] " + data + " | - CRIADA");
                    } else {
                        saida.reset();
                        saida.writeObject(new Mensagem("FALSE", "[ERRO] EMENTA JÀ CRIADA E FECHADA | DIA: " + data ));
                        System.out.println( "[" + msg.getTipo() + "] " + data + " | - JÀ CRIADA");
                    }

                    saida.flush();
                }
                else if (msg.getTipo().equalsIgnoreCase("ADICIONAR_ITEM_EMENTA")) {
                    ArrayList<Object> dados = (ArrayList<Object>) msg.getDados();

                    int codigoItem = (int) dados.get(0);
                    int stock = (int) dados.get(1);
                    LocalDate dataEmenta = (LocalDate) dados.get(2);

                    if(Servidor.gerir.pesquisarItem(codigoItem) != null){

                        if(Servidor.gerir.pesquisarItemEmenta(dataEmenta, codigoItem) == null){

                            Servidor.gerir.adicionarItemEmenta(dataEmenta, codigoItem, stock);

                            saida.reset();
                            saida.writeObject(new Mensagem("INFO", "[SUCESSO] ITEM (" + codigoItem + ") ADICIONADO À EMENTA"));
                            System.out.println( "[" + msg.getTipo() + "] " + codigoItem + " | - ADICIONADO À EMENTA - DIA: " + dataEmenta);

                        } else {
                            saida.reset();
                            saida.writeObject(new Mensagem("INFO", "[ERRO] ITEM (" + codigoItem + ") JÀ FOI ADICIONADO À EMENTA"));
                        }

                    } else {
                        saida.reset();
                        saida.writeObject(new Mensagem("INFO", "[ERRO] ITEM (" + codigoItem + ") NÂO EXISTE NA LISTA"));
                    }
                    saida.flush();
                }
                else if (msg.getTipo().equalsIgnoreCase("VER_EMENTAS")) {
                	
                    ArrayList<Ementa> ementas = Servidor.gerir.getEmentas();

                    saida.reset();
                    saida.writeObject(new Mensagem("INFO", ementas));
                    saida.flush();

                }
                else if (msg.getTipo().equalsIgnoreCase("VER_EMENTA_DIA")) {


                    Ementa ementaDia = Servidor.gerir.pesquisarEmentaHoje();

                    if(ementaDia != null){
                    	
                        saida.reset();
                        saida.writeObject(new Mensagem("INFO", ementaDia));
                        saida.flush();
                        
                    } else {
                    	
                        saida.reset();
                        saida.writeObject(new Mensagem("ERRO", "[INFO] Nenhum ementa criada para o dia de hoje! ( " + LocalDate.now() + ")"));
                        saida.flush();
                    }

                }
                else if (msg.getTipo().equalsIgnoreCase("CRIAR_PEDIDO")) {
                    if(Servidor.gerir.pesquisarEmenta(LocalDate.now()) == null){
                        saida.reset();
                        saida.writeObject(new Mensagem("ERRO", "[ATENÇÃO] Ainda não foi criada nenhuma ementa para o dia de hoje!"));
                        saida.flush();
                    } else {
                        if (user == null) {
                            saida.reset();
                            saida.writeObject(new Mensagem("ERRO", "[ATENÇÃO] Primeiro faça login!"));
                            saida.flush();
                        } else {
                            String notas = (String) msg.getDados();

                                Pedido pedido = new Pedido(user, notas);

                                if (Servidor.gerir.pesquisarPedidoDia(user)) {

                                    synchronized (Servidor.gerir.getPedidos()) {
                                        Servidor.gerir.criarPedidos(pedido);
                                        Servidor.gerir.guardarDados();
                                    }

                                    saida.reset();
                                    saida.writeObject(new Mensagem("INFO", "Pedido criado com sucesso."));
                                    saida.flush();

                                    Servidor.atualizarMonitores();

                                } else {
                                    saida.reset();
                                    saida.writeObject(new Mensagem("ERRO", "Pedido de hoje já foi criado!"));
                                    saida.flush();
                                }
                            }
                        }

                }
                else if (msg.getTipo().equalsIgnoreCase("ADICIONAR_ITEM_PEDIDO")) {

                    if (user == null) {
                        saida.reset();
                        saida.writeObject(new Mensagem("ERRO", "[ATENÇÃO] Primeiro faça login!"));
                        saida.flush();
                        return;
                    }

                    int nitem = (int) msg.getDados();

                    boolean value = Servidor.gerir.adicionarItemsPedido(user, nitem);

                    if (value) {
                        saida.reset();
                        saida.writeObject(new Mensagem("INFO", "[SUCESSO] ITEM ADICIONADO AO PEDIDO! VALOR DO PEDIDO ATUALMENTE: " + Servidor.gerir.pesquisarPedidoPendente(user).getPreçoTotalAtual() + "€"));
                        saida.flush();

                        Servidor.gerir.guardarDados();
                        Servidor.atualizarMonitores();

                    } else {

                        saida.reset();
                        saida.writeObject(new Mensagem("ERRO", "[ERRO] ITEM FORA DE STOCK OU NÃO ESTÁ PRESENTE NA EMENTA!"));
                        saida.flush();
                    }
                }
                else if (msg.getTipo().equalsIgnoreCase("RELATORIO_VENDAS"))
                {
                    saida.reset();
                    saida.writeObject(new Mensagem("INFO", Servidor.gerir.criarRelatorio()));
                    saida.flush();

                    System.out.println("[RELATORIO_VENDAS] Relatório enviado ao administrador.");
                }
                else if (msg.getTipo().equalsIgnoreCase("SAIR")) {
                    if (user != null) {
                        System.out.println( "[" + msg.getTipo() + "] " + user.getCodigo() + " - TERMINOU A SESSÃO");
                    }

                    saida.writeObject(new Mensagem("INFO", "Ligação terminada."));
                    saida.flush();
                    break;
                }
                else if (msg.getTipo().equalsIgnoreCase("ENTREGAR_PEDIDO")){

                    int numPedido = (int) msg.getDados();
                    boolean value = false;
                    for (Pedido pedido : Servidor.gerir.getPedidos()){
                        if(pedido.getCliente().getCodigo() == numPedido && pedido.getData().equals(LocalDate.now()) && pedido.getEstado() == EstadoPedido.A_FAZER){
                            value = true;
                            pedido.entregarPedido();
                            Servidor.gerir.guardarDados();
                            break;
                        }
                    }

                    if(value) {
                        Servidor.atualizarMonitores();
                        System.out.println("PEDIDO #" + numPedido + " | A ENTREGAR");
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
                            Servidor.gerir.guardarDados();
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
                            Servidor.gerir.guardarDados();
                            break;
                        }
                    }

                    Servidor.atualizarMonitores();

                    System.out.println("PEDIDO #" + numPedido + " | NÃO ENTREGUE");

                    saida.reset();
                    saida.writeObject(new Mensagem("INFO", "Pedido não entregue!"));
                    saida.flush();
                }
                else if (msg.getTipo().equalsIgnoreCase("VER_ESTADO_PEDIDO")){

                    Pedido pedido = Servidor.gerir.pesquisarPedidoPendente(user);

                    if(pedido != null){

                        saida.reset();
                        saida.writeObject(new Mensagem("INFO", "[PEDIDO # " + pedido.getCliente().getCodigo() + "] Estado: " + pedido.getEstado().name()));
                        saida.flush();

                    } else {

                        saida.reset();
                        saida.writeObject(new Mensagem("INFO", "Não existe nenhum pedido pendente hoje!"));
                        saida.flush();
                    }
                }
                else if (msg.getTipo().equalsIgnoreCase("VER_PEDIDOS")){
                	
                    saida.reset();
                    saida.writeObject(new Mensagem("INFO", Servidor.gerir.getPedidos()));
                    saida.flush();
                    
                }
                else if (msg.getTipo().equalsIgnoreCase("HISTORICO_PEDIDOS")){

                    ArrayList<Pedido> historico = Servidor.gerir.getHistoricoPedidos(user);

                    saida.reset();
                    saida.writeObject(new Mensagem("INFO", historico));
                    saida.flush();

                    System.out.println("[HISTORICO_PEDIDOS] Enviado ao utilizador: " + user.getCodigo());
                    
                }
                else if(msg.getTipo().equalsIgnoreCase("VER PEDIDOS PENDENTES")){
                    if (user == null){
                        saida.writeObject((new Mensagem("ERRO", "ATENÇÃO: Primeiro faça login.")));
                        saida.flush();
                    }else if(user.getTipo() != TipoUtilizador.FUNCIONARIO){
                        saida.writeObject(new Mensagem("ERRO", "ATENÇÃO: Sem permissões para esta operação."));
                        saida.flush();
                    }else{
                        ArrayList<Pedido> pendentes = new ArrayList<>();
                        for (Pedido pedido : Servidor.gerir.getPedidos()){
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
            /*System.out.println("[CONSOLE ERROR] - " + e);*/
        } finally {
            synchronized (Servidor.monitores) {
                Servidor.monitores.remove(saida);
            }
        }
    }
}