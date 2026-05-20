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

    public Utilizador pesquisarCliente(int codigoCliente){
        for (Utilizador cliente : utilizadores){
            if(cliente.getCodigo() == codigoCliente){
                return cliente;
            }
        }
        return null;
    }

    public void criarPedidos(int codigoCliente, Bebida bebida, String notas){
        Utilizador cliente = pesquisarCliente(codigoCliente);
        Pedido pedido = new Pedido(codigoCliente, bebida, notas);
        pedidos.add(pedido);
    }

    /**
     * Pesquisa se existe um pedido pendente do cliente, pelo codigo do Cliente
     * @param codigoCliente Código do Cliente a pesquisar o pedido
     * @return Retorna o pedido ou nulo, se não existir
     */
    public Pedido pesquisarPedidoPendente(int codigoCliente){
        for (Pedido pedido : pedidos){
            if(pedido.getCodigo() == codigoCliente && pedido.getData().equals(LocalDate.now()) && pedido.getEstado() == EstadoPedido.A_FAZER){
                return pedido;
            }
        }
        return null;
    }

    public Ementa pesquisarEmentaHoje(){
        for (Ementa ementa : cantina.ementas){
            if(ementa.getData().equals(LocalDate.now())){
                return ementa;
            }
        }
        return null;
    }

    public void adicionarItemsPedido(int codigoCliente, int codigoItem){
        Utilizador cliente = pesquisarCliente(codigoCliente);
        Pedido pedido = pesquisarPedidoPendente(codigoCliente);

    }





    /**
     * Metodo para guardar os dados no ficheiro ("dados.dat") sempre que o projeto fecha
     */
    public void guardarDados(){
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dados.dat"));

            out.writeObject(utilizadores);
            out.writeObject(cantina);

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

            utilizadores = (ArrayList<Utilizador>)  in.readObject();
            cantina = (Cantina) in.readObject();

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
