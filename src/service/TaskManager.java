package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int taskId = 0;

    public void printTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void printSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    public void printEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    public void createTask(Task task) {
        task.setId(createId());
        tasks.put(task.getId(), task);
    }


    public void createEpic(Epic epic) {
        epic.setId(createId());
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(createId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        subtasksId.add(subtask.getId());
        updateEpic(epic);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateEpic(epic);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            ArrayList<String> subtasksStatus = new ArrayList<>();
            for (Integer subtaskId : epic.getSubtasksId()) {
                String subtaskStatus = subtasks.get(subtaskId).getStatus();
                subtasksStatus.add(subtaskStatus);
            }
            if (subtasksStatus.isEmpty()) {
                epic.setStatus("NEW");
                epics.put(epic.getId(), epic);
            } else if (!subtasksStatus.contains("NEW") && !subtasksStatus.contains("IN_PROGRESS")) {
                epic.setStatus("DONE");
                epics.put(epic.getId(), epic);
            } else if (!subtasksStatus.contains("DONE") && !subtasksStatus.contains("IN_PROGRESS")) {
                epic.setStatus("NEW");
                epics.put(epic.getId(), epic);
            } else {
                epic.setStatus("IN_PROGRESS");
                epics.put(epic.getId(), epic);
            }
        }
    }

    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subtaskId);
            subtasksByEpic.add(subtask);
        }
        return subtasksByEpic;
    }

    public Task getTaskById(Integer taskId) {
        return tasks.get(taskId);
    }

    public Subtask getSubtaskById(Integer subtaskId) {
        return subtasks.get(subtaskId);
    }

    public Epic getEpicById(Integer epicId) {
        return epics.get(epicId);
    }


    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.setSubtasksId(new ArrayList<>());
            epic.setStatus("NEW");
        }
        subtasks.clear();
    }


    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }


    public void deleteSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksId().remove((Integer) subtask.getId());
            updateEpic(epic);
            subtasks.remove(subtask.getId());
        }
    }

    public void deleteEpic(Epic epic) {
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epic.getId());
    }

    public void deleteTask(Task task) {
        tasks.remove(task.getId());
    }

    private Integer createId() {
        return ++taskId;
    }

}
