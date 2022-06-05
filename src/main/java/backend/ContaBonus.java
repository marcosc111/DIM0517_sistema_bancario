package backend;

public class ContaBonus extends Conta {

    private int pontuacao;

    public ContaBonus(int id, float saldo) {
        super(id, saldo);
    }

    public ContaBonus() {
        super();
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    @Override
    public String toString() {
        return "ContaBonus [ " +
                "id = " + id +
                ", saldo = " + saldo +
                " ]";
    }
}
