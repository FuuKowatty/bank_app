package pl.bartoszmech.BankApp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRates {
    @JsonProperty("conversion_rates")
    private Map<String, Double> conversionRates;
    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public Entry<String, Double> getConversionRate(String symbol) {
        return conversionRates
                .entrySet()
                .stream()
                .filter(r -> Objects.equals(r.getKey(), symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("provided symbol is not valid"));
    }
}
