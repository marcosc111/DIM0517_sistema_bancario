package frontend;

import backend.ContaManager;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ContaCommand {

    @ShellMethod("Para cadastrar uma nova conta")
    public String cadastro(@ShellOption int numConta) {
        var r = ContaManager.getInstance( ContaManager.DEFAULT_JSON_FILE_PATH ).addConta(numConta);
        return r
                ? "Nova conta (num = " + numConta + ") cadastrada!"
                : "Ação não permitida: número de conta já cadastrado!";
    }

    @ShellMethod("Para verificar o saldo de uma conta")
    public String saldo(@ShellOption int numConta) {
        var c = ContaManager.getInstance( ContaManager.DEFAULT_JSON_FILE_PATH ).getConta(numConta);
        return c != null
                ? "O saldo da conta " + numConta + " é: R$" + c.getSaldo()
                : "Conta não existe!";
    }

    @ShellMethod("Para creditar uma conta")
    public String credito(@ShellOption int numConta, @ShellOption float valor) {
        return "Creditando R$" + valor + " na conta " + numConta;
    }

    @ShellMethod("Para debitar uma conta")
    public String debito(@ShellOption int numConta, @ShellOption float valor) {
        return "Debitando R$" + valor + " na conta " + numConta;
    }

    @ShellMethod("Para fazer uma transferência entre contas")
    public String transferencia(@ShellOption int numContaOrigem, @ShellOption int numContaDestino, @ShellOption float valor) {
        return "Transferindo R$" + valor + " da conta " + numContaOrigem + " para a conta " + numContaDestino;
    }

    @ShellMethod("Para listar todas as contas")
    public String listar() {
        var numeroTodasContas = ContaManager.getInstance( ContaManager.DEFAULT_JSON_FILE_PATH ).getNumeroTodasContas();
        StringBuilder sb = new StringBuilder();
        numeroTodasContas.forEach(n -> sb.append("" + n + "\n"));
        return numeroTodasContas.isEmpty()
                ? "Não há contas cadastradas!"
                : sb.toString();
    }

}
