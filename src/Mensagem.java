import java.io.Serializable;

public class Mensagem implements Serializable {
    private String tipo;
    private Object dados;

    public Mensagem(String tipo, Object dados) {
        this.tipo = tipo;
        this.dados = dados;
    }

    public String getTipo() {
        return tipo;
    }

    public Object getDados() {
        return dados;
    }
}