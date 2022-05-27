package backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ContaManager {
    private static ContaManager instance;
    public final static String JSON_FILE = "_data.json";

    public List<Conta> contas;

    private ContaManager() {
        loadContas();
    }

    private void loadContas() {
        String jsonStr = readJSONFile();
        Type contasListType = new TypeToken<ArrayList<Conta>>(){}.getType();
        contas = new Gson().fromJson(jsonStr, contasListType);
    }

    public String readJSONFile() {
        Path dir = Paths.get(JSON_FILE);
        StringBuilder sb = new StringBuilder();
        Stream<String> content = null;
        try {
            content = Files.lines(dir);
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

        Path path = Paths.get(JSON_FILE);
        String jsonString = new Gson().toJson(contas);
        try {
            Files.write(path, jsonString.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ContaManager getInstance() {
        if (instance == null) {
            instance = new ContaManager();
        }
        return instance;
    }

}
