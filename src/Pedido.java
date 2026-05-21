import java.time.LocalDate;
import java.util.ArrayList;

import java.io.Serializable;

public class Pedido implements Serializable{
    private Utilizador cliente;
    private ArrayList<Item> items; // todo: verificar se tem prato, sobremesa e entrega e quantidades, por causa do preço
    private EstadoPedido estado;
    private String notas;
    private LocalDate data;

    public Pedido(Utilizador cliente, String notas) {
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.estado = EstadoPedido.A_FAZER;
        this.notas = notas;
        this.data = LocalDate.now();
    }

    public void adicionarItems(Item item){
        items.add(item);
    }

    public Utilizador getCliente() {
        return cliente;
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

        return "CODIGO #" + cliente.getCodigo() +
                "\nItems:" + listaItems +
                "\nNotas: " + notas +
                "\nEstado: " + estado;
    }

}
