package frontend;

import backend.Conta;
import backend.ContaManager;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ContaCommand {

    @ShellMethod("Para cadastrar uma nova conta (conta normal = 0, conta bonus = 1)")
    public String cadastro(@ShellOption int numConta, @ShellOption int tipoConta) {

        if ( !Conta.checkTipoConta(tipoConta) )
            return "Tipo conta inválido!";

        var r = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).addConta(numConta, tipoConta);
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

        if (valor < 0)
            return "Valor negativo!";

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

        if (valor < 0)
            return "Valor negativo!";

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

        if (valor < 0)
            return "Valor negativo!";

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
        numeroTodasContas.forEach((k, v) -> sb.append("num: " + k + " \t\t|\t\ttipo: " + v + "\n"));
        return numeroTodasContas.isEmpty()
                ? "Não há contas cadastradas!"
                : sb.toString();
    }

    @ShellMethod("Para render juros (exclusivo para conta poupança)")
    public String renderjuros(@ShellOption int numConta, @ShellOption float taxaJuros) {

        Conta c = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).getConta(numConta);

        if (c == null)
            return "Conta inexistente!";

        if (c.getTipoConta() != Conta.TIPO_CONTA_POUPANCA)
            return "Operação inválida! Conta " + numConta + " não é uma Conta Poupança!";

        ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).renderJuros(numConta, taxaJuros);
        c = ContaManager.getInstance(ContaManager.DEFAULT_JSON_FILE_PATH).getConta(numConta);
        return "Juros de " + taxaJuros + "% aplicados! Novo saldo: " + c.getSaldo();
    }

}
