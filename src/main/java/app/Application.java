package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import utils.JsonTools;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {SpringApplication.run(Application.class, args);
    }
    @RestController
    public class JsonController {

        private final JsonTools jsonTools = new JsonTools();
        @PostMapping("/test")
        public String updateAge(@RequestBody String json) {
            return jsonTools.getPostRequest(json);
        }

        @DeleteMapping("/test")
        public String deleteMethod(@RequestBody String json) {
            return jsonTools.getDeleteRequest(json);

        }
    }
}
