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
        cm.addConta(456);
        assert(cm.contaExiste(456));
    }

    @Test
    void removeContaTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);
        cm.addConta(321);
        cm.removeConta(321);
        assert(!cm.contaExiste(321));
    }
}
