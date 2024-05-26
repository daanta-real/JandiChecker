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
                .uri(new URI("http://localhost:11434/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<Void> response = client.sendAsync(request, HttpResponse.BodyHandlers.fromLineSubscriber(new LineSubscriber()))
                .join();

        if (response.statusCode() != 200) {
            System.out.println("Request failed: " + response.statusCode());
        }
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

class LineSubscriber implements Flow.Subscriber<ByteBuffer> {
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); // Request the first item
    }

    @Override
    public void onNext(ByteBuffer item) {
        String response = StandardCharsets.UTF_8.decode(item).toString();
        System.out.println(response);
        subscription.request(1); // Request the next item
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Response processing complete");
    }

}
