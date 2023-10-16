package pl.bartoszmech.BankApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.bartoszmech.BankApp.ExchangeRates;


@Service
public class CurrencyService {

    static Map<String, String> allowedCurrencies() {
        Map<String, String> countryToCurrency = new HashMap<>();

        countryToCurrency.put("1", "CNY - China");
        countryToCurrency.put("2", "GBP - United Kingdoms");
        countryToCurrency.put("3", "PLN - Poland");
        countryToCurrency.put("4", "USD - United States");
        countryToCurrency.put("5", "CZK - Czech Republic");

        return countryToCurrency;
    }

    public void getExchangeRates() throws IOException {
        ExchangeRates exchangeRates = fetchRates();
        System.out.println(exchangeRates.getConversionRates());
    }

    private ExchangeRates fetchRates() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://v6.exchangerate-api.com/v6/3428fae301d832296fad9132/latest/EUR")
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (response.isSuccessful() && responseBody != null) {
                return new ObjectMapper().readValue(responseBody.string(), ExchangeRates.class);
            }
            throw new IOException("jd");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
