package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.OffsetDateTime;

public class JsonTools {
    @PostMapping("/test")
    public String getPostRequest(@RequestBody String json) {

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
    public String getDeleteRequest(@RequestBody String json) {

        JSONObject jsonObject = new JSONObject(json); // Парсим JSON строку
        if (!jsonObject.has("delete")) return getWithoutDeleteFieldError();
        // вытащил алерт индекс и тикер нейм
        String alertIndex = jsonObject.getJSONObject("delete").opt("alertIndex").toString();
        String tickerName = jsonObject.getJSONObject("delete").opt("tickerName").toString();

        // обновляем текущее время в ответе
        String[] currentTime_raw = OffsetDateTime.now().toString().split("\\.");
        String currentTime = currentTime_raw[0];
        JSONObject result = jsonObject.put("lastUpdate",currentTime);

        // длина тикеров
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
    private String getWithoutDeleteFieldError() {
        JSONObject result = new JSONObject();
        result.put("status", "error");
        result.put("message", "Передан некорректный action - update");
        return result.toString();
    }
}
