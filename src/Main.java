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
                            char[] senhaChars = console.readPassword();
                            String senha = new String(senhaChars);

                            user = new Utilizador(codigo, nomeCliente, senha, TipoUtilizador.CLIENTE);
                        } catch (Exception e) {
                            System.out.println("[ERRO] Dados introduzido inválidos");
                        }

                        // ENVIOS PARA SERVIDOR

                        saida.writeObject(new Mensagem("REGISTO", user));
                        saida.flush();

                        // MENSAGEM VINDA DO SERVIDOR

                        Mensagem resposta = (Mensagem) entrada.readObject();
                        System.out.println(resposta.getDados());

                        // TITULO DO TERMINAL

                        System.out.print("\033]0;CANTINA | CLIENTE - " + user.getNome() + "\007");
                        System.out.flush();

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
                            char[] senhaChars = console.readPassword();
                            String senha = new String(senhaChars);

                            user = new Utilizador(codigo, nomeCliente, senha, TipoUtilizador.FUNCIONARIO);
                        } catch (Exception e) {
                            System.out.println("[ERRO] Dados introduzido inválidos");
                        }

                        // ENVIOS PARA SERVIDOR

                        saida.writeObject(new Mensagem("REGISTO", user));
                        saida.flush();

                        // MENSAGEM VINDA DO SERVIDOR

                        resposta = (Mensagem) entrada.readObject();
                        System.out.println(resposta.getDados());

                        // TITULO DO TERMINAL

                        System.out.print("\033]0;CANTINA | " + user.getTipo().name() + " - " + user.getNome() + "\007");
                        System.out.flush();
                    case 3:
                        int codigo = -1;
                        try {
                            /**
                             * EXEMPLO DE TESTE SEM REGISTO - LOGIN FALSO
                             */
                            System.out.println(" --- LOGIN --- ");
                            while (Servidor.gerir.pesquisarCliente(codigo) == null){
                                System.out.print("Número do cliente: ");
                                codigo = input.nextInt();
                            }
                            input.nextLine();
                            System.out.print("Senha: ");
                            char[] senhaChars = console.readPassword();
                            String senha = new String(senhaChars);
                            ArrayList<Object> dados = new ArrayList<>();
                            dados.add(codigo);
                            dados.add(senha);
                        } catch (Exception e){
                            System.out.println("[ERRO] Dados introduzido inválidos");
                        }

                        // ENVIOS PARA SERVIDOR

                        saida.writeObject(new Mensagem("LOGIN", user));
                        saida.flush();

                        // MENSAGEM VINDA DO SERVIDOR

                        resposta = (Mensagem) entrada.readObject();
                        System.out.println(resposta.getDados());

                        if(resposta.getTipo().equalsIgnoreCase("TRUE")){
                            // TITULO DO TERMINAL

                            user = Servidor.gerir.pesquisarCliente(codigo);

                            System.out.print("\033]0;CANTINA | CLIENTE - " + user.getNome() + "\007");
                            System.out.flush();
                        }
                        break;
                    case 0:
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
                    System.out.println(" 1 - Adicionar Item");
                    System.out.println(" 2 - Ver Items por Tipo");
                    System.out.println(" 3 - Criar Ementa");
                    System.out.println(" 4 - Ver Ementa de Hoje");
                    System.out.println(" 5 - Ver Ementa Geral");
                    System.out.println(" 6 - Ver Pedidos Pendentes");
                    System.out.println(" 7 - Entregar Pedido");
                    System.out.println(" 8 - Ver Pedidos Geral");
                    System.out.println(" 9 - Ver Utilizadores Geral");

                    System.out.println(" 0 - Sair");

                    System.out.print("\nInsira a ação que deseja: ");
                    n = input.nextInt();

                    switch (n){
                        case 1:
                            /**
                             * EXEMPLO DE TESTE DE CRIAÇÃO DE PEDIDO
                             */
                            Pedido pedidoCliente = null;
                            System.out.print("Número do pedido: ");
                            int numPedido = input.nextInt();

                            saida.writeObject(new Mensagem("ENTREGAR_PEDIDO", numPedido));
                            saida.flush();

                            Mensagem value = (Mensagem) entrada.readObject();
                            if(value.getDados().equals("True")){
                                System.out.print("Pedido entregue (S/N)?");
                                input.nextLine();
                                String estado = input.nextLine();
                                switch (estado.toLowerCase()){
                                    case "s":
                                        saida.writeObject(new Mensagem("PEDIDO_ENTREGUE", numPedido));
                                        saida.flush();

                                        Mensagem resposta = (Mensagem) entrada.readObject();
                                        System.out.println(resposta.getDados());
                                        break;
                                    case "n":
                                        saida.writeObject(new Mensagem("PEDIDO_NAO_ENTREGUE", numPedido));
                                        saida.flush();

                                        resposta = (Mensagem) entrada.readObject();
                                        System.out.println(resposta.getDados());
                                }
                            } else {
                                System.out.println(value.getDados());
                            }
                            break;
                        case 2:
                            System.out.println(" --- PEDIDOS CRIADOS --- ");
                            saida.writeObject(new Mensagem("VER_PEDIDOS", null));
                            saida.flush();

                            Mensagem resposta = (Mensagem) entrada.readObject();
                            ArrayList<Pedido> pedidosGeral = (ArrayList<Pedido>) resposta.getDados();

                            for (Pedido pedido : pedidosGeral){
                                System.out.println(pedido);
                            }

                            break;
                        case 0:
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
                                    TipoItem.Entrada
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

                            saida.writeObject(new Mensagem("CRIAR_PEDIDO", pedido));
                            saida.flush();

                            Mensagem resposta = (Mensagem) entrada.readObject();
                            System.out.println(resposta.getDados());
                            break;
                        case 0:
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
}