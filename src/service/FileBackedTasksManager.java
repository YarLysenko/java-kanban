package service;

import exception.ManagerSaveException;
import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements FileBacked {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);

        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();

    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        super.deleteSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(Task task) {
        super.deleteTask(task);
        save();
    }


    @Override
    public void deleteEpic(Epic epic) {
        super.deleteEpic(epic);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("id,type,name,status,description,startTime,duration,epic");
            for (Task task : getAllTasks()) {
                bufferedWriter.append(toString(task));
            }
            for (Epic epic : getAllEpics()) {
                bufferedWriter.append(toString(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                bufferedWriter.append(toString(subtask));
            }

            bufferedWriter.append("\n" + "\n" + historyToString(getHistoryManager()));


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи файла.");
        }

    }

    public Task fromString(String value) {
        String[] strSplit = value.split(",");
        Integer idTask = Integer.parseInt(strSplit[0]);
        if (idTask > taskId) {
            taskId = idTask;
        }
        TaskType typeTask = TaskType.valueOf(strSplit[1]);
        String name = strSplit[2];
        StatusType status = StatusType.valueOf(strSplit[3]);
        String description = strSplit[4];
        switch (typeTask) {
            case TASK:
                Task task = new Task(name, description);
                task.setId(idTask);
                task.setStatus(status);
                task.setStartTime(dataTimeFromString(strSplit[5]));
                task.setDuration(Integer.parseInt(strSplit[6]));
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(idTask);
                epic.setStatus(status);
                return epic;
            default:
                Subtask subTask = new Subtask(name, description, dataTimeFromString(strSplit[5]),
                        Integer.parseInt(strSplit[6]), Integer.parseInt(strSplit[7]));
                subTask.setId(idTask);
                subTask.setStatus(status);
                return subTask;
        }

    }

    @Override
    public String toString(Task task) {
        return "\n" + task.getId() + ',' + TaskType.TASK + ',' + task.getName() + ',' + task.getStatus()
                + ',' + task.getDescription() + ',' + dataTimeToString(task.getStartTime()) + ',' + task.getDuration();
    }

    @Override
    public String toString(Subtask subtask) {
        return "\n" + subtask.getId() + ',' + TaskType.SUBTASK + ',' + subtask.getName() + ',' + subtask.getStatus()
                + ',' + subtask.getDescription() + ',' + dataTimeToString(subtask.getStartTime()) + ',' + subtask.getDuration() + ',' + subtask.getEpicId();
    }

    @Override
    public String toString(Epic epic) {
        return "\n" + epic.getId() + ',' + TaskType.EPIC + ',' + epic.getName() + ',' + epic.getStatus()
                + ',' + epic.getDescription() + ',' + dataTimeToString(epic.getStartTime()) + ',' + epic.getDuration();
    }
    public String dataTimeToString(LocalDateTime lc) {
        if (lc == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return lc.format(formatter);
    }

    public LocalDateTime dataTimeFromString(String lc) {
        if (lc.equals("null")) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return LocalDateTime.parse(lc, formatter);
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackendTasksManager = new FileBackedTasksManager(file);
        if (file.exists()) {
            try {
                String autoSafe = Files.readString(file.toPath());
                String[] lines = autoSafe.split("\n");
                for (int i = 1; i < lines.length; i++) {
                    if (lines[i].isEmpty()) {
                        if (!lines[++i].isEmpty()) {
                            for (Integer hs : historyFromString(lines[i])) {
                                fileBackendTasksManager.taskToHistory(hs);
                            }
                            break;
                        }
                    }
                    fileBackendTasksManager.putTask(lines[i]);
                }
            } catch (IOException t) {
                throw new ManagerSaveException("Ошибка при загрузке данных.");
            }
        }

        return fileBackendTasksManager;
    }

    public void taskToHistory(Integer hs) {
        if (tasks.containsKey(hs)) {
            historyManager.add(tasks.get(hs));
        } else if (subtasks.containsKey(hs)) {
            historyManager.add(subtasks.get(hs));
        } else {
            historyManager.add(epics.get(hs));
        }
    }

    public void putTask(String lines) {
        String[] split = lines.split(",");
        TaskType typeTask = TaskType.valueOf(split[1]);
        switch (typeTask) {
            case TASK:
                Task task = fromString(lines);
                if (crossingCheck(task)) {
                    tasks.put(task.getId(), task);
                    taskTreeSet.add(task);
                }
                break;
            case EPIC:
                Epic epic = (Epic) fromString(lines);
                epics.put(epic.getId(), epic);
                break;
            default:
                Subtask subtask = (Subtask) fromString(lines);
                if (crossingCheck(subtask)) {
                    subtasks.put(subtask.getId(), subtask);
                    taskTreeSet.add(subtask);
                    Epic epic1 = epics.get(subtask.getEpicId());
                    epic1.getSubtasksId().add(subtask.getId());
//                    updateEpicStatus(epic1);
                    updateEpicTime(epic1);
                }
        }
    }

    public static String historyToString(HistoryManager historyManager) {
        StringBuilder str = new StringBuilder();
        if (!historyManager.getHistory().isEmpty()) {
            for (Task task : historyManager.getHistory()) {
                str.append(task.getId() + ",");

            }
        }
        return str.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] strSplit = value.split(",");
        for (String sss : strSplit) {
            history.add(Integer.parseInt(sss));
        }
        return history;
    }


}