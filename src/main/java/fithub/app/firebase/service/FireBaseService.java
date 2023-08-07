package fithub.app.firebase.service;

import java.io.IOException;
import java.util.Map;

public interface FireBaseService {
    public void sendMessageTo(String targetToken, String title, String body, String targetView, String targetPK, String targetNotification) throws IOException;
    public void sendMessageToV2(String targetToken, String title, String body, String targetView, String targetPK, String targetNotification,String targetImage) throws IOException;
}
