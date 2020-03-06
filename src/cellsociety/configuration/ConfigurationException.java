package cellsociety.configuration;

public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String message, Object ... values) {
        super(String.format(message, values));
    }
}
