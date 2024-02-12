package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class StaticMethod {
    private StaticMethod() {
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
