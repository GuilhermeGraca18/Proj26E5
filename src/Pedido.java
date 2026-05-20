import java.time.LocalDate;
import java.util.ArrayList;

import java.io.Serializable;

public class Pedido implements Serializable{
    private int codigo;
    private Bebida bebida;
    private ArrayList<Item> items; // todo: verificar se tem prato, sobremesa e entrega e quantidades, por causa do preço
    private EstadoPedido estado;
    private String notas;
    private LocalDate data;

    public Pedido(int codigo, Bebida bebida, String notas) {
        this.codigo = codigo;
        this.bebida = bebida;
        this.items = new ArrayList<>();
        this.estado = EstadoPedido.A_FAZER;
        this.notas = notas;
        this.data = LocalDate.now();
    }

    public void adicionarItems(Item item){
        items.add(item);
    }

    public int getCodigo() {
        return codigo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void entregarPedido(){
        estado = EstadoPedido.A_ENTREGAR;
    }

    public void pedidoEntregue(){
        estado = EstadoPedido.ENTREGUE;
    }

    public void pedidoNaoEntregue(){
        estado = EstadoPedido.NAO_ENTREGUE;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        String listaItems = "";

        for (Item item : items) {
            listaItems += "\n - " + item;
        }

        return "CODIGO #" + codigo +
                "\nBebida: " + bebida.getNome() +
                "\nItems:" + listaItems +
                "\nNotas: " + notas +
                "\nEstado: " + estado;
    }

}
