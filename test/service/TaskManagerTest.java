package service;

import model.Epic;
import model.StatusType;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;


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
    public void checkStatusIsEmpty() {
        taskManager.deleteAllSubtasks();
        assertEquals(StatusType.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusNew() {
        epic.setStatus(StatusType.DONE);
        assertEquals(StatusType.DONE, epic.getStatus());
    }


    @Test
    public void checkStatusNewWhenEpicIsEmpty() {
        taskManager.deleteAllEpics();
        assertEquals(StatusType.NEW, epic.getStatus());
    }

    @Test
    public void checkStatusInProgress() {
        subtask.setStatus(StatusType.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        assertEquals(StatusType.IN_PROGRESS, epic.getStatus());

    }

    @Test
    public void checkStatusDone() {
        subtask.setStatus(StatusType.DONE);
        taskManager.updateSubtask(subtask);
        assertEquals(StatusType.DONE, epic.getStatus());
    }

    @Test
    public void addNewTask() {
        Integer taskId = task.getId();
        assertTrue(taskId.equals(1), "неверный индфикатор задачи");
        assertNotNull(task, "таска пустая");
        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void addNewSubtask() {
        Integer subtaskId = subtask.getId();
        assertTrue(subtaskId.equals(3), "неверный индификатор задачи");
        assertNotNull(subtask, "сабтаска пустая");
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertNotNull(subtasks, "Задачи не возращаются");
        assertEquals(1, subtasks.size(), "Неверное колличество задач");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void addNewEpic() {
        Integer epicId = epic.getId();
        assertTrue(epicId.equals(2), "неверный индификатор задач");
        assertNotNull(epic, "таска пустая");
        List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Задачи не возращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic, epics.get(0));
    }

    @Test
    public void updateTask() {
        task.setStatus(StatusType.NEW);
        taskManager.updateTask(task);
        assertEquals(StatusType.NEW, task.getStatus());
        task.setStatus(StatusType.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(StatusType.IN_PROGRESS, task.getStatus());
    }

    @Test
    public void updateSubtask() {
        subtask.setStatus(StatusType.NEW);
        taskManager.updateSubtask(subtask);
        assertEquals(StatusType.NEW, subtask.getStatus());
        subtask.setStatus(StatusType.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        assertEquals(StatusType.IN_PROGRESS, subtask.getStatus());
    }

    @Test
    public void updateEpic() {
        epic.setStatus(StatusType.NEW);
        taskManager.updateEpic(epic);
        assertEquals(StatusType.NEW, epic.getStatus(), "Начальные  статусы не совпадают");
        subtask.setStatus(StatusType.IN_PROGRESS);
        epic.setStatus(StatusType.IN_PROGRESS);
        taskManager.updateEpic(epic);
        assertEquals(StatusType.IN_PROGRESS, epic.getStatus(), "Статусы не совпадают");
    }

    @Test
    public void getTaskById() {
        assertNotNull(task, "таска пустая");
        assertEquals(task, taskManager.getTaskById(task.getId()), "метод возращает нету таску");
    }

    @Test
    public void getSubtaskById() {
        assertNotNull(subtask, "сабтаска пустая");
        assertEquals(subtask, taskManager.getSubtaskById(subtask.getId()));
    }

    @Test
    public void getEpicById() {
        assertNotNull(epic, "эпик пустой");
        assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    public void getSubtaskByEpic() {
        assertNotNull(epic, "эпик пустой");
        assertNotNull(subtask, "сабтаска пустая");
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        subtasksByEpic.add(subtask);
        assertArrayEquals(subtasksByEpic.toArray(), taskManager.getSubtasksByEpic(epic).toArray(),
                "неверная сабтаска");
    }

    @Test
    public void getTasks() {
        ArrayList<Task> tasks = new ArrayList<>(taskManager.getAllTasks());
        for (Task task1 : tasks) {
            assertNotNull(task1, "в списке есть пустая задача");
        }
        ArrayList<Task> taskArrayList = new ArrayList<>();
        taskArrayList.add(task);
        assertArrayEquals(taskArrayList.toArray(), taskManager.getAllTasks().toArray());
    }

    @Test
    public void getSubtask() {
        ArrayList<Subtask> subtasks = new ArrayList<>(taskManager.getAllSubtasks());
        for (Subtask subtask1 : subtasks) {
            assertNotNull(subtask1, "в списке пустая задача");
        }
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>();
        subtaskArrayList.add(subtask);
        assertArrayEquals(subtaskArrayList.toArray(), taskManager.getAllSubtasks().toArray());
    }

    @Test
    public void getEpic() {
        ArrayList<Epic> epics = new ArrayList<>(taskManager.getAllEpics());
        for (Epic epic1 : epics) {
            assertNotNull(epic1, "в списке есть пустая задача");
        }
        ArrayList<Epic> epicArrayList = new ArrayList<>();
        epicArrayList.add(epic);
        assertArrayEquals(epicArrayList.toArray(), taskManager.getAllEpics().toArray());
    }

    @Test
    public void deleteTask() {
        assertEquals(1, taskManager.getTaskById(1).getId(), "задачи с таким индификатором не существует");
        taskManager.deleteTask(task);
        assertTrue(taskManager.getAllTasks().isEmpty(), "задача не удалена");

    }

    @Test
    public void deleteSubtask() {
        assertEquals(3, taskManager.getSubtaskById(3).getId(), "задачи с таким id не существует");
        taskManager.deleteSubtask(subtask);
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "задача не удалена");
    }

    @Test
    public void deleteEpic() {
        assertEquals(2, taskManager.getEpicById(2).getId(), "задачи с таким индификатором не существует");
        taskManager.deleteEpic(epic);
        assertTrue(taskManager.getAllEpics().isEmpty(), "задача не удалена");
    }

    @Test
    public void deleteTasks() {
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "список не удалился");
    }

    @Test
    public void deleteSubtasks() {
        taskManager.deleteAllSubtasks();
        assertEquals(StatusType.NEW, epic.getStatus());
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "список не удалился");
    }

    @Test
    public void deleteEpics() {
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "список не удалился");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "сабтаски эпиков не удалилилсь");
    }


    @Test
    public void getHistoryCheck() {
        taskManager.getTaskById(task.getId());
        taskManager.getSubtaskById(subtask.getId());
        taskManager.getTaskById(task.getId());
        List<Task> history = List.of(subtask, task);
        assertArrayEquals(history.toArray(), taskManager.getHistory().toArray());
    }



    @Test
    public void updateEpicTime() {
        Subtask subtask1 = new Subtask("Сабтаск111", "miniNigga",
                LocalDateTime.of(2025,2,5,2,2),30, 2);
        taskManager.createTask(subtask1);
        LocalDateTime str = subtask1.getStartTime();
        assertEquals(epic.getEndTime(), str.plusMinutes(subtask1.getDuration()), "Неправильное обновление конца эпика");

    }

}