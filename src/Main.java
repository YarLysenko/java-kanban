import model.Epic;
import model.StatusType;
import model.Subtask;
import model.Task;
import service.FileBackedTasksManager;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {


        TaskManager taskManager = Managers.getDefaultFromFile();



        Epic epic = new Epic("Эпик.1", "первый эпик");

        taskManager.createEpic(epic);



        Subtask subtask1_2 = new Subtask("Сабтаска.2", "вторая подзадача",
                LocalDateTime.of(2026, 5, 17, 00, 50), 60, epic.getId());
        taskManager.createSubtask(subtask1_2);

        System.out.println(epic);
        subtask1_2.setStatus(StatusType.IN_PROGRESS);
        taskManager.updateSubtask(subtask1_2);

        System.out.println(epic);
        System.out.println(subtask1_2);




    }
}