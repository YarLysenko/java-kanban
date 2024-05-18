package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTests {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp(){
        taskManager = new InMemoryTaskManager();
    }



    @Test
    void testUtilityClassReturnsInitializedManagers() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager);
    }

    @Test
    void testInMemoryTaskManagerAddsAndFindsTasksById() {

        Task task = new Task("Задача 1", "Описание задачи 1");
        taskManager.createTask(task);
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void testNoIdConflictBetweenGivenAndGeneratedIds() {

        Task task = new Task("Задача 1", "Описание задачи 1");
        task.setId(1);
        taskManager.createTask(task);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.createTask(task2);
        assertNotEquals(task.getId(), task2.getId());
    }

    @Test
    void testTaskRemainsUnchangedWhenAddedToManager() {
        Task originalTask = new Task("Задача 1", "Описание задачи 1");
        originalTask.setId(1);
        taskManager.createTask(originalTask);
        Task retrievedTask = taskManager.getTaskById(1);
        assertEquals(originalTask, retrievedTask);
    }


}