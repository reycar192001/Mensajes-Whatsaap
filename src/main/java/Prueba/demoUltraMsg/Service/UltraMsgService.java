package Prueba.demoUltraMsg.Service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UltraMsgService {
    private static final String INSTANCE_ID = "instance109385"; // Reemplaza con tu ID
    private static final String TOKEN = "6s3u9jm7tt71i2e4"; // Reemplaza con tu Token
    private static final String API_URL = "https://api.ultramsg.com/" + INSTANCE_ID + "/messages/chat";

    public void sendMessages(List<String> phones, String message) {
        RestTemplate restTemplate = new RestTemplate();

        for (String phone : phones) {
            JSONObject body = new JSONObject();
            body.put("token", TOKEN);
            body.put("to", phone);
            body.put("body", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            System.out.println("Mensaje enviado a " + phone + ": " + response.getBody());
        }
    }
}