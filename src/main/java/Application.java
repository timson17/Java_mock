import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;

@SpringBootApplication
@ComponentScan("src/main/java/com/example/myapp")
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostMapping("/test")
    public String updateAge(@RequestBody String json) {

        JSONObject jsonObject = new JSONObject(json); // Парсим JSON строку

        jsonObject.put("age", 100); // Изменяем значение под ключем "age" на 100

        return jsonObject.toString();
    }
}
