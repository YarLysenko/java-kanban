package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;

public interface FileBacked {

    void save();

    Task fromString(String value);

    String toString(Task task);

    String toString(Subtask subtask);

    String toString(Epic epic);


}
