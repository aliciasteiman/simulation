package cellsociety.configuration;

import java.io.IOException;

public class ResourceKeyException extends IllegalArgumentException {
    public ResourceKeyException(String s) {
        super(s);
    }
//    TODO: potentially have this extend a missingresourceexception instead but need to figure out messaging
}
