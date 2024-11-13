package br.com.conversor.convert;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class Converter {
    private static String API_KEY;
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            API_KEY = properties.getProperty("API_KEY");

            if (API_KEY == null || API_KEY.isEmpty()) {
                System.out.println("Erro: chave de API não encontrada no arquivo config.properties.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar a chave de API: " + e.getMessage());
        }
    }

    public static void convert(int option, double valor) {

        String fromCurrency = "";
        String toCurrency = "";

        switch (option) {
            case 1 -> { fromCurrency = "USD"; toCurrency = "ARS"; }
            case 2 -> { fromCurrency = "ARS"; toCurrency = "USD"; }
            case 3 -> { fromCurrency = "USD"; toCurrency = "BRL"; }
            case 4 -> { fromCurrency = "BRL"; toCurrency = "USD"; }
            case 5 -> { fromCurrency = "USD"; toCurrency = "COP"; }
            case 6 -> { fromCurrency = "COP"; toCurrency = "USD"; }
        }

        String link = API_URL + API_KEY + "/latest/" + fromCurrency;


        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(link))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String json = response.body();
                JsonObject jsonResponse = JsonParser.parseString(json).getAsJsonObject();

                if (jsonResponse.has("conversion_rates") && jsonResponse.get("conversion_rates").isJsonObject()) {
                    JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");

                    if (rates.has(toCurrency)) {
                        double exchangeRate = rates.get(toCurrency).getAsDouble();
                        double convertedValue = valor * exchangeRate;
                        System.out.printf("Valor convertido: %.2f %s\n", convertedValue, toCurrency);
                    } else {
                        System.out.println("A moeda de destino não foi encontrada na resposta da API.");
                    }
                } else {
                    System.out.println("A resposta JSON não contém o objeto 'conversion_rates'. Resposta completa:");
                    System.out.println(json);
                }
            } else {
                System.out.println("Erro na requisição. Código de resposta: " + response.statusCode());
                System.out.println("Resposta completa: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao obter a taxa de câmbio: " + e.getMessage());
        }
    }
}
