package fithub.app.firebase.service;

import java.io.IOException;
import java.util.Map;

public interface FireBaseService {

    public void sendMessageToV1(String targetToken, String title, String body, String targetView, String targetPK) throws IOException;
    public void sendMessageToV2(String targetToken, String title, String body, String targetView, String targetPK) throws IOException;
    public void sendMessageToV3(String targetToken, String title, String body, String targetView, String targetPK) throws IOException;
}
