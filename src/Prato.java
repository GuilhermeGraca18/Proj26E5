public class Prato extends Item{
    private TipoPrato tipoPrato;

    public Prato(int codigo, String nome, String descricao, TipoItem tipo, TipoPrato tipoPrato) {
        super(codigo, nome, descricao, tipo);
        this.tipoPrato = tipoPrato;
    }
}
