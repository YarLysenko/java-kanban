package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient taskClient;
    private final Gson gson = Managers.getGson();

    public HttpTaskManager(String url) {
        this(url, false);
    }

    public HttpTaskManager(String url, boolean isLoad) {
        super(null);
        taskClient = new KVTaskClient(url);
        if (isLoad) {
            load();
        }
    }

    @Override
    public void save() {
        String taskJson = gson.toJson(new ArrayList<>(tasks.values()));
        taskClient.put("tasks", taskJson);
        String epicJson = gson.toJson(new ArrayList<>(epics.values()));
        taskClient.put("epics", epicJson);
        String subtaskJson = gson.toJson(new ArrayList<>(subtasks.values()));
        taskClient.put("subtasks", subtaskJson);
        String historyJson = gson.toJson(historyManager.getHistory().stream()
                .map(Task::getId).collect(Collectors.toList()));

        taskClient.put("history", historyJson);
    }

    public void load() {
        Type type1 = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> taskList = gson.fromJson(taskClient.load("tasks"), type1);
        for (Task task : taskList) {
            int taskId = task.getId();
            if (taskId > id) {
                id = taskId;
            }
            tasks.put(taskId, task);
            taskTreeSet.add(task);
        }
        Type type2 = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epicList = gson.fromJson(taskClient.load("epics"), type2);
        for (Epic epic : epicList) {
            int epicId = epic.getId();
            if (epicId > id) {
                id = epicId;
            }
            epics.put(epicId, epic);
        }
        Type type3 = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtaskList1 = gson.fromJson(taskClient.load("subtasks"), type3);
        for (Subtask subtask : subtaskList1) {
            int subtaskId = subtask.getId();
            if (subtaskId > id) {
                id = subtaskId;
            }
            subtasks.put(subtaskId, subtask);
            taskTreeSet.add(subtask);
        }
        Type type4 = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> historyList = gson.fromJson(taskClient.load("history"), type4);
        for (Integer taskId : historyList) {

            if (tasks.containsKey(taskId)) {
                historyManager.add(tasks.get(taskId));
            } else if (subtasks.containsKey(taskId)) {
                historyManager.add(subtasks.get(taskId));
            } else {
                historyManager.add(epics.get(taskId));
            }
        }
    }
}