package chat;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Flow;

@Slf4j
public class D240526_C01_Ollama_Request {


    public static void sendOllamaRequest(String model, String prompt) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String payload = "{ \"model\": \"" + model + "\", \"prompt\": \"" + prompt + "\" }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:3000/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        System.out.println(response.body());
                    } else {
                        System.out.println("요청 실패: " + response.statusCode());
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                }).join();
    }

    public static void main(String[] args) {
        try {
            String model = "eeveq5";
            String prompt = "프로그래밍 언어의 종류는 무엇인가요?";
            sendOllamaRequest(model, prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
