package service;

import model.Epic;
import model.StatusType;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static model.StatusType.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> taskTreeSet = new TreeSet<>(new StartComparator());

    protected Integer id = 0;


    public Set<Task> getTaskTreeSet() {
        return taskTreeSet;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            if (crossingCheck(task)) {
                task.setId(createId());
                tasks.put(task.getId(), task);
                taskTreeSet.add(task);
            }
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(createId());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (subtask != null) {
            if (crossingCheck(subtask)) {
                subtask.setId(createId());
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                epic.getSubtasksId().add(subtask.getId());
                updateEpicStatus(epic);
                updateEpicTime(epic);
                taskTreeSet.add(subtask);
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpic(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.setStatus(updateEpicStatus(epic));
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksId()) {
            Subtask subtask = subtasks.get(subtaskId);
            subtasksByEpic.add(subtask);
        }
        return subtasksByEpic;
    }

    @Override
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtasksId()) {
                historyManager.remove(subtaskId);
            }
            epic.getSubtasksId().clear();
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getSubtasksId()) {
                historyManager.remove(subtaskId);
            }
            historyManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksId().remove((Integer) subtask.getId());
            updateEpic(epic);
            subtasks.remove(subtask.getId());
            historyManager.remove(subtask.getId());
        }
    }

    @Override
    public void deleteEpic(Epic epic) {
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(epic.getId());
        historyManager.remove(epic.getId());
    }

    @Override
    public void deleteTask(Task task) {
        tasks.remove(task.getId());
        historyManager.remove(task.getId());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(taskTreeSet);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> timeList() {
        return new ArrayList<>(taskTreeSet);
    }

    protected StatusType updateEpicStatus(Epic epic) {
        ArrayList<StatusType> subtasksStatus = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksId()) {
            StatusType status = subtasks.get(subtaskId).getStatus();
            subtasksStatus.add(status);
        }
        if (subtasksStatus.isEmpty()) {
            return NEW;
        } else if (!subtasksStatus.contains(NEW) && !subtasksStatus.contains(IN_PROGRESS)) {
            return DONE;
        } else if (!subtasksStatus.contains(DONE) && !subtasksStatus.contains(IN_PROGRESS)) {
            return NEW;
        } else {
            return IN_PROGRESS;
        }
    }

    protected void updateEpicTime(Epic epic) {
        List<Subtask> subs = epic.getSubtasksId().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
        LocalDateTime epicStart = subs.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        LocalDateTime epicEnd = subs.stream()
                .map(subtask -> {
                    LocalDateTime strStartTime = subtask.getStartTime();
                    Integer durat = subtask.getDuration();
                    return (strStartTime != null) ? strStartTime.plusMinutes(durat) : null;
                })
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        epic.setStartTime(epicStart);
        epic.setEndTime(epicEnd);
    }

    protected boolean crossingCheck(Task task) {
        if (task.getStartTime() == null) {
            return true;
        }
        for (Task task1 : taskTreeSet) {
            if (task1.getStartTime() != null) {
                if ((task.getStartTime().isAfter(task1.getStartTime()) || task.getStartTime().equals(task1.getStartTime()))
                        && (task.getStartTime().isBefore(task1.endTme()) || task.getStartTime().equals(task1.getStartTime()))) {
                    return false;
                }
            }
        }
        return true;
    }

    private Integer createId() {
        return ++id;
    }

    public static class StartComparator implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.getStartTime() == null || task2.getStartTime() == null) {
                if (task1.getStartTime() != null) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return task1.getStartTime().compareTo(task2.getStartTime());
            }
        }
    }
}
