import backend.Conta;
import backend.ContaManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ContaManagerTest {

    public static final String TEST_JSON_PATH = "_testdata.json";

    @BeforeEach
    void beforeEach() {
        System.out.println("Removendo (se existir) " + TEST_JSON_PATH);
        try {
            Files.deleteIfExists(Paths.get(TEST_JSON_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getInstanceTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);
        assert( cm != null );
    }

    @ParameterizedTest
    @ValueSource(ints = { Conta.TIPO_CONTA_NORMAL, Conta.TIPO_CONTA_BONUS, Conta.TIPO_CONTA_POUPANCA })
    void addContaTest(int tipoConta) {
        int numConta = 220 + tipoConta;
        System.out.println("Tipo conta: " + Conta.getTipoContaString(tipoConta));
        var cm = ContaManager.getInstance(TEST_JSON_PATH);
        cm.addConta(numConta, tipoConta, 0);
        assert(cm.contaExiste(numConta));
    }

    @ParameterizedTest
    @ValueSource(ints = { Conta.TIPO_CONTA_NORMAL, Conta.TIPO_CONTA_BONUS, Conta.TIPO_CONTA_POUPANCA })
    void addContaNegativaTest(int tipoConta) {
        int numConta = 456 + tipoConta; // variacao
        System.out.println("Tipo conta: " + Conta.getTipoContaString(tipoConta));
        var cm = ContaManager.getInstance(TEST_JSON_PATH);
        cm.addConta(numConta, tipoConta, -200);
        assert(cm.contaExiste(numConta));
        assert(cm.getConta(numConta).getSaldo() == -200);
    }

    @ParameterizedTest
    @ValueSource(ints = { Conta.TIPO_CONTA_NORMAL, Conta.TIPO_CONTA_BONUS, Conta.TIPO_CONTA_POUPANCA })
    void removeContaTest(int tipoConta) {
        int numConta = 321 + tipoConta; // variacao
        System.out.println("Tipo conta: " + Conta.getTipoContaString(tipoConta));
        var cm = ContaManager.getInstance(TEST_JSON_PATH);
        cm.addConta(numConta, tipoConta, 0);
        cm.removeConta(numConta);
        assert(!cm.contaExiste(numConta));
    }

    @ParameterizedTest
    @ValueSource(ints = { Conta.TIPO_CONTA_NORMAL, Conta.TIPO_CONTA_BONUS, Conta.TIPO_CONTA_POUPANCA })
    void debitarContaTest(int tipoConta) {
        System.out.println("Tipo conta: " + Conta.getTipoContaString(tipoConta));

        int numConta = 425 + tipoConta; // variacao

        var cm = ContaManager.getInstance(TEST_JSON_PATH);

        cm.addConta(numConta, tipoConta, 0);
        assert(cm.contaExiste(numConta));
        cm.debito(numConta, 500);

        if (tipoConta == Conta.TIPO_CONTA_NORMAL) {
            assert(cm.getConta(numConta).getSaldo() == -500);
            assert(cm.getConta(numConta).getPontuacao() == -1);
        }

        if (tipoConta == Conta.TIPO_CONTA_BONUS) {
            assert(cm.getConta(numConta).getSaldo() == -500);
            assert(cm.getConta(numConta).getPontuacao() == 15);
        }

        if (tipoConta == Conta.TIPO_CONTA_POUPANCA){
            assert(cm.getConta(numConta).getSaldo() == 0);
            assert(cm.getConta(numConta).getPontuacao() == -1);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { Conta.TIPO_CONTA_NORMAL, Conta.TIPO_CONTA_BONUS, Conta.TIPO_CONTA_POUPANCA })
    void creditarContaTest(int tipoConta) {
        int numConta = 700 + tipoConta;
        System.out.println("Tipo conta: " + Conta.getTipoContaString(tipoConta));
        var cm = ContaManager.getInstance(TEST_JSON_PATH);

        cm.addConta(numConta, tipoConta, 0);
        assert(cm.contaExiste(numConta));
        cm.credito(numConta, 1200);
        cm.credito(numConta, 520);
        System.out.println(cm.getConta(numConta).getSaldo());
        assert(cm.getConta(numConta).getSaldo() == 1720);

        if (tipoConta == Conta.TIPO_CONTA_NORMAL) {
            assert(cm.getConta(numConta).getPontuacao() == -1);
        }

        if (tipoConta == Conta.TIPO_CONTA_BONUS) {
            assert(cm.getConta(numConta).getPontuacao() == 10);
        }

        if (tipoConta == Conta.TIPO_CONTA_POUPANCA){
            assert(cm.getConta(numConta).getPontuacao() == -1);
        }
    }

    @Test
    void transferenciaEntreContasTest() {
        var cm = ContaManager.getInstance(TEST_JSON_PATH);

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
