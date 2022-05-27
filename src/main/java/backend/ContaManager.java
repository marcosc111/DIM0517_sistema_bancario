package backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
        Type contasListType = new TypeToken<ArrayList<Conta>>(){}.getType();
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

    public boolean addConta(int num) {

        if (contaExiste(num))
            return false;

        contas.add(new Conta(num, 0));
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

}
