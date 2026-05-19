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

    public void visualizarTodosPedidosPendentes(){
        System.out.println("Pedidos pendentes:");

        boolean encontrou = false;

        for (Utilizador u: utilizadores){
            if(u instanceof Cliente){
                Cliente cliente = (Cliente) u;
                Pedido pendente = cliente.getPedidosPendentes();

                if (pendente != null){
                    encontrou = true;
                    System.out.println("\nCliente: " + getNome() + " (" +
                            "código: " + getCodigo() + ")" );
                    System.out.println(pendente);
                }
            }
        }
        if(!encontrou){
            System.out.println("Não existem pedidos pendentes.");
        }
    }

    public void visualizarPedidoPendenteCliente(){
        System.out.println("O seu pedido pendente: ");
        Pedido pendente = cliente.getPedidoPendente();

        if (pendente != null){
            System.out.println(pendente);
        }else{
            System.out.println("Não tem nenhum pedido pendente.");
        }
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
