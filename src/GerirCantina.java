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

    public GerirCantina() {
        this.utilizadores = new ArrayList<>();
        this.cantina = new Cantina();
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
        cliente.adicionarPedido(new Pedido(bebida, notas));
    }

    public void adicionarItemsPedido(int codigoCliente){
        // TODO: pesquisar se tem pedido realizado na data de hoje se sim colocar la o item, se não enviar mensagem a dizer para criar pedido primeiro
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
