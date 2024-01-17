package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_SIZE = 10;
    private Node first;
    private Node last;
    private int size = 0;
    private final Map<Integer, Node> taskNodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        Node existingNode = taskNodeMap.get(task.getId());


        if (existingNode != null) {
            removeNode(existingNode);
        }

        Node newNode = linkLast(task);
        taskNodeMap.put(task.getId(), newNode);


        if (size > HISTORY_SIZE) {
            removeNode(first);
        }
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = taskNodeMap.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
            taskNodeMap.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasks.add((Task) current.item);
            current = current.next;
        }
        return tasks;
    }

    private Node linkLast(Object item) {
        final Node l = last;
        final Node newNode = new Node(l, item, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
        return newNode;
    }

    private void removeNode(Node node) {
        final Node next = node.next;
        final Node prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        size--;
    }
}