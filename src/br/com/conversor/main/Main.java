package br.com.conversor.main;

import br.com.conversor.convert.Converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Seja bem-vindo/a ao Conversor de Moedas! \n");

        Scanner scanner = new Scanner(System.in);

        int option;
        do {
            System.out.println("1) Dólar => Peso argentino");
            System.out.println("2) Peso argentino => Dólar");
            System.out.println("3) Dólar => Real brasileiro");
            System.out.println("4) Real brasileiro => Dólar");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            System.out.println("7) Sair");
            System.out.println("Escolha uma opção válida:");
            option = scanner.nextInt();

            if (option >= 1 && option < 6) {
                System.out.println("Digite o valor para a conversão: ");
                double valor = scanner.nextDouble();
                Converter.convert(option, valor);
            } else if (option != 7) {
                System.out.println("Opção inválida!");
            }
        }

        while (option != 7);

        System.out.println("Saindo...");
        scanner.close();

    }

}