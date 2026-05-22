import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;

public class GerirCantina {

    private static GerirCantina instance;

    public static GerirCantina getInstance(){
        if(instance == null){
            instance = new GerirCantina();
        }
        return instance;
    }

    private ArrayList<Utilizador> utilizadores;
    private Cantina cantina;
    private ArrayList<Pedido> pedidos;

    public GerirCantina() {
        this.utilizadores = new ArrayList<>();
        this.cantina = new Cantina();
        this.pedidos = new ArrayList<>();
    }

    // UTILIZADOR - METODOS

    public void registarUser(Utilizador user){
        if (user!= null){
            utilizadores.add(user);
        }
    }
    public Utilizador pesquisarUtilizador(int codigoUser){
        for (Utilizador user : utilizadores){
            if(user.getCodigo() == codigoUser){
                return user;
            }
        }
        return null;
    }

    public boolean verificarPassword(Utilizador user, String pass){
        if(user.getSenha().equalsIgnoreCase(pass)){
            return true;
        } else {
            return false;
        }
    }

    // PEDIDOS - METODOS

    public void criarPedidos(Pedido pedido){;
        pedidos.add(pedido);
    }

    /**
     * Pesquisa se existe um pedido pendente do cliente, pelo codigo do Cliente
     * @param cliente Utilizador (Cliente) a pesquisar o pedido
     * @return Retorna o pedido ou nulo, se não existir
     */
    public Pedido pesquisarPedidoPendente(Utilizador cliente){
        for (Pedido pedido : pedidos){
            if(pedido.getCliente().equals(cliente) && pedido.getData().equals(LocalDate.now()) && pedido.getEstado() == EstadoPedido.A_FAZER){
                return pedido;
            }
        }

        return null;
    }

    public boolean adicionarItemsPedido(Utilizador cliente, int codigoItem){
        Pedido pedido = pesquisarPedidoPendente(cliente);
        Ementa ementa = pesquisarEmentaHoje();
        if(pedido != null && ementa != null){
            ArrayList<ItemDia> itemsDia = ementa.getItemsDia();
            for (ItemDia itemDia : itemsDia){
                Item item = itemDia.getItem();
                if(item.getCodigo() == codigoItem && itemDia.getStock() > 0){
                    pedido.adicionarItems(item);
                    itemDia.decrementarStock();
                    return true;
                }
            }
        }
        return false;
    }

    // EMENTA - METODOS

    public Ementa pesquisarEmentaHoje(){
        for (Ementa ementa : cantina.ementas){
            if(ementa.getData().equals((LocalDate.now()))){
                return ementa;
            }
        }
        return null;
    }

    // LISTA DE ITEMS - METODOS

    public Item pesquisarItem(int codigoItem){
        ArrayList<Item> items = cantina.items;
        for (Item item : items){
            if (item.getCodigo() == codigoItem){
                return item;
            }
        }
        return null;
    }
    
    /**
     * @author Arthur Santana - 53987
     * Método para consultar todos os utilizadores.
     */
    public void consultarUtilizadores() {
    	
    	System.out.println("\nCLIENTES: ");
    	
    	for(Utilizador i : utilizadores) {
    		
    		if(i.getTipo() == TipoUtilizador.CLIENTE) {
    			
    			System.out.println(i);
    		}
    	}
    	
    	System.out.println("\nFUNCIONÁRIOS: ");
    	
    	for(Utilizador i : utilizadores) {
    		
    		if(i.getTipo() == TipoUtilizador.FUNCIONARIO) {
    			
    			System.out.println(i);
    		}
    	}
    }

    public ArrayList<Utilizador> getUtilizadoresClientes(){
        ArrayList<Utilizador> listaClientes = new ArrayList<>();
        for (Utilizador cliente : utilizadores){
            if (cliente.getTipo().equals(TipoUtilizador.CLIENTE)){
                listaClientes.add(cliente);
            }
        }
        return listaClientes;
    }

    public ArrayList<Utilizador> getUtilizadoresFuncionarios(){
        ArrayList<Utilizador> listaFuncionarios = new ArrayList<>();
        for (Utilizador funcionario : utilizadores){
            if (funcionario.getTipo().equals(TipoUtilizador.FUNCIONARIO)){
                listaFuncionarios.add(funcionario);
            }
        }
        return listaFuncionarios;
    }
    
    /**
     * @author Arthur Santana - 53987
     * Metodo para criar relatorio de vendas de artigos total.
     */
    public void criarRelatorio() {
    	
    	double total = 0;
    	
    	System.out.println("\tArtigos vendidos");
    	System.out.println("\nNome: \tCódigo: \tPreço: \tData: ");
    	System.out.println();
    	
    	for(Pedido i : pedidos) {
    		
    		for(Item j : i.getItens()) {
    			
    			System.out.println(j.getNome() + "\t" + j.getCodigo() + "\t" + j.getPreco() + "\t" + i.getData());
    			
    			total = total + j.getPreco();
    			
    		}
    	}
    	
    	System.out.println("Total de Vendas: " + total);
    }

    public void registarItem(Item item){
        cantina.registarItem(item);
    }

    public ArrayList<Item> getListaItems(){
        return  cantina.getItems();
    }

    // GETS

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public ArrayList<Utilizador> getUtilizadores() {
        return utilizadores;
    }

    // SAVES DE DADOS

    /**
     * Metodo para guardar os dados no ficheiro ("dados.dat") sempre que o projeto fecha
     */
    public void guardarDados(){
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dados.dat"));

            out.writeObject(utilizadores);
            out.writeObject(cantina);
            out.writeObject(pedidos);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo para caregar os dados do ficheiro ("dados.dat")
     */
    public void carregarDados(){
        try {
            ObjectInputStream in = new ObjectInputStream( new FileInputStream("dados.dat"));

            Object objUtilizadores = in.readObject();
            Object objCantina = in.readObject();
            Object objPedidos = in.readObject();

            if (objUtilizadores instanceof ArrayList<?> listaUtilizadores) {
                utilizadores = new ArrayList<>();
                for (Object obj : listaUtilizadores) {
                    utilizadores.add((Utilizador) obj);
                }
            }

            if (objCantina instanceof Cantina) {
                cantina = (Cantina) objCantina;
            }

            if (objPedidos instanceof ArrayList<?> listaPedidos) {
                pedidos = new ArrayList<>();
                for (Object obj : listaPedidos) {
                    pedidos.add((Pedido) obj);
                }
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Diana Santos - 53267
     * Metodo para criar ementa
     */
    
    public void criarEmenta(LocalDate data) {
        if(pesquisarEmenta(data) == null){
            cantina.ementas.add(new Ementa(data));
        }
    }

    public Ementa pesquisarEmenta(LocalDate data){
        for (Ementa ementa : cantina.ementas){
            if(ementa.getData().equals(data)){
                return ementa;
            }
        }
        return null;
    }
}
