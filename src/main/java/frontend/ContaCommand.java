package frontend;

import backend.Conta;
import backend.ContaManager;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ContaCommand {

    @ShellMethod("Para cadastrar uma nova conta")
    public String cadastro(@ShellOption int numConta) {
        var r = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).addConta(numConta);
        return r
                ? "Nova conta (num = " + numConta + ") cadastrada!"
                : "Ação não permitida: número de conta já cadastrado!";
    }

    @ShellMethod("Para verificar o saldo de uma conta")
    public String saldo(@ShellOption int numConta) {
        var c = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).getConta(numConta);
        return c != null
                ? "O saldo da conta " + numConta + " é: R$" + c.getSaldo()
                : "Conta não existe!";
    }

    @ShellMethod("Para creditar uma conta")
    public String credito(@ShellOption int numConta, @ShellOption float valor) {
        var b = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).credito(numConta, valor);
        Conta c = null;
        if (b)
            c = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).getConta(numConta);
        return b
                ? "Creditando R$" + valor + " na conta " + numConta + "! Novo saldo: " + c.getSaldo() + "!"
                : "Conta não existe!";
    }

    @ShellMethod("Para debitar uma conta")
    public String debito(@ShellOption int numConta, @ShellOption float valor) {
        var b = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).debito(numConta, valor);
        Conta c = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).getConta(numConta);
        return b
                ? "Debitando R$" + valor + " na conta " + numConta + "! Novo saldo: " + c.getSaldo() + "!"
                :
                    c == null
                    ? "Conta não existe!"
                    : "Saldo insuficiente: " + c.getSaldo();
    }

    @ShellMethod("Para fazer uma transferência entre contas")
    public String transferencia(@ShellOption int numContaOrigem, @ShellOption int numContaDestino, @ShellOption float valor) {
        var b = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).transferencia(numContaOrigem, numContaDestino, valor);
        Conta c1 = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).getConta(numContaOrigem);
        Conta c2 = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).getConta(numContaDestino);
        return b
                ? "Transferindo R$" + valor + " da conta " + numContaOrigem + " para a conta " + numContaDestino + "!\n"
                    + "Novo saldo conta " + c1.getId() + ": " + c1.getSaldo() + "\n"
                    + "Novo saldo conta " + c2.getId() + ": " + c2.getSaldo() + "\n"
                :
                    c1 == null || c2 == null
                    ? "Operaçao invalida! Verifique o numero das contas!"
                    : "Saldo insuficiente na conta de origem: " + c1.getSaldo();
    }

    @ShellMethod("Para listar todas as contas")
    public String listar() {
        var numeroTodasContas = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).getNumeroTodasContas();
        StringBuilder sb = new StringBuilder();
        numeroTodasContas.forEach(n -> sb.append("" + n + "\n"));
        return numeroTodasContas.isEmpty()
                ? "Não há contas cadastradas!"
                : sb.toString();
    }

}
