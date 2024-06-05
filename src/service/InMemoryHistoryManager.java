package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node first;
    private Node last;
    protected final Map<Integer, Node> taskNodeMap = new HashMap<>();

    @Override
    public void add(Task task) {
        // Проверка на null и вывод отладочного сообщения
        if (task == null) {
            System.err.println("Attempted to add a null task.");
            throw new IllegalArgumentException("Task cannot be null");
        }

        System.out.println("Adding task with ID: " + task.getId());

        Node existingNode = taskNodeMap.remove(task.getId());

        if (existingNode != null) {
            System.out.println("Removing existing node for task with ID: " + task.getId());
            removeNode(existingNode);
        } else {
            System.out.println("No existing node found for task with ID: " + task.getId());
        }

        Node newNode = linkLast(task);
        taskNodeMap.put(task.getId(), newNode);

        System.out.println("Task with ID: " + task.getId() + " added successfully.");
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = taskNodeMap.remove(id);

        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
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

    public Map<Integer, Node> getTaskNodeMap() {
        return taskNodeMap;
    }

    private Node linkLast(Task item) {
        final Node l = last;
        final Node newNode = new Node(l, item, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
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
    }

    private static class Node {
        private Task item;
        private Node next;
        private Node prev;

        Node(Node prev, Task item, Node next) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }
}