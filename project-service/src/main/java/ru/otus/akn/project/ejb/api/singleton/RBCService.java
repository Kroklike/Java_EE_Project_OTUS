package ru.otus.akn.project.ejb.api.singleton;

import javax.ejb.Remote;
import java.io.IOException;
import java.util.Map;

@Remote
public interface RBCService {

    Map<String, String> getFreshNews() throws IOException;

    Map<String, String> getFreshNewsOrNull() throws IOException;
}
