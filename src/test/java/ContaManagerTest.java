import backend.Conta;
import backend.ContaManager;
import org.junit.jupiter.api.Test;

public class ContaManagerTest {

    public static final String TEST_JSON_PATH = "_testdata.json";

    @Test
    void getInstanceTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);
        assert( cm != null );
    }

    @Test
    void addContaTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);
        cm.addConta(456, Conta.TIPO_CONTA_NORMAL, 0);
        assert(cm.contaExiste(456));
    }

    @Test
    void removeContaTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);
        cm.addConta(321, Conta.TIPO_CONTA_NORMAL, 0);
        cm.removeConta(321);
        assert(!cm.contaExiste(321));
    }

    @Test
    void debitarContaTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);

        cm.removeConta(991);

        cm.addConta(991, Conta.TIPO_CONTA_NORMAL, 0);
        assert(cm.contaExiste(991));
        cm.debito(991, 500);
        assert(cm.getConta(991).getSaldo() == -500);
    }

    @Test
    void creditarContaTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);

        cm.removeConta(992);

        cm.addConta(992, Conta.TIPO_CONTA_NORMAL, 0);
        assert(cm.contaExiste(992));
        cm.credito(992, 1200);
        cm.credito(992, 520);
        assert(cm.getConta(992).getSaldo() == 1720);
    }

    @Test
    void transferenciaEntreContasTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);

        cm.removeConta(993);
        cm.removeConta(994);

        cm.addConta(993, Conta.TIPO_CONTA_NORMAL, 0);
        cm.addConta(994, Conta.TIPO_CONTA_NORMAL, 0);
        assert(cm.contaExiste(993));
        assert(cm.contaExiste(994));

        cm.credito(993, 2000);
        cm.credito(994, 5000);

        cm.transferencia(993, 994, 1300);

        assert(cm.getConta(993).getSaldo() == 700);
        assert(cm.getConta(994).getSaldo() == 6300);
    }
}
