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
    private static final String INSTANCE_ID_1 = "instance109385";
    private static final String TOKEN_1 = "6s3u9jm7tt71i2e4";
    private static final String INSTANCE_ID_2 = "instance109487";
    private static final String TOKEN_2 = "rne3v2cxu3cz5vxe"; 
    
    private static final int MESSAGE_LIMIT = 10;
    private int messageCount = 0;
    private boolean useFirstAccount = true;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public String sendMessages(List<String> phones, String message) {
        StringBuilder responseText = new StringBuilder();

        for (String phone : phones) {
            responseText.append(sendMessage(phone, message)).append("\n");
        }

        return responseText.toString();
    }

    private synchronized String sendMessage(String phone, String message) {
        String instanceId = useFirstAccount ? INSTANCE_ID_1 : INSTANCE_ID_2;
        String token = useFirstAccount ? TOKEN_1 : TOKEN_2;
        String apiUrl = "https://api.ultramsg.com/" + instanceId + "/messages/chat";
        
        JSONObject body = new JSONObject();
        body.put("token", token);
        body.put("to", phone);
        body.put("body", message);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        
        String result = "Mensaje enviado a " + phone + " usando " + (useFirstAccount ? "cuenta 1" : "cuenta 2") + ": " + response.getBody();
        
        messageCount++;
        if (messageCount >= MESSAGE_LIMIT) {
            useFirstAccount = !useFirstAccount;
            messageCount = 0;
            result += "\nCambiando a " + (useFirstAccount ? "cuenta 1" : "cuenta 2");
        }

        return result;
    }
}

