import model.Epic;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1");
        taskManager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.createTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic1);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", epic1.getId());
        taskManager.createSubtask(subtask3);


        System.out.println();
        System.out.println("ИЗМЕНЕНИЕ ЗАДАЧ");
        System.out.println();
        task1.setStatus("DONE");
        taskManager.updateTask(task1);
        epic1.setStatus("DONE");
        taskManager.updateEpic(epic1);
        taskManager.getAllEpics().forEach(System.out::println);

        subtask1.setStatus("DONE");
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus("IN_PROGRESS");
        taskManager.updateSubtask(subtask2);
        taskManager.getAllEpics().forEach(System.out::println);
        taskManager.getAllSubtasks().forEach(System.out::println);

        System.out.println();
        System.out.println("ПОЛУЧИТЬ ВСЕ ПОДЗАДАЧИ ЭПИКА");
        System.out.println();
        List<Subtask> subtasksByEpic = taskManager.getSubtasksByEpic(epic1);
        subtasksByEpic.forEach(System.out::println);

        System.out.println();
        System.out.println("ПОЛУЧЕНИЕ ЗАДАЧ ПО ИДЕНТИФИКАТОРУ");
        System.out.println();
        System.out.println(taskManager.getTaskById(task1.getId()));
        System.out.println(taskManager.getEpicById(epic1.getId()));
        System.out.println(taskManager.getSubtaskById(subtask1.getId()));
        System.out.println(taskManager.getTaskById(task2.getId()));
        System.out.println(taskManager.getSubtaskById(subtask3.getId()));
        System.out.println(taskManager.getEpicById(epic2.getId()));

        System.out.println();

        System.out.println();
        System.out.println("ИСТОРИЯ ПРОСМОТРОВ:");
        System.out.println();
        taskManager.getHistory().forEach(System.out::println);

        System.out.println();
        System.out.println("ПОЛУЧЕНИЕ ЗАДАЧ ПО ИДЕНТИФИКАТОРУ 2");
        System.out.println();
        System.out.println(taskManager.getEpicById(epic1.getId()));
        System.out.println(taskManager.getEpicById(epic1.getId()));
        System.out.println(taskManager.getSubtaskById(subtask2.getId()));
        System.out.println(taskManager.getSubtaskById(subtask3.getId()));


        System.out.println();
        System.out.println("УДАЛЕНИЕ ОДНОЙ ПОДЗАДАЧИ");
        System.out.println();
        taskManager.deleteSubtask(subtask1);
        taskManager.getAllEpics().forEach(System.out::println);
        taskManager.getAllSubtasks().forEach(System.out::println);

        System.out.println();
        System.out.println("ИСТОРИЯ ПРОСМОТРОВ 2:");
        System.out.println();
        taskManager.getHistory().forEach(System.out::println);


        System.out.println();
        System.out.println("УДАЛЕНИЕ ОДНОГО ЭПИКА");
        System.out.println();
        taskManager.deleteEpic(epic1);
        taskManager.getAllEpics().forEach(System.out::println);
        taskManager.getAllSubtasks().forEach(System.out::println);

        System.out.println();
        System.out.println("УДАЛЕНИЕ ВСЕХ ЗАДАЧ");
        System.out.println();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllTasks();
        taskManager.getAllEpics().forEach(System.out::println);
        taskManager.getAllSubtasks().forEach(System.out::println);

        System.out.println();
        System.out.println("ИСТОРИЯ ПРОСМОТРОВ 3:");
        System.out.println();
        taskManager.getHistory().forEach(System.out::println);

    }
}