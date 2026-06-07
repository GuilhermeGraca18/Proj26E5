import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
                System.out.println(" 4 - Abrir Monitor de Pedidos");
                System.out.println(" 0 - Sair");

                System.out.print("\nInsira a ação que deseja: ");
                n = input.nextInt();
                input.nextLine();

                switch (n) {
                
                    case 1: // REGISTRO CLIENTE
                    	
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
                        
                    case 2: // REGISTRO FUNCIONÁRIO
                    	
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
                        
                    case 3: // LOGIN
                    	
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
                        
                    case 4: // MONITOR DE PEDIDOS
                    	
                        try {
                        	
                            String classpath = System.getProperty("java.class.path");

                            String comando =
                                    "java -cp '" + classpath + "' MonitorCliente";

                            new ProcessBuilder(
                                    "osascript",
                                    "-e",
                                    "tell application \"Terminal\" to do script \"" + comando + "\""
                            ).start();

                        } catch (Exception e) {
                        	
                            e.printStackTrace();
                        }
                        
                        break;
                        
                    case 0: // SAIR
                    	
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

            if (user != null && user.getTipo().equals(TipoUtilizador.FUNCIONARIO)) {

                while (n != 0) {
                    System.out.println(" --- ÁREA DO FUNCIONÁRIO | CANTINA ---");
                    System.out.println(" 1 - Entregar Pedido Pendente"); // FEITO
                    System.out.println(" 2 - Adicionar Item"); // FEITO
                    System.out.println(" 3 - Ver Items"); // FEITO
                    System.out.println(" 4 - Eliminar Item"); // FEITO
                    System.out.println(" 5 - Criar Ementa"); // FEITO
                    System.out.println(" 6 - Ver Ementa de Hoje"); // FEITO
                    System.out.println(" 7 - Ver Ementa Geral"); // FEITO
                    System.out.println(" 8 - Ver Pedidos Geral"); // FEITO
                    System.out.println(" 9 - Ver Clientes"); // FEITO
                    System.out.println(" 10 - Ver Funcionários"); // FEITO
                    System.out.println(" 11 - Criar Relatório"); // FEITO

                    System.out.println(" 0 - Sair");

                    try {
                    	
                        System.out.print("\nInsira a ação que deseja: ");
                        n = input.nextInt();
                        
                    } catch (Exception e){
                        System.out.println("[ERRO] Tipo de dados inserido inválido");
                    }


                    switch (n){
                    
                        case 1: // ENTREGAR PEDIDO
                        	
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
                                System.out.print("Código do Item ( >= 1): ");
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
                                Item novoItem = null;
                                
                                if(tipo.equals(TipoItem.Prato)){
                                    System.out.print("Tipo de Prato (1 - Carne , 2 - Peixe, 3 - Vegetariano): ");
                                    int ntipoPrato = input.nextInt();

                                    while (ntipoPrato > 3 || ntipoPrato < 1){
                                        System.out.println("[ERRO] Digite um dos números apresentados");
                                        System.out.print("Tipo de Prato (1 - Carne , 2 - Peixe, 3 - Vegetariano): ");
                                        ntipoPrato = input.nextInt();
                                    }
                                    TipoPrato tipoPrato = null;
                                    if (ntipoPrato == 1){
                                        tipoPrato = TipoPrato.Carne;
                                    } else if (ntipoPrato == 2) {
                                        tipoPrato = TipoPrato.Peixe;
                                    } else if (ntipoPrato == 3) {
                                        tipoPrato = TipoPrato.Vegetariano;
                                    }

                                    novoItem = new Prato(
                                            codigo,
                                            nome,
                                            descricao,
                                            preco,
                                            tipo,
                                            tipoPrato
                                    );

                                } else {
                                    novoItem = new Item(
                                            codigo,
                                            nome,
                                            descricao,
                                            preco,
                                            tipo
                                    );
                                }


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
                            
                        case 4: // ELIMINAR ITEM
                        	
                            try {
                            	
                                System.out.println(" === ELIMINAR ITEM === ");
                                System.out.print("Código do Item: ");
                                int codigoItem = input.nextInt();

                                saida.reset();
                                saida.writeObject(new Mensagem("ELIMINAR_ITEM", codigoItem));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                System.out.println(resposta.getDados());

                            } catch (Exception e){
                            	
                                System.out.println("[ERRO] Dados inválidos inseridos!");
                            }
                            
                            break;
                            
                        case 5: // CRIAR EMENTA
                        	
                            try {
                            	
                                LocalDate dataEmeta = null;
                                input.nextLine();
                                Mensagem resposta = null;

                                System.out.println(" === CRIAR EMENTA === ");
                                System.out.print("Data/Dia (dd/mm/aaaa): ");
                                String dataString = input.nextLine();

                                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                                dataEmeta = LocalDate.parse(dataString, formatador);

                                saida.reset();
                                saida.writeObject(new Mensagem("CRIAR_EMENTA", dataEmeta));
                                saida.flush();

                                resposta = (Mensagem) entrada.readObject();
                                System.out.println(resposta.getDados());
                                if(resposta.getTipo().equalsIgnoreCase("TRUE")){
                                    int escolha = -1;
                                    while (escolha != 0) {

                                        saida.reset();
                                        saida.writeObject(new Mensagem("VER_LISTA_ITEMS", null));
                                        saida.flush();

                                        resposta = (Mensagem) entrada.readObject();
                                        ArrayList<Item> listaItems = (ArrayList<Item>) resposta.getDados();

                                        for (Item item : listaItems) {
                                            System.out.println(item.toStringLista());
                                        }

                                        System.out.print("Código do Item (0 para fechar ementa): ");
                                        escolha = input.nextInt();

                                        if (escolha == 0) {
                                            break;
                                        }
                                        System.out.print("Número de Pratos/Stock do dia: ");
                                        int stock = input.nextInt();

                                        ArrayList<Object> dados = new ArrayList<>();

                                        dados.add(escolha);
                                        dados.add(stock);
                                        dados.add(dataEmeta);

                                        saida.reset();
                                        saida.writeObject(new Mensagem("ADICIONAR_ITEM_EMENTA", dados));
                                        saida.flush();

                                        resposta = (Mensagem) entrada.readObject();
                                        System.out.println(resposta.getDados());
                                    }
                                    System.out.println("[SUCESSO] Ementa Fechada!");
                                }

                            } catch (Exception e){
                            	
                            	System.out.println("[ERRO] O Programa não conseguiu criar a ementa!");
                            }
                            
                            break;
                            
                        case 6: // VER EMENTA DO DIA
                        	
                        	try {
                        		
                        		saida.reset();
                        		saida.writeObject(new Mensagem("VER_EMENTA_DIA", null));
                        		saida.flush();
                        		
                        		Mensagem resposta = (Mensagem) entrada.readObject();
                        		Ementa ementaHoje = (Ementa) resposta.getDados();
                        		
                        		System.out.println(ementaHoje);
                        		
                        	} catch (Exception e) {
                        		
                        		System.out.println("[ERRO] O Programa não conseguiu ler a ementa do dia!");
                        	}
                            
                        	break;
                        	
                        case 7: // VER EMENTAS GERAL
                        	
                            try {
                            	
                                saida.reset();
                                saida.writeObject(new Mensagem("VER_EMENTAS", null));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                ArrayList<Ementa> listaEmenta = (ArrayList<Ementa>) resposta.getDados();

                                for (Ementa ementa : listaEmenta) {
                                	
                                    System.out.println(ementa);
                                }
                                
                            } catch (Exception e) {
                            	
                            	System.out.println("[ERRO] O Programa ler a ementa geral!");
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
                            
                        case 9: // CONSULTAR CLIENTES

                            try {
                            	
                                System.out.println(" === CLIENTES REGISTADOS === ");
                                saida.reset();
                                saida.writeObject(new Mensagem("VER_CLIENTES", null));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                ArrayList<Utilizador> listaClientes = (ArrayList<Utilizador>) resposta.getDados();
                                
                                for (Utilizador cliente : listaClientes) {
                                    System.out.println(cliente);

                                }

                            } catch (Exception e){
                            	
                                System.out.println("[ERRO] O Programa não conseguiu ler a lista de clientes!");
                            }
                        	
                        	break;
                        	
                        case 10: // CONSULTAR FUNCIONARIOS
                        	
                            try {
                            	
                                System.out.println(" === FUNCIONÁRIOS REGISTADOS === ");
                                saida.reset();
                                saida.writeObject(new Mensagem("VER_FUNCIONARIOS", null));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                ArrayList<Utilizador> listaFuncionarios = (ArrayList<Utilizador>) resposta.getDados();

                                for (Utilizador funcionario : listaFuncionarios) {
                                    System.out.println(funcionario);
                                }

                            } catch (Exception e){
                                System.out.println("[ERRO] O Programa não conseguiu ler a lista de funcionários!");
                            }

                            break;
                            
                        case 11: // CRIAR RELATORIO
                        	
	                        try {
	                        	
                        		saida.reset();
	                            saida.writeObject(new Mensagem("RELATORIO_VENDAS", null));
	                            saida.flush();
	
	                            Mensagem respostaRelatorio = (Mensagem) entrada.readObject();
	                            ArrayList<Object> dadosRelatorio = (ArrayList<Object>) respostaRelatorio.getDados();
	
	                            System.out.println("\n===== RELATÓRIO DE VENDAS =====");
	                            System.out.printf("%-25s %-10s %-10s %-12s%n", "ARTIGO", "CÓDIGO", "PREÇO", "DATA");
	                            System.out.println("-".repeat(60));
	
	                            double totalVendas = 0;
	                            for (int i = 0; i < dadosRelatorio.size() - 1; i++) {
	                            	
	                                ArrayList<Object> linha = (ArrayList<Object>) dadosRelatorio.get(i);
	                                
	                                String nome = (String) linha.get(0);
	                                int codigo = (int) linha.get(1);
	                                double preco = (double) linha.get(2);
	                                Object data = linha.get(3);
	                                
	                                System.out.printf("%-25s %-10d %-10.2f %-12s%n", nome, codigo, preco, data);
	                                totalVendas += preco;
	                                
	                            }
	
	                            System.out.println("Total de Vendas: " + totalVendas);
                            
	                        } catch (Exception e) {
	                        	
	                        	System.out.println("[ERRO] O Programa não conseguiu criar o relatório!");
	                        }
                            break;
                        	
                        case 0: // SAIR
                        	
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
                    System.out.println(" 1 - Ver ementa de hoje"); // FEITO
                    System.out.println(" 2 - Criar Pedido"); // FEITO
                    System.out.println(" 3 - Ver estado do meu pedido"); // FEITO
                    System.out.println(" 4 - Ver histórico pedidos"); // FEITO
                    System.out.println(" 0 - Sair"); // FEITO

                    System.out.print("\nInsira a ação que deseja: ");
                    n = input.nextInt();

                    switch (n) {
                    
                    	case 1: // VER EMENTA DE HOJE
                    		
                    		try {
                        		
                        		saida.reset();
                        		saida.writeObject(new Mensagem("VER_EMENTA_DIA", null));
                        		saida.flush();
                        		
                        		Mensagem resposta = (Mensagem) entrada.readObject();
                        		Ementa ementaHoje = (Ementa) resposta.getDados();
                        		
                        		System.out.println(ementaHoje);
                        		
                        	} catch (Exception e) {
                        		System.out.println("[ERRO] O Programa não conseguiu ler a ementa do dia!");
                        	}
                            
                    		break;
                    
                        case 2: // CRIAR PEDIDO
                            try {

                                System.out.println(" === CRIAR PEDIDO === ");

                                input.nextLine();

                                System.out.print("Deseja colocar alguma nota no pedido, se sim escreva: ");
                                String notas = input.nextLine();

                                saida.reset();
                                saida.writeObject(new Mensagem("CRIAR_PEDIDO", notas));
                                saida.flush();


                                Mensagem resposta = (Mensagem) entrada.readObject();
                                System.out.println(resposta.getDados());

                                if (!resposta.getTipo().equalsIgnoreCase("ERRO")){

                                    int nitem = -1;

                                    while (nitem != 0) {

                                        System.out.print("Adicionar item (número do item presente na ementa / 0 para terminar): ");
                                        nitem = input.nextInt();

                                        if (nitem == 0) {
                                            break;
                                        }

                                        saida.reset();
                                        saida.writeObject(new Mensagem("ADICIONAR_ITEM_PEDIDO", nitem));
                                        saida.flush();

                                        resposta = (Mensagem) entrada.readObject();
                                        System.out.println(resposta.getDados());

                                    }

                                    System.out.println("[INFO] Pedido a fazer! Fique atento ao ecrã dos pedidos e ao seu número de cliente!");
                                }

                                System.out.println("[SUCESSO] Pedido Finalizado!");


                            } catch (Exception e){
                                System.out.println("[ERRO] O Programa não conseguiu criar o pedido!");
                            }
                            break;
                            
                        case 3: // VER ESTADO DO PEDIDO
                            try {
                                saida.reset();
                                saida.writeObject(new Mensagem("VER_ESTADO_PEDIDO", null));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                System.out.println(resposta.getDados());
                            } catch (Exception e){
                                System.out.println("[ERRO] O Programa não conseguiu ler o estado do pedido!");
                            }
                    		break;

                        case 4: // VER HISTORICO DE PEDIDOS

                            try {
                            	
                                saida.reset();
                                saida.writeObject(new Mensagem("HISTORICO_PEDIDOS", null));
                                saida.flush();

                                Mensagem resposta = (Mensagem) entrada.readObject();
                                ArrayList<Pedido> historico = (ArrayList<Pedido>) resposta.getDados();

                                if (historico.isEmpty()){
                                	
                                    System.out.println("[INFO] Ainda não tem pedidos registrados.");
                                    
                                } else {
                                	
                                    System.out.println("\n===== HISTÓRICO DE PEDIDOS =====");
                                    
                                    for (Pedido pedido : historico) {
                                    	
                                        System.out.println("--------------------------------");
                                        System.out.println(pedido);
                                    }
                                    
                                    System.out.println("================================\n");
                                }

                            } catch (Exception e) {
                            	
                                System.out.println("[ERRO] O Programa não conseguiu ler o histórico!");
                            }
                            break;
                    			
                        case 0: // SAIR
                        	
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

    public static TipoItem escolherTipo() throws Exception {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();

        Attributes original = terminal.enterRawMode();

        TipoItem[] tipos = TipoItem.values();
        int selecionado = 0;

        try {
            while (true) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.writer().println("=== ESCOLHER TIPO ===\n");

                for (int i = 0; i < tipos.length; i++) {
                    if (i == selecionado) {
                        terminal.writer().println("> " + tipos[i]);
                    } else {
                        terminal.writer().println("  " + tipos[i]);
                    }
                }

                terminal.writer().flush();

                int ch = terminal.reader().read();

                if (ch == 27) { // ESC
                    terminal.reader().read(); // [
                    int arrow = terminal.reader().read();

                    if (arrow == 65) { // cima
                        selecionado = (selecionado - 1 + tipos.length) % tipos.length;
                    } else if (arrow == 66) { // baixo
                        selecionado = (selecionado + 1) % tipos.length;
                    }

                } else if (ch == 10 || ch == 13) {
                    return tipos[selecionado];
                }
            }

        } finally {
            terminal.setAttributes(original);
            terminal.close();
        }
    }
}