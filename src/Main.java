import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class Main {
    private static final String URL = DataAPI.getURL();
    private static final String API_KEY = DataAPI.getApiKey();
    private static final String MODEL_URI = DataAPI.getModelURI();
    private static final OkHttpClient httpClient = new OkHttpClient();

    public static void main(String... args) {
        try {
            String responseJSON = run();
            System.out.println("Response: " + responseJSON);
            System.out.println("\nResponse Text: " + extractJSONText(responseJSON));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String run() throws IOException {
        int STATUS_CODE;
        String RESPONSE_BODY;

        JSONObject requestBodyJSON = new JSONObject();
        requestBodyJSON.put("modelUri", MODEL_URI);
        JSONObject completionOptionsJSON = new JSONObject();
        completionOptionsJSON.put("stream", false);
        completionOptionsJSON.put("temperature", 0.6);
        completionOptionsJSON.put("maxTokens", "2000");
        requestBodyJSON.put("completionOptions", completionOptionsJSON);

        JSONArray messagesArray = new JSONArray();

        JSONObject message1 = new JSONObject();
        message1.put("role", "system"); // Поведение
        message1.put("text", "Ты умный ассистент");
        messagesArray.add(message1);
        JSONObject message2 = new JSONObject();
        message2.put("role", "user");   // Запрос
        message2.put("text", "Привет! Какими науками занимался Альберт Эйнштейн?");
        messagesArray.add(message2);
        // assistant - ответ на запрос

        requestBodyJSON.put("messages", messagesArray);

        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                requestBodyJSON.toString());
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Authorization", "Api-Key " + API_KEY)
                .build();

        try (Response response = httpClient.newCall(request).execute()){
            STATUS_CODE = response.code();
            RESPONSE_BODY = response.body().string();
            if (STATUS_CODE == 200) {
                return RESPONSE_BODY;
            }
        }
        throw new IOException("ERROR: "+RESPONSE_BODY);
    }

    private static String extractJSONText(String txt) {
        JSONObject jsonObject = JSONObject.parseObject(txt);
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray alternatives = result.getJSONArray("alternatives");
        JSONObject firstAlternative = alternatives.getJSONObject(0);
        JSONObject message = firstAlternative.getJSONObject("message");
        return message.get("text").toString();
    }
}