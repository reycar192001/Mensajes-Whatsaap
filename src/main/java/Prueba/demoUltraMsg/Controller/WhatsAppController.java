package Prueba.demoUltraMsg.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Prueba.demoUltraMsg.Service.UltraMsgService;

@RestController
@RequestMapping("/send-whatsapp")
public class WhatsAppController {

    @Autowired
    private UltraMsgService ultraMsgService;
    
    private static final Logger Logger = org.slf4j.LoggerFactory.getLogger(WhatsAppController.class);

    @PostMapping("/send-fixed")
    public ResponseEntity<Map<String, Object>> sendFixedMessages(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        int count = Integer.parseInt(request.get("count"));

        List<String> phoneNumbers = Arrays.asList(
            "+51931053418",
            "+51974778060",
            "+51902275242"
        );

        Logger.info("Enviando mensaje: '{}' a los números: {} ({} veces)", message, phoneNumbers, count);

        Map<String, Object> response = ultraMsgService.sendMessages(phoneNumbers, message, count);

        return ResponseEntity.ok(response);
    }
}



