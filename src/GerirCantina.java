import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;

/**
 * Classe Gerir - Contêm todos os metodos para gerir o programa e os dados/objetos
 * @author Grupo 5 
 * @version 07/07/2026
 */

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


    /**
     * Metodo para registar Cliente ou Funcionários
     * @param user Utilizador a resgistar
     */
    public void registarUser(Utilizador user){
        if (user!= null){
            utilizadores.add(user);
        }
    }

    /**
     * Metodo para pesquisar um utilizador
     * @param codigoUser Codigo do Utilizador
     * @return Retorna o objeto do Utilizador pesquisado ou valor nulo se nao existir
     */
    public Utilizador pesquisarUtilizador(int codigoUser){
        for (Utilizador user : utilizadores){
            if(user.getCodigo() == codigoUser){
                return user;
            }
        }
        return null;
    }

    /**
     * Verifica se a palavra-passe está correta ou não (ass: Guilherme Graça 53861)
     * @param user Utilizador
     * @param pass Palavra-passe inserida
     * @return Retorna valor Verdadeiro ou Falso
     */
    public boolean verificarPassword(Utilizador user, String pass){
        if(user.getSenha().equals(pass)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Simão Gonçalves  53570
     * Métododo para adicionar pedidos 
     */
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
            if(pedido.getCliente().getCodigo() == cliente.getCodigo() && pedido.getData().equals(LocalDate.now())){
                return pedido;
            }
        }

        return null;
    }

    /**
     * Metodo par apesquisar Pedido do dia do cliente (ass: Guilherme Graça 53861)
     * @param cliente Cliente para pesquisar se há pedido de dia ou não
     * @return Retorna um valor Verdadeiro se tiver um pedido realizado do dia ou Falso se não tiver
     */
    public boolean pesquisarPedidoDia(Utilizador cliente) {
        for (Pedido pedido : pedidos){
            if(pedido.getCliente().equals(cliente) && pedido.getData().equals(LocalDate.now())){
                return false;
            }
        }
        return true;
    }

    /**
     * @author Simão Gonçalves  53570
     * Adiciona um item ao pedido pendente do cliente, decrementando o stock da ementa
     */
    public boolean adicionarItemsPedido (Utilizador cliente, int codigoItem){
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
    
    /**
     * @author Simão Gonçalves  53570
     * Métododo para pesquisar a ementa do dia
     */
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

    /**
     * Metodo para pesquisar Item (ass: Guilherme Graça 53861)
     * @param codigoItem Código do Item
     * @return Retorna o Item
     */
    public Item pesquisarItem(int codigoItem){
        return cantina.pesquisarItem(codigoItem);
    }

    /**
     * Metodo para registar Item (ass: Guilherme Graça 53861)
     * @param item Objeto do item a pesquisar
     */
    public void registarItem(Item item){
        cantina.registarItem(item);
    }
    
    /**
     * Método para retornar lista de itens
     * @return lista de itens
     */
    public ArrayList<Item> getListaItems(){
        return  cantina.getItems();
    }

    /**
     * Metodo para pesquisar Item (ass: Guilherme Graça 53861)
     * @param codigoItem Código do Item a pesquisar
     */
    public void eliminarItem(int codigoItem){
        cantina.eliminarItem(codigoItem);
    }

    /**
     * Método para consultar e retornar a lista de utilizadores do tipo cliente.
     * @return lista de clientes.
     */
    public ArrayList<Utilizador> getUtilizadoresClientes(){
        ArrayList<Utilizador> listaClientes = new ArrayList<>();
        for (Utilizador cliente : utilizadores){
            if (cliente.getTipo().equals(TipoUtilizador.CLIENTE)){
                listaClientes.add(cliente);
            }
        }
        return listaClientes;
    }

    /**
     * Método para consultar e retornar a lista de utilizadores do tipo funcionário.
     * @return lista de funcionários.
     */
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
    
    /**
     * @author Arthur Santana - 53987
     * 
     * Método para retornar todos os pedidos de um cliente específico
     * @return Lista de pedidos do cliente
     */
    public ArrayList<Pedido> getHistoricoPedidos(Utilizador cliente) {
    	
        ArrayList<Pedido> historico = new ArrayList<>();
        
        for (Pedido pedido : pedidos) {
        	
            if (pedido.getCliente().getCodigo() == cliente.getCodigo()) {
            	
                historico.add(pedido);
            }
        }
        return historico;
    }

    /**
     * Método para retornar a lista de pedidos da cantina
     * @return lista de pedidos
     */
    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    /**
     * Método para retornar a lista de utilizadores da cantina
     * @return lista de utilizadores
     */
    public ArrayList<Utilizador> getUtilizadores() {
        return utilizadores;
    }

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
     * Metodo para caregar os dados do ficheiro ("dados.dat") (ass: Guilherme Graça 53861)
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

    /**
     * Metodo para pesquisar a Ementa pela data (ass: Guilherme Graça 53861)
     * @param data Data da ementa a pesquisar
     * @return Retorna a ementa, se existir nesse dia inserido.
     */
    public Ementa pesquisarEmenta(LocalDate data){
        for (Ementa ementa : cantina.ementas){
            if(ementa.getData().equals(data)){
                return ementa;
            }
        }
        return null;
    }

    /**
     * Metodo para adicionar Item à ementa (ass: Guilherme Graça 53861)
     * @param data Data da ementa
     * @param codigoItem Codigo do Item a adicionar
     * @param stock Stock do Item
     */
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

    /**
     * Metodo para pesquisar um item da Ementa (ass: Guilherme Graça 53861)
     * @param data Data da Ementa
     * @param codigoItem Código do item a pesquisar na Ementa
     * @return Retorna o Item
     */
    public Item pesquisarItemEmenta(LocalDate data, int codigoItem){
        Ementa ementa = pesquisarEmenta(data);
        
        if (ementa == null) return null;
        
        return ementa.pesquisarItem(codigoItem);
    }

    public ArrayList<Ementa> getEmentas(){
        return  cantina.getEmentas();
    }
}
