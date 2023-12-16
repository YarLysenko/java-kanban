package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int taskId = 0;

    private Integer createId() {
        return ++taskId;
    }

    @Override
    public void printTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    @Override
    public void printSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    @Override
    public void printEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    @Override
    public void createTask(Task task) {
        task.setId(createId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(createId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(createId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        subtasksId.add(subtask.getId());
        updateEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpic(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.setStatus(updateEpicStatus(epic));
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subtaskId);
            subtasksByEpic.add(subtask);
        }
        return subtasksByEpic;
    }

    @Override
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        addToHistory(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        addToHistory(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        addToHistory(epic);
        return epic;
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            epic.setStatus("NEW");
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksId().remove((Integer) subtask.getId());
            updateEpic(epic);
            subtasks.remove(subtask.getId());
        }
    }

    @Override
    public void deleteEpic(Epic epic) {
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epic.getId());
    }

    @Override
    public void deleteTask(Task task) {
        tasks.remove(task.getId());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private String updateEpicStatus(Epic epic) {
        ArrayList<String> subtasksStatus = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksId()) {
            String subtaskStatus = subtasks.get(subtaskId).getStatus();
            subtasksStatus.add(subtaskStatus);
        }
        if (subtasksStatus.isEmpty()) {
            return "NEW";
        } else if (!subtasksStatus.contains("NEW") && !subtasksStatus.contains("IN_PROGRESS")) {
            return "DONE";
        } else if (!subtasksStatus.contains("DONE") && !subtasksStatus.contains("IN_PROGRESS")) {
            return "NEW";
        } else {
            return "IN_PROGRESS";
        }
    }

    private void addToHistory(Task task) {
        historyManager.add(task);
    }
}
