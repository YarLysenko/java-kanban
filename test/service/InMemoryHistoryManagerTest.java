package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    public void firstMethodTest() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task = new Task("Task", "Описание");
        task.setId(1);
        epic = new Epic("Epic", "Описание");
        epic.setId(2);
        subtask = new Subtask("Subtask", "Описание", 2);
        subtask.setId(3);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
    }

    @Test
    public void addHistoryWhenTaskNull() {
        assertNotNull(task);
        assertNotNull(subtask);
        assertNotNull(epic);

    }

    @Test
    public void addTaskCheckIdAndName() {
        ArrayList<Task> historyElement = new ArrayList<>(inMemoryHistoryManager.getHistory());
        assertEquals(1, historyElement.get(0).getId());
        assertEquals("Task", historyElement.get(0).getName());
    }


    @Test
    public void addTaskCheckDuplicate() {
        inMemoryHistoryManager.add(task);
        List<Task> history = List.of(epic, subtask, task);
        assertArrayEquals(history.toArray(), inMemoryHistoryManager.getHistory().toArray());
    }

    @Test
    public void removeNodeStart() {
        inMemoryHistoryManager.remove(task.getId());
        for (Task task1 : inMemoryHistoryManager.getHistory()) {
            System.out.println(task1);
        }
        assertEquals(2, inMemoryHistoryManager.taskNodeMap.size());
    }

    @Test
    public void removeMidNode() {
        inMemoryHistoryManager.remove(epic.getId());
        for (Task task1 : inMemoryHistoryManager.getHistory()) {
            System.out.println(task1);
        }
        assertEquals(2, inMemoryHistoryManager.taskNodeMap.size());
    }

    @Test
    public void removeTailNode() {
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.remove(subtask.getId());
        for (Task task1 : inMemoryHistoryManager.getHistory()) {
            System.out.println(task1);
        }
        assertEquals(2, inMemoryHistoryManager.taskNodeMap.size());
    }
}
