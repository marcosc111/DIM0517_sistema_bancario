package backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;



public class ContaManager {

    private static ContaManager instance;
    public final static String DEFAULT_JSON_FILE_PATH = "_data.json";
    private List<Conta> contas;
    private String jsonFilePath;



    private ContaManager(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
        loadContas();
    }

    public static ContaManager getInstance(String jsonFilePath) {
        if (instance == null) {
            instance = new ContaManager(jsonFilePath != null ? jsonFilePath : DEFAULT_JSON_FILE_PATH);
        }
        return instance;
    }

    private void loadContas() {
        String jsonStr = readJSONFile();
        Type contasListType = new TypeToken<ArrayList<Conta>>() {
        }.getType();
        contas = new Gson().fromJson(jsonStr, contasListType);

        if (contas == null)
            contas = new ArrayList<>();
    }

    public String readJSONFile() {
        Path p = Paths.get(jsonFilePath);

        if (!Files.exists(p)) {
            try {
                Files.createFile(p);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        StringBuilder sb = new StringBuilder();
        Stream<String> content = null;
        try {
            content = Files.lines(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        content.forEach(sb::append);
        return sb.toString();
    }

    private void persistContas() {
        writeJSONFile();
    }

    public void writeJSONFile() {

        Path path = Paths.get(jsonFilePath);
        String jsonString = new Gson().toJson(contas);
        try {
            Files.write(path, jsonString.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addConta(int num, int tipoConta, float saldo) {
        if (contaExiste(num))
            return false;

        int pontos = Conta.getPontuacaoPorTipoConta( tipoConta );

        Conta c = new Conta(num, saldo, tipoConta, pontos);
        contas.add(c);
        persistContas();
        return true;
    }

    public boolean removeConta(int num) {
        contas.removeIf(c -> c.getId() == num);
        persistContas();
        return !contaExiste(num);
    }

    public boolean contaExiste(int num) {
        for (Conta c : contas)
            if (c.getId() == num)
                return true;
        return false;
    }

    public Map<Integer, String> getNumeroTodasContas() {
        Map<Integer, String> map = new HashMap<>();
        contas.forEach(c -> map.put(c.getId(), c.getTipoContaString()));
        return map;
    }

    public Conta getConta(int num) {
        for (Conta c : contas)
            if (c.getId() == num)
                return c;
        return null;
    }

    public boolean credito(int numConta, float valor) {
        var c = getConta(numConta);
        if (c == null)
            return false;

        c.setSaldo( c.getSaldo() + valor );
        persistContas();
        return true;
    }

    public boolean debito(int numConta, float valor) {
        var c = getConta(numConta);
        if (c == null)
            return false;

        float valorFuturo = c.getSaldo() - valor;
        if (valorFuturo < 0)
            return false; // saldo insuficiente

        c.setSaldo( valorFuturo );
        persistContas();
        return true;
    }

    public boolean transferencia(int numContaOrigem, int numContaDestino, float valor) {
        var c1 = getConta(numContaOrigem);
        var c2 = getConta(numContaDestino);

        if (c1 == null || c2 == null)
            return false;

        float valorFuturoContaOrigem = c1.getSaldo() - valor;
        if (valorFuturoContaOrigem < 0)
            return false; // saldo insuficiente

        debito(numContaOrigem, valor);
        credito(numContaDestino, valor);

        persistContas();
        return true;
    }

}
