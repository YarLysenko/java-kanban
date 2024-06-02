package server;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import exception.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    protected final String apikey;
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public KVTaskClient(String url) {
        this.url = url;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/register"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apikey = response.body();
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            throw new ManagerSaveException("Ошибка");
        }
    }

    public void put(String key, String json) {
        try {
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(body)
                    .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apikey))
                    .build();

            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() != 200) {
                throw new RuntimeException("");
            }
        } catch (IOException | InterruptedException | ManagerSaveException exception) {
            throw new ManagerSaveException("Во время выполнения запроса возникла ошибка.");
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apikey);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем, успешно ли обработан запрос
            if (response.statusCode() == 200) {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
                    System.out.println("Ответ от сервера не соответствует ожидаемому.");
                    return response.body();
                }
            }
        } catch (IOException | InterruptedException exception) {
            throw new ManagerSaveException("Во время выполнения запроса возникла ошибка.");
        }
        return null;
    }
}