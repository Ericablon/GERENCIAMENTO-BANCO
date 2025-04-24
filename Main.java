import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ContaBancaria {
    private static int proximoNumero = 1;
    private final String nome;
    private final String sobrenome;
    private final String cpf;
    private double saldo;
    private final int numeroConta;
    private final List<String> historico;

    public ContaBancaria(String nome, String sobrenome, String cpf) {
        this.historico = new ArrayList<>();
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = validarCPF(cpf);
        this.numeroConta = proximoNumero++;
    }

    private String validarCPF(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF inválido! Deve conter 11 dígitos");
        }
        return cpfLimpo;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
            registrarTransacao("Depósito", valor);
            System.out.printf("\nDepósito de R$%.2f realizado!%n", valor);
        } else {
            System.out.println("\nValor inválido para depósito");
        }
    }

    public void sacar(double valor) {
        if (valor > 0 && saldo >= valor) {
            saldo -= valor;
            registrarTransacao("Saque", -valor);
            System.out.printf("\nSaque de R$%.2f realizado!%n", valor);
        } else {
            System.out.println("\nSaldo insuficiente ou valor inválido");
        }
    }

    private void registrarTransacao(String tipo, double valor) {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        historico.add(String.format("[%s] %s: R$%.2f", data, tipo, valor));
    }

    public void exibirDados() {
        System.out.println("\n=== Dados da Conta ===");
        System.out.println("Titular: " + nome + " " + sobrenome);
        System.out.println("CPF: " + formatarCPF());
        System.out.println("Número da Conta: " + numeroConta);
        System.out.printf("Saldo Disponível: R$%.2f%n", saldo);
    }

    private String formatarCPF() {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Sistema Bancário ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Sobrenome: ");
        String sobrenome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        try {
            ContaBancaria conta = new ContaBancaria(nome, sobrenome, cpf);
            exibirMenu(conta, scanner);
        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void exibirMenu(ContaBancaria conta, Scanner scanner) {
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Consultar Saldo");
            System.out.println("2. Realizar Depósito");
            System.out.println("3. Realizar Saque");
            System.out.println("4. Exibir Dados da Conta");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida!");
                continue;
            }

            switch (opcao) {
                case 1:
                    conta.exibirDados();
                    break;
                case 2:
                    System.out.print("Valor do depósito: R$");
                    double deposito = scanner.nextDouble();
                    scanner.nextLine();
                    conta.depositar(deposito);
                    break;
                case 3:
                    System.out.print("Valor do saque: R$");
                    double saque = scanner.nextDouble();
                    scanner.nextLine();
                    conta.sacar(saque);
                    break;
                case 4:
                    conta.exibirDados();
                    break;
                case 5:
                    System.out.println("Obrigado por usar nosso sistema! Até breve.");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
