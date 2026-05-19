import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 5001;

        try (Socket socket = new Socket(host, porta)) {

            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            Scanner input = new Scanner(System.in);

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

                        String nomeCliente = input.nextLine();
                        saida.println(nomeCliente + " | LOGIN_CLIENTE");
                        System.out.println(entrada.readLine());
                        // Exemplo temporário
                        break;

                    case 2:
                        String nomeFunc = input.nextLine();
                        saida.println(nomeFunc + " | LOGIN_FUNCIONARIO");
                        System.out.println(entrada.readLine());
                        // Exemplo temporário
                        break;

                    case 3:
                        saida.println("REGISTAR_CLIENTE");
                        System.out.println(entrada.readLine());
                        // Exemplo temporário
                        break;
                    case 0:
                        saida.println("SAIR");
                        System.out.println("A sair...");
                        break;

                    default:
                        System.out.println("[ERRO] Ação inválida");
                        break;
                }
            }

            if (user instanceof Utilizador) {

                System.out.println(" --- ÁREA DO FUNCIONÁRIO | CANTINA ---");
                System.out.println(" 1 - Adicionar Item");
                System.out.println(" 2 - Adicionar Bebida");
                System.out.println(" 3 - Ver Items por Tipo");
                System.out.println(" 4 - Criar Ementa");
                System.out.println(" 5 - Ver Ementa de Hoje");
                System.out.println(" 6 - Ver Ementa Geral");
                System.out.println(" 7 - Ver Pedidos pendentes");
                System.out.println(" 8 - Ver Pedidos Geral");
                System.out.println(" 9 - Ver Funcionários Registados");
                System.out.println(" 10 - Ver Clientes Registados");
                System.out.println(" 0 - Sair");

            } else if (user instanceof Cliente) {

                System.out.println(" --- ÁREA DO CLIENTE | CANTINA ---");
                System.out.println(" 1 - Ver Ementa de Hoje");
                System.out.println(" 2 - Criar Pedido");
                System.out.println(" 3 - Ver estado do meu pedido");
                System.out.println(" 4 - Ver pedidos anteriores");
                System.out.println(" 0 - Sair");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}