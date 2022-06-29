package backend;

public class Conta {

    protected int id;
    protected float saldo;

    protected int tipoConta;

    protected int pontuacao;

    public static final int TIPO_CONTA_NORMAL = 0;
    public static final int TIPO_CONTA_BONUS = 1;
    public static final int TIPO_CONTA_POUPANCA = 2;

    public Conta(int id, float saldo, int tipoConta, int pontuacao) {
        this.id = id;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.pontuacao = pontuacao;
    }

    public Conta() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(int tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getTipoContaString() {
        switch(tipoConta) {
            case Conta.TIPO_CONTA_BONUS:
                return "BONUS";
            case Conta.TIPO_CONTA_NORMAL:
                return "NORMAL";
            case Conta.TIPO_CONTA_POUPANCA:
                return "POUPANÇA";
            default:
                return "INVÁLIDO";
        }
    }

    public static String getTipoContaString(int tipoConta) {
        switch(tipoConta) {
            case Conta.TIPO_CONTA_BONUS:
                return "BONUS";
            case Conta.TIPO_CONTA_NORMAL:
                return "NORMAL";
            case Conta.TIPO_CONTA_POUPANCA:
                return "POUPANÇA";
            default:
                return "INVÁLIDO";
        }
    }

    public static boolean checkTipoConta(int tipoConta) {
        switch(tipoConta) {
            case Conta.TIPO_CONTA_BONUS:
            case Conta.TIPO_CONTA_NORMAL:
            case Conta.TIPO_CONTA_POUPANCA:
                return true;
            default:
                return false;
        }
    }

    public static int getPontuacaoInicialPorTipoConta(int tipoConta) {
        switch(tipoConta) {
            case Conta.TIPO_CONTA_BONUS:
                return 10;
            case Conta.TIPO_CONTA_NORMAL:
            case Conta.TIPO_CONTA_POUPANCA:
            default:
                return -1;
        }
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id=" + id +
                ", saldo=" + saldo +
                ", tipoConta=" + tipoConta +
                ", pontuacao=" + pontuacao +
                '}';
    }
}