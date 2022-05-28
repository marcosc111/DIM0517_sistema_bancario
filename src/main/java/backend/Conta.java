package backend;

public class Conta {

    private int id;
    private float saldo;

    public Conta(int id, float saldo) {
        this.id = id;
        this.saldo = saldo;
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

    @Override
    public String toString() {
        return "Conta [ " +
                "id = " + id +
                ", saldo = " + saldo +
                " ]";
    }
}
