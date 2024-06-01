package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryTaskManagerTest<T extends TaskManager> extends TaskManagerTest<FileBackedTasksManager> {
    private File file;
    private FileBackedTasksManager fileBackedTasksManager;

    @BeforeEach
    public void startMethod() {
        file = new File("./src/resources/AutoSave.csv");
        setFileBackedTasksManager(new FileBackedTasksManager(file));
        taskManager = new FileBackedTasksManager(file);
        task = new Task("Задача_1", "Описание задачи_1",
                LocalDateTime.of(2025, 5, 5, 2, 2), 30);
        task.setId(1);
        taskManager.createTask(task);
        epic = new Epic("Эпик_1", "Описание эпика_1");
        epic.setId(2);
        taskManager.createEpic(epic);
        subtask = new Subtask("Сабтаска", "Описание сабтаски_1",
                LocalDateTime.of(2025, 2, 5, 2, 2), 30, epic.getId());
        subtask.setId(3);
        taskManager.createSubtask(subtask);
    }

    @Test
    public void loadFromFileTest() {
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertFalse(taskManager.tasks.isEmpty());
        assertFalse(taskManager.subtasks.isEmpty());
        assertFalse(taskManager.epics.isEmpty());
    }

    public FileBackedTasksManager getFileBackedTasksManager() {
        return fileBackedTasksManager;
    }

    public void setFileBackedTasksManager(FileBackedTasksManager fileBackedTasksManager) {
        this.fileBackedTasksManager = fileBackedTasksManager;
    }
}
