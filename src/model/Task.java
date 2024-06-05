package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static model.StatusType.NEW;

public class Task {
    protected Integer id;
    protected TaskType type;
    protected String name;
    protected StatusType status;
    protected String description;
    protected LocalDateTime startTime;
    protected int duration;



    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        status = NEW;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = NEW;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime endTme() {
        if (startTime == null) {
            return null;
        }
        if (duration == 0) {
            return null;
        }
        return startTime.plusMinutes(duration);
    }
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    public String dataTimeTpString(LocalDateTime lc) {
        if (lc == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return lc.format(formatter);
    }
}
