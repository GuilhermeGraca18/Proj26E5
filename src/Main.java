import jdk.jshell.execution.Util;

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
                System.out.println(" 1 - Login como Cliente");
                System.out.println(" 2 - Login como Funcionário");
                System.out.println(" 3 - Registar Cliente");
                System.out.println(" 0 - Sair");

                System.out.print("\nInsira a ação que deseja: ");
                n = input.nextInt();
                input.nextLine();

                switch (n) {
                    case 1:
                        System.out.print("Número do cliente: ");
                        int codigo = input.nextInt();
                        input.nextLine();
                        System.out.print("Nome do cliente: ");
                        String nomeCliente = input.nextLine();
                        System.out.print("Senha: ");
                        char[] senhaChars = console.readPassword();
                        String senha = new String(senhaChars);
                        user = new Cliente(codigo, nomeCliente, senha);

                        // ENVIOS PARA SERVIDOR

                        user = new Cliente(codigo, nomeCliente, senha);

                        saida.writeObject(new Mensagem("LOGIN_CLIENTE", user));
                        saida.flush();

                        // MENSAGEM VINDA DO SERVIDOR

                        Mensagem resposta = (Mensagem) entrada.readObject();
                        System.out.println(resposta.getDados());

                        // Exemplo temporário
                        break;

                    case 2:
                        saida.writeObject(new Mensagem("LOGIN_FUNCIONARIO", null));
                        saida.flush();
                        // Exemplo temporário
                        break;

                    case 3:
                        saida.writeObject(new Mensagem("REGISTAR CLIENTE", null));
                        saida.flush();
                        // Exemplo temporário
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

            if (user.getClass().getName().equalsIgnoreCase("Utilizador")) {

                System.out.println(" --- ÁREA DO FUNCIONÁRIO | CANTINA ---");
                /*System.out.println(" 1 - Adicionar Item");
                System.out.println(" 2 - Adicionar Bebida");
                System.out.println(" 3 - Ver Items por Tipo");
                System.out.println(" 4 - Criar Ementa");
                System.out.println(" 5 - Ver Ementa de Hoje");
                System.out.println(" 6 - Ver Ementa Geral");
                System.out.println(" 7 - Ver Pedidos pendentes");
                System.out.println(" 8 - Ver Pedidos Geral");
                System.out.println(" 9 - Ver Funcionários Registados");
                System.out.println(" 10 - Ver Clientes Registados");
                System.out.println(" 0 - Sair");*/

            } else if (user.getClass().getName().equalsIgnoreCase("Cliente")) {

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
                            Bebida bebida = new Bebida("Água", 2);

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

                            Pedido pedido = new Pedido(user.getCodigo(), bebida, "NOTAS");

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