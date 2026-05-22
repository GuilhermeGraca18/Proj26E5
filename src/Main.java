import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 5001;

        try (Socket socket = new Socket(host, porta)) {

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            Scanner input = new Scanner(System.in);
            Console console = System.console();

            int n = -1;
            Utilizador user = null;
            
            // Cantina a ser gerida
            GerirCantina gerir = new GerirCantina();

            while (n != 0 && user == null) {

                System.out.println(" --- MENU LOGIN/REGISTO | CANTINA ---");
                System.out.println(" 1 - Registo como Cliente");
                System.out.println(" 2 - Registo como Funcionário");
                System.out.println(" 3 - Login");
                System.out.println(" 0 - Sair");

                System.out.print("\nInsira a ação que deseja: ");
                n = input.nextInt();
                input.nextLine();

                switch (n) {
                    case 1:
                        try {
                        	
                            System.out.println(" --- REGISTO DO CLIENTE --- ");
                            System.out.print("Número do cliente: ");
                            int codigo = input.nextInt();
                            input.nextLine();
                            System.out.print("Nome do cliente: ");
                            String nomeCliente = input.nextLine();
                            System.out.print("Senha: ");
                            String senha = input.nextLine();

                            user = new Utilizador(codigo, nomeCliente, senha, TipoUtilizador.CLIENTE);
                            
                            // Registra um cliente na lista de usuarios da cantina.
                            gerir.registarUser(user);
                            
                        } catch (Exception e) {
                            System.out.println("[ERRO] Dados introduzido inválidos");
                            System.out.println(e);
                        }

                        // ENVIOS PARA SERVIDOR

                        saida.reset();
                        saida.writeObject(new Mensagem("REGISTO", user));
                        saida.flush();

                        user = null; // para necessitar login após o registo!

                        // MENSAGEM VINDA DO SERVIDOR

                        Mensagem resposta = (Mensagem) entrada.readObject();
                        System.out.println(resposta.getDados());

                        break;
                    case 2:
                        try {
                        	
                            System.out.println(" --- REGISTO DO FUNCIONÁRIO --- ");
                            System.out.print("Número do Funcionário: ");
                            int codigo = input.nextInt();
                            input.nextLine();
                            System.out.print("Nome do Funcionário: ");
                            String nomeCliente = input.nextLine();
                            System.out.print("Senha: ");
                            String senha = input.nextLine();

                            user = new Utilizador(codigo, nomeCliente, senha, TipoUtilizador.FUNCIONARIO);
                            
                            // Registra um funcionário na lista de usuarios da cantina.
                            gerir.registarUser(user);
                            
                        } catch (Exception e) {
                            System.out.println("[ERRO] Dados introduzido inválidos");
                        }

                        // ENVIOS PARA SERVIDOR

                        saida.reset();
                        saida.writeObject(new Mensagem("REGISTO", user));
                        saida.flush();

                        user = null; // para necessitar login após o registo!

                        // MENSAGEM VINDA DO SERVIDOR

                        resposta = (Mensagem) entrada.readObject();
                        System.out.println(resposta.getDados());
                        break;
                    case 3:
                        int codigo = -1;
                        ArrayList<Object> dados = new ArrayList<>();
                        try {

                            System.out.println(" --- LOGIN --- ");
                            System.out.print("Número do Utilizador: ");
                            codigo = input.nextInt();
                            input.nextLine();
                            System.out.print("Senha: ");

                            // PARA ESCONDER A PASS
                            /*char[] senhaChars = console.readPassword();
                            String senha = new String(senhaChars);*/

                            String senha = input.nextLine();
                            dados.add(codigo);
                            dados.add(senha);
                        } catch (Exception e){
                            System.out.println("[ERRO] Dados introduzido inválidos");
                        }

                        // ENVIOS PARA SERVIDOR

                        saida.reset();
                        saida.writeObject(new Mensagem("LOGIN", dados));
                        saida.flush();

                        // MENSAGEM VINDA DO SERVIDOR

                        resposta = (Mensagem) entrada.readObject();

                        if(resposta.getTipo().equalsIgnoreCase("TRUE")){

                            ArrayList<Object> dadosServidor = (ArrayList<Object>) resposta.getDados();

                            user = (Utilizador) dadosServidor.get(1);

                            System.out.println((String) dadosServidor.get(0));
                        } else {
                            System.out.println(resposta.getDados());
                        }
                        break;
                    case 0:
                        saida.reset();
                        saida.writeObject(new Mensagem("SAIR", null));
                        saida.flush();
                        System.out.println("A sair...");
                        break;
                    default:
                        System.out.println("[ERRO] Ação inválida");
                        break;
                }
            }

            if (user.getTipo().equals(TipoUtilizador.FUNCIONARIO)) {

                while (n != 0) {
                    System.out.println(" --- ÁREA DO FUNCIONÁRIO | CANTINA ---");
                    System.out.println(" 1 - Entregar Pedido Pendente");
                    System.out.println(" 2 - Adicionar Item");
                    System.out.println(" 3 - Ver Items por Tipo");
                    System.out.println(" 4 - Criar Ementa");
                    System.out.println(" 5 - Ver Ementa de Hoje");
                    System.out.println(" 6 - Ver Ementa Geral");
                    System.out.println(" 7 - Ver Pedidos Pendentes");
                    System.out.println(" 8 - Ver Pedidos Geral");
                    System.out.println(" 9 - Ver Utilizadores Geral");
                    System.out.println(" 10 - Criar Relatório");

                    System.out.println(" 0 - Sair");

                    try {
                        System.out.print("\nInsira a ação que deseja: ");
                        n = input.nextInt();
                    } catch (Exception e){
                        System.out.println("[ERRO] Tipo de dados inserido inválido");
                    }


                    switch (n){
                        case 1: // ENTREGAR PEDIDO
                            Pedido pedidoCliente = null;
                            System.out.println(" === ENTREGAR PEDIDO PENDENTE === ");
                            System.out.print("Número do pedido: ");
                            int numPedido = input.nextInt();

                            saida.reset();
                            saida.writeObject(new Mensagem("ENTREGAR_PEDIDO", numPedido));
                            saida.flush();

                            Mensagem value = (Mensagem) entrada.readObject();
                            if(value.getDados().equals("True")){
                                System.out.print("Pedido entregue (S/N)? ");
                                input.nextLine();
                                String estado = input.nextLine();
                                switch (estado.toLowerCase()){
                                    case "s":
                                        saida.reset();
                                        saida.writeObject(new Mensagem("PEDIDO_ENTREGUE", numPedido));
                                        saida.flush();

                                        Mensagem resposta = (Mensagem) entrada.readObject();
                                        System.out.println(resposta.getDados());
                                        break;
                                    case "n":
                                        saida.reset();
                                        saida.writeObject(new Mensagem("PEDIDO_NAO_ENTREGUE", numPedido));
                                        saida.flush();

                                        resposta = (Mensagem) entrada.readObject();
                                        System.out.println(resposta.getDados());
                                }
                            } else {
                                System.out.println(value.getDados());
                            }
                            break;
                        case 2: // ADICIONAR ITEM

                            try {
                                System.out.println(" === CRIAR NOVO ITEM === ");
                                System.out.print("Código do Item: ");
                                int codigo = input.nextInt();
                                System.out.print("Nome: ");
                                input.nextLine();
                                String nome = input.nextLine();
                                System.out.print("Descrição: ");
                                String descricao = input.nextLine();
                                System.out.print("Preço: ");
                                double preco = input.nextDouble();
                                input.nextLine();

                                // MENU COM SETAS COM GOOGLE LANTERNA - EXPLICA ISTO À PROFESSORA, SE NAO DER PARA USAR A FUNÇAO escolhertipo() por de forma convencional
                                // PARA JA NO PRIMEIRO SPRINT POIS DEPOIS TENCIONAMOS FAZER A INTERFACE GRAFICA
                                TipoItem tipo = escolherTipo();

                                Item novoItem = new Item(
                                        codigo,
                                        nome,
                                        descricao,
                                        preco,
                                        tipo
                                );

                                saida.reset();
                                saida.writeObject(new Mensagem("ADICIONAR_ITEM", novoItem));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                System.out.println(resposta.getDados());

                            } catch (Exception e) {
                                System.out.println("[ERRO] Tipo de dados inserido inválido");
                                input.nextLine();
                            }
                            break;
                        case 3: // VER LISTA DE ITEM
                            try {
                                System.out.println(" === LISTA ITEMS === ");
                                saida.reset();
                                saida.writeObject(new Mensagem("VER_LISTA_ITEMS", null));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                ArrayList<Item> listaItems = (ArrayList<Item>) resposta.getDados();

                                for (Item item : listaItems){
                                    System.out.println(item.toStringDetalhado());
                                }
                            } catch (Exception e){
                                System.out.println("[ERRO] O Programa não conseguiu ler a lista de Items!");
                            }

                            break;
                        case 8: // VER PEDIDOS GERAL
                            try {
                                System.out.println(" === PEDIDOS CRIADOS === ");
                                saida.reset();
                                saida.writeObject(new Mensagem("VER_PEDIDOS", null));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                ArrayList<Pedido> pedidosGeral = (ArrayList<Pedido>) resposta.getDados();

                                for (Pedido pedido : pedidosGeral){
                                    System.out.println(pedido);
                                }
                            } catch (Exception e){
                                System.out.println("[ERRO] O Programa não conseguiu ler a lista de Pedidos!");
                            }
                            
                            break;
                            
                        case 9: // CONSULTAR UTILIZADORES
                        	
                        	gerir.consultarUtilizadores();
                        	
                        	break;
                        	
                        case 10: // CRIAR RELATORIO
                        	
                        	gerir.criarRelatorio();
                        	
                        	break;
                        	
                        case 0:
                            saida.reset();
                            saida.writeObject(new Mensagem("SAIR", null));
                            saida.flush();
                            System.out.println("A sair...");
                            break;
                        default:
                            System.out.println("[ERRO] Ação inválida");
                            break;

                    }

                }

            } else if (user.getTipo().equals(TipoUtilizador.CLIENTE)) {

                while ( n != 0 ) {
                    System.out.println(" --- ÁREA DO CLIENTE | CANTINA ---");
                    System.out.println(" 1 - Criar Pedido");
                    System.out.println(" 0 - Sair");

                    /*System.out.println(" 1 - Ver Ementa de Hoje");
                    System.out.println(" 2 - Criar Pedido");
                    System.out.println(" 3 - Ver estado do meu pedido");
                    System.out.println(" 4 - Ver pedidos anteriores");
                    System.out.println(" 0 - Sair");*/

                    System.out.print("\nInsira a ação que deseja: ");
                    n = input.nextInt();

                    switch (n) {
                        case 1:

                            Item bebida = new Item(
                                    111,
                                    "Cola",
                                    "",
                                    2,
                                    TipoItem.Bebida
                            );

                            Item entradaPedido = new Item(
                                    111,
                                    "Sopa",
                                    "Com feijão",
                                    2,
                                    TipoItem.Entrada
                            );

                            Item prato = new Item(
                                    123,
                                    "Hambúrguer",
                                    "Com batatas",
                                    5,
                                    TipoItem.Prato
                            );

                            Pedido pedido = new Pedido(user, "NOTAS");
                            
                            // Adicionar um pedido a lista de pedidos.
                            gerir.criarPedidos(pedido);

                            pedido.adicionarItems(bebida);
                            pedido.adicionarItems(entradaPedido);
                            pedido.adicionarItems(prato);

                            saida.reset();
                            saida.writeObject(new Mensagem("CRIAR_PEDIDO", pedido));
                            saida.flush();

                            Mensagem resposta = (Mensagem) entrada.readObject();
                            System.out.println(resposta.getDados());
                            break;
                        case 0:
                            saida.reset();
                            saida.writeObject(new Mensagem("SAIR", null));
                            saida.flush();
                            System.out.println("A sair...");
                            break;
                        default:
                            System.out.println("[ERRO] Ação inválida");
                            break;

                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static TipoItem escolherTipo() throws Exception{
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Screen screen = terminalFactory.createScreen();
        screen.startScreen();

        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);

        BasicWindow window = new BasicWindow("Escolher Tipo");

        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        ComboBox<TipoItem> comboBox = new ComboBox<>(TipoItem.values());

        final TipoItem[] escolhido = new TipoItem[1];

        Button confirmar = new Button("Confirmar", () -> {
            escolhido[0] = comboBox.getSelectedItem();
            window.close();
        });

        panel.addComponent(new Label("Escolha o tipo do item:"));
        panel.addComponent(comboBox);
        panel.addComponent(confirmar);

        window.setComponent(panel);

        gui.addWindowAndWait(window);

        screen.stopScreen();

        return escolhido[0];
    }
}