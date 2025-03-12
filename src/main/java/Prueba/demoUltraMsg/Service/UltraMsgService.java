package Prueba.demoUltraMsg.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class UltraMsgService {

    private static final Logger LOGGER = Logger.getLogger(UltraMsgService.class.getName());

    @Value("${ultramsg.instance1.id}")
    private String INSTANCE_ID_1;

    @Value("${ultramsg.instance1.token}")
    private String TOKEN_1;

    @Value("${ultramsg.instance2.id}")
    private String INSTANCE_ID_2;

    @Value("${ultramsg.instance2.token}")
    private String TOKEN_2;

    private static final int MESSAGE_LIMIT = 4;

    private int account1Count = 0;
    private int account2Count = 0;
    private int totalMessageCount = 0;

    private final RestTemplate restTemplate = new RestTemplate();

    public synchronized Map<String, Object> sendMessages(List<String> phones, String message, int totalMessages) {
        StringBuilder responseText = new StringBuilder();
        int countMessage = 0;
        int failedMessages = 0;

        for (int i = 0; i < totalMessages; i++) {
            boolean useFirstAccount = account1Count < MESSAGE_LIMIT;
            boolean useSecondAccount = account2Count < MESSAGE_LIMIT;

            // Si ambas cuentas han alcanzado el límite, detener el envío
            if (!useFirstAccount && !useSecondAccount) {
                LOGGER.info("Se alcanzó el límite de mensajes en ambas cuentas.");
                failedMessages = totalMessages - countMessage;
                break;
            }

            // Selecciona la cuenta a usar
            boolean useAccount = useFirstAccount ? true : useSecondAccount ? false : true;

            String phone = phones.get(totalMessageCount % phones.size());

            String result = sendMessage(phone, message, useAccount);
            responseText.append(result).append("\n");
            countMessage++;
            totalMessageCount++;

            LOGGER.info("Total enviados: " + totalMessageCount + " | Cuenta 1: " + account1Count + " | Cuenta 2: " + account2Count);
        }

        // Agregar mensaje si hubo mensajes no enviados
        if (failedMessages > 0) {
            responseText.append("\nNo se enviaron ").append(failedMessages).append(" mensajes. Intente más tarde.");
        }
        LOGGER.info("Total de mensajes no enviados: " + failedMessages);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", responseText.toString());
        response.put("countMessage", countMessage);
        response.put("account1Count", account1Count);
        response.put("account2Count", account2Count);
        response.put("failedMessages", failedMessages);

        return response;
    }

    private synchronized String sendMessage(String phone, String message, boolean useFirstAccount) {
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
        
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(apiUrl, request, String.class);
        } catch (Exception e) {
            return "Error enviando mensaje a " + phone + ": " + e.getMessage();
        }

        String result = "Mensaje enviado a " + phone + " usando " + (useFirstAccount ? "cuenta 1" : "cuenta 2") + ": " + response.getBody();

        if (useFirstAccount) {
            account1Count++;
        } else {
            account2Count++;
        }

        return result;
    }

}



