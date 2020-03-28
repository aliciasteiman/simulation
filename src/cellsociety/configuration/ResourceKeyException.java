package cellsociety.configuration;

import java.io.IOException;

/**
 * This exception is used to handle exceptions thrown due to querying a key that does not exist in a properties file
 */
public class ResourceKeyException extends IllegalArgumentException {
    public ResourceKeyException(String s) {
        super(s);
    }
}
