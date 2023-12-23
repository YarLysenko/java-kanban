package service;

import model.Task;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_SIZE = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            history.addLast(task);
            if (history.size() > HISTORY_SIZE) {
                history.removeFirst();
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }
}