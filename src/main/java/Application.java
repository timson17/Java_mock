import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.OffsetDateTime;

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
        JSONObject addValue = jsonObject.getJSONObject("add");

        // Добавляем новый элемент в массив "alerts" для соответствующего "ticker"
        JSONArray tickers = jsonObject.getJSONObject("info").getJSONArray("tickers");

        for (int i = 0; i < tickers.length(); i++) {
            JSONObject ticker = tickers.getJSONObject(i);

            if (ticker.getString("ticker").equals(addValue.getString("name"))) {
                JSONArray alerts = ticker.getJSONArray("alerts");
                JSONObject newAlert = new JSONObject();
                newAlert.put("timeFrame", addValue.getInt("timeFrame"));
                newAlert.put("percent", addValue.getInt("percent"));
                alerts.put(newAlert);
            }
        }

        // удаляем поле add
        jsonObject.remove("add");
        
        // обновляем текущее время в ответе
        String[] currentTime_raw = OffsetDateTime.now().toString().split("\\.");
        String currentTime = currentTime_raw[0];
        JSONObject result = jsonObject.put("lastUpdate",currentTime);

        return result.toString();
    }
    @DeleteMapping("/test")
    public String deleteMethod(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json); // Парсим JSON строку
        // вытащил алерт индекс и тикер нейм
        String alertIndex = jsonObject.getJSONObject("delete").opt("alertIndex").toString();
        String tickerName = jsonObject.getJSONObject("delete").opt("tickerName").toString();

        // обновляем текущее время в ответе
        String[] currentTime_raw = OffsetDateTime.now().toString().split("\\.");
        String currentTime = currentTime_raw[0];
        JSONObject result = jsonObject.put("lastUpdate",currentTime);

        // длинна тикеров
        JSONArray countTickers = jsonObject.getJSONObject("info").getJSONArray("tickers");

        // удаляем тикер
        for (int i = 0; i < countTickers.length(); i++) {
            JSONObject ticker = countTickers.getJSONObject(i);

            if (ticker.toString().contains(tickerName)) {
                JSONArray alerts = ticker.getJSONArray("alerts");
                alerts.remove(Integer.parseInt(alertIndex)); // Удаляем из алертов нужный тикер
            }
        }
        result.remove("delete"); // Удаляем ключ delete и его значения
        return result.toString();
    }
}
