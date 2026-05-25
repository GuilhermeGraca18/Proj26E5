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
        if(user.getSenha().equals(pass)){
            return true;
        } else {
            return false;
        }
    }

    // PEDIDOS - METODOS

    public void criarPedidos(Pedido pedido){
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
    
    public Ementa pesquisarEmentaHoje()
    {
        for (Ementa ementa : cantina.ementas)
        {
            if(ementa.getData().equals((LocalDate.now()))){
            	
                return ementa;
            }
        }
        
        return null;
    }

    // LISTA DE ITEMS - METODOS
    
    public Item pesquisarItem(int codigoItem){
        return cantina.pesquisarItem(codigoItem);
    }

    public void registarItem(Item item){
        cantina.registarItem(item);
    }

    public ArrayList<Item> getListaItems(){
        return  cantina.getItems();
    }

    public void eliminarItem(int codigoItem){
        cantina.eliminarItem(codigoItem);
    }
    
    // CONSULTAR CLIENTES
    public ArrayList<Utilizador> getUtilizadoresClientes(){
        ArrayList<Utilizador> listaClientes = new ArrayList<>();
        for (Utilizador cliente : utilizadores){
            if (cliente.getTipo().equals(TipoUtilizador.CLIENTE)){
                listaClientes.add(cliente);
            }
        }
        return listaClientes;
    }

    // CONSULTAR FUNCIONÁRIOS
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
     * Métododo para criar relatorio atualizado com comunicação entre cliente e servidor
     */
    public ArrayList<Object> criarRelatorio() {
        
    	ArrayList<Object> relatorio = new ArrayList<>();
        double total = 0;

        for (Pedido i : pedidos) {
        	
            for (Item j : i.getItens()) {
                
            	ArrayList<Object> linha = new ArrayList<>();
            	
                linha.add(j.getNome());
                linha.add(j.getCodigo());
                linha.add(j.getPreco());
                linha.add(i.getData());
                relatorio.add(linha);
                
                total = total + j.getPreco();
            }
        }

        relatorio.add(total);
        
        return relatorio;
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

    public void adicionarItemEmenta(LocalDate data, int codigoItem, int stock){
        Ementa ementa = pesquisarEmenta(data);
        Item item = pesquisarItem(codigoItem);
        
        if (ementa == null) {
            System.out.println("[ERRO] Ementa não encontrada para o dia: " + data);
            return;
        }

        if (item == null) {
            System.out.println("[ERRO] Item não encontrado com código: " + codigoItem);
            return;
        }
        ementa.adicionarItemDia(item, stock);
    }

    public Item pesquisarItemEmenta(LocalDate data, int codigoItem){
        Ementa ementa = pesquisarEmenta(data);
        
        if (ementa == null) return null;
        
        return ementa.pesquisarItem(codigoItem);
    }

    public ArrayList<Ementa> getEmentas(){
        return  cantina.getEmentas();
    }
}
