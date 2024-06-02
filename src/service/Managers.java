package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.HttpTaskServer;

import java.io.File;

public class Managers {
    public static TaskManager getDefaultFromFile() {
        return FileBackedTasksManager.loadFromFile(new File("./src/resources/AutoSave.csv"));
    }

    public static HttpTaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}