package service;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(new File("./src/resources/AutoSave.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}