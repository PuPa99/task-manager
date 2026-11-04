package taskLogic;

import java.util.Objects;

public class Task {
    private String title;
    private int priority;
    private boolean completed;

    public Task(String title, int priority, boolean completed){
        this.title = title;
        this.priority = priority;
        this.completed = completed;
    }

    public Task(String title, int priority){
        this(title, priority, false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted(boolean completed) {
        this.completed = completed;
    }


    @Override
    public String toString() {
        String info = priorityString() + " " + title + " " + completedString();
        return info;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        int hash = Objects.hash(title);
        return hash;
    }

    public String completedString(){
        if(completed){
            return "(выполнена)";
        }
        else {
            return "(не выполнена)";
        }
    }

    public String priorityString(){
        return switch (priority){
            case 1 -> "[LOW]";
            case 2 -> "[MEDIUM]";
            case 3 ->  "[HIGH]";
            default -> "[UNKNOWN]";
        };
    }

    public String priorityKey(){
        return switch (priority){
            case 1 -> "LOW";
            case 2 -> "MEDIUM";
            case 3 ->  "HIGH";
            default -> "UNKNOWN";
        };
    }

    public void printInfo(){
        System.out.println(this);
    }

}
