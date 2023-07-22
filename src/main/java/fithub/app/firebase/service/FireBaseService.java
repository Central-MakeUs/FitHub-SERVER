package fithub.app.firebase.service;

import java.io.IOException;

public interface FireBaseService {

    public void sendMessageTo(String targetToken, String title, String body) throws IOException;
}
