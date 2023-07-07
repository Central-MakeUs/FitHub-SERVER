package fithub.app.service;

import java.io.IOException;
import java.net.MalformedURLException;

public interface AppleService {

    String userIdFromApple(String identityToken) throws IOException;
}
