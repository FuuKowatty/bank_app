package pl.bartoszmech.BankApp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRates {
    @JsonProperty("conversion_rates")
    private Map<String, Double> conversionRates;
    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }
}
