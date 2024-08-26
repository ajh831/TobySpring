package tobyspring.hellospring.api;

import java.io.IOException;
import java.net.URI;

public interface ApiExcutor {
    String execute(URI uri) throws IOException;
}
