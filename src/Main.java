import model.Epic;
import model.StatusType;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;

import java.io.File;


public class Main {
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