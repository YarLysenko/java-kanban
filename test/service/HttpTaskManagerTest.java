package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;
    @BeforeEach
    void startMethod() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        super.taskManager = Managers.getDefault();
        task = new Task("Task", "описание таски",
                LocalDateTime.of(2025, 5, 5, 2, 2), 30);
        task.setId(1);
        taskManager.createTask(task);
        epic = new Epic("epic", "Описание Эпика");
        epic.setId(2);
        taskManager.createEpic(epic);
        subtask = new Subtask("subtask", "описание сабтаски",
                LocalDateTime.of(2025, 2, 5, 2, 2), 30, epic.getId());
        subtask.setId(3);
        taskManager.createSubtask(subtask);
    }
    @AfterEach
    void stop() {
        kvServer.stop();
    }
    @Test
    void load() {
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.getEpicById(epic.getId());
        HttpTaskManager httpTaskManagerLoad = new HttpTaskManager("http://localhost:8078", true);
        assertEquals(taskManager.getAllTasks().toString(), httpTaskManagerLoad.getAllTasks().toString());
        assertEquals(taskManager.getAllSubtasks().toString(), httpTaskManagerLoad.getAllSubtasks().toString());
        assertEquals(taskManager.getAllEpics().toString(), httpTaskManagerLoad.getAllEpics().toString());
        assertEquals(taskManager.getPrioritizedTasks().toString(), httpTaskManagerLoad.getPrioritizedTasks().toString());
    }
}