package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileBackedTasksManager extends InMemoryTaskManager {

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
    public StatusType updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
        return epic.getStatus();
    }


    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write("id,type,name,status,description,epic");
            for (Task task : getAllTasks()) {
                bufferedWriter.append(toString(task));
            }
            for (Epic epic : getAllEpics()) {
                bufferedWriter.append(toString(epic));
            }
            for (Subtask subtask : getAllSubtasks()) {
                bufferedWriter.append(toString(subtask));
            }

            bufferedWriter.append("\n" + "\n" + StaticMethod.historyToString(getHistoryManager()));


        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи файла.");
        }

    }


    public Task fromString(String value) {
        String[] strSplit = value.split(",");
        TaskType typeTask = TaskType.valueOf(strSplit[1]);
        if (typeTask.equals(TaskType.TASK)) {
            Task task = new Task(strSplit[2], strSplit[4]);
            createTask(task);
            return task;
        } else if (typeTask.equals(TaskType.SUBTASK)) {
            int parse = Integer.parseInt(strSplit[5]);
            Subtask subtask = new Subtask(strSplit[2], strSplit[4], parse);
            createSubtask(subtask);
            return subtask;
        } else {
            Epic epic = new Epic(strSplit[2], strSplit[4]);
            createEpic(epic);
            return epic;
        }
    }

    public String toString(Task task) {
        return "\n" + task.getId() + ',' + TaskType.TASK + ',' + task.getName() + ',' + task.getStatus()
                + ',' + task.getDescription();
    }

    public String toString(Subtask subtask) {
        return "\n" + subtask.getId() + ',' + TaskType.SUBTASK + ',' + subtask.getName() + ',' + subtask.getStatus()
                + ',' + subtask.getDescription() + ',' + subtask.getEpicId();
    }

    public String toString(Epic epic) {
        return "\n" + epic.getId() + ',' + TaskType.EPIC + ',' + epic.getName() + ',' + epic.getStatus()
                + ',' + epic.getDescription();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackendTasksManager = new FileBackedTasksManager(file);
        if (!file.isFile()) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                bufferedWriter.write("id,type,name,status,description,epic");
            } catch (IOException e) {
                System.out.println("Ошибка, файл не создан");
            }
        } else {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                ArrayList<String> line = new ArrayList<>();
                while (bufferedReader.ready()) {
                    line.add(bufferedReader.readLine());
                }
                for (String string : line) {
                    System.out.println(string);
                }
            } catch (IOException t) {
                throw new ManagerSaveException("Ошибка при загрузке данных.");
            }
        }
        return fileBackendTasksManager;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File(".\\src\\resources\\AutoSave.csv"));
        Task task1 = new Task("Задача 1", "Содержание задачи 1");
        fileBackedTasksManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Содержание задачи 2");
        fileBackedTasksManager.createTask(task2);
        Epic epic12 = new Epic("Эпик 1", "Содержание эпика 1");
        fileBackedTasksManager.createEpic(epic12);

        Subtask subtask11 = new Subtask("Подзадача 1", "Содержание подзадачи 1", 3);
        fileBackedTasksManager.createSubtask(subtask11);
        Epic epic22 = new Epic("Эпик 2", "Des2");
        fileBackedTasksManager.createEpic(epic22);
        Subtask subtask21 = new Subtask("Подзадача 2", "Содержание подзадачи 2", epic22.getId());
        fileBackedTasksManager.createSubtask(subtask21);
        Subtask subtask22 = new Subtask("Подзадача 3", "Содержание подзадачи 3", epic22.getId());
        fileBackedTasksManager.createSubtask(subtask22);


        Task task = new Task("Задача 2", "Содержание задачи 2");
        fileBackedTasksManager.createTask(task);
        Task task40 = new Task("Задача 3", "Содержание задачи 3");
        fileBackedTasksManager.createTask(task40);
        Task task50 = new Task("Задача 4", "Содержание задачи 4");
        fileBackedTasksManager.createTask(task50);
        Task task60 = new Task("Задача 5", "Содержание задачи 5");
        fileBackedTasksManager.createTask(task60);


        Subtask subtask1212 = new Subtask("Подзадача 4", "Содержание подзадачи 4", 1212);
        Task task1323 = new Task("Task1323", "1323");
        task1323.setStatus(StatusType.IN_PROGRESS);
        Epic epic1222 = new Epic("Epic1222", "1222");

        Task task11666 = new Task("Task11666", "11666");
        System.err.println("история");
        for (Task tasktask : fileBackedTasksManager.getHistory()) {
            System.out.println(tasktask);
        }


    }
}