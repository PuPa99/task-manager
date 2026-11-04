package taskLogic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class TaskManager {

    private static final String NL = System.lineSeparator();

    private List<Task>  tasks;
    private Set<String> titles;
    private Map<String, List<Task>> tasksByPriority;

    public TaskManager(){
        this.tasks = new ArrayList<>();
        this.titles = new HashSet<>();
        this.tasksByPriority = new HashMap<>();
    }

    public void addTask(Task task) throws TaskAlreadyExistsException {
        String title = task.getTitle();
        if(titles.contains(title)){
           throw new TaskAlreadyExistsException("Задача \"" + title + "\" уже добавлена");
        }

        tasks.add(task);
        titles.add(task.getTitle());

        String priorityKey = task.priorityKey();
        if(tasksByPriority.containsKey(priorityKey)){
            List<Task> list = tasksByPriority.get(priorityKey);
            list.add(task);
        }
        else{
            List<Task> newList = new ArrayList<>();
            newList.add(task);
            tasksByPriority.put(priorityKey, newList);
        }

    }

    public void removeTask(String title) throws TaskNotFoundException{
        if(!titles.contains(title)){
            throw new TaskNotFoundException("Задача \"" + title + "\" не найдена");
        }

        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()){
            Task t = it.next();
            if(t.getTitle().equals(title)){
                String priorityKey = t.priorityKey();
                tasksByPriority.get(priorityKey).remove(t);
                it.remove();
                break;
            }
        }
        titles.remove(title);
    }

    public void markCompleted(String title)throws TaskNotFoundException {
        if(!titles.contains(title)){
            throw new TaskNotFoundException("Задача \"" + title + "\" не найдена");
        }
        for(Task task : tasks){
            if(task.getTitle().equals(title)){
                task.markCompleted(true);
                return;
            }
        }

    }

    public void printAll(){
        System.out.println("=== Все задачи ===");
        for(Task task : tasks){
            task.printInfo();
        }
    }

    public void printByPriority(String priority){
        List<Task> list = tasksByPriority.get(priority);
        if(list == null || list.isEmpty()){
            System.out.println("Задач с приоритетом " + priority + " нет.");
            return;
        }
        System.out.println("=== Задачи с приоритетом " + priority + " ===");
        for(Task task : list){
            task.printInfo();
        }
    }

    public void printCompleted() {
        boolean contain = false;
        for (Task task : tasks) {
            if (task.isCompleted()) {
                if(!contain){
                    System.out.println("=== Выполненные задачи ===");
                    contain = true;
                }
                task.printInfo();
            }
        }
        if(!contain) {
            System.out.println("Выполненных задач нет.");
        }
    }

    public int count(){
        return tasks.size();
    }

    public void printReversed(){

        List<Task> copy = new ArrayList<>(tasks);
        Collections.reverse(copy);
        if(copy.isEmpty()){
            System.out.println("Список пуст");
        }
        else {
            System.out.println(NL + "=== Обратный порядок ===");
            for(Task task : copy){
            task.printInfo();
                }
        }
    }

    public void printShuffled(){
        List<Task> copy = new ArrayList<>(tasks);
        Collections.shuffle(copy);
        if(copy.isEmpty()){
            System.out.println("Список пуст");
        }
        else {
            System.out.println(NL + "=== Случайный порядок ===");
            for (Task task : copy) {
                task.printInfo();
            }
        }
    }

    public int getMaxPriority(){
        int max = 0;
        for(Task task :tasks){
            if(task.getPriority() > max){
                max = task.getPriority();
            }
        }
        return max;
    }

    public int countByPriority(String priority){
        List<Task> list = tasksByPriority.get(priority);
        if(list == null){
            return 0;
        }
        return list.size();
    }


    public void printSortedByPriorityDesc(){
        List<Task> list = new ArrayList<>(tasks);
        if(list.isEmpty()){
            System.out.println("Список пуст");
            return;
        }
        Comparator<Task> byPriorityDesc = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Integer.compare(t2.getPriority(), t1.getPriority());
            }
        };
        Collections.sort(list, byPriorityDesc);
        System.out.println(NL + "=== Задачи по приоритету (от высокого) ===");
        for(Task task : list){
            task.printInfo();
        }
    }


    public void printSortedByTitle(){
        List<Task> list = new ArrayList<>(tasks);
        if(list.isEmpty()){
            System.out.println("Список пуст");
            return;
        }
        Comparator<Task> byTitle = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return t1.getTitle().compareTo(t2.getTitle());
            }
        };
        System.out.println(NL + "=== Задачи по алфавиту ===");
        Collections.sort(list, byTitle);
        for(Task task : list){
            task.printInfo();
        }
    }



    public void printSortedByStatus(){
      List<Task> list = new ArrayList<>(tasks);
      if(list.isEmpty()){
          System.out.println("Список пуст");
          return;
      }
      Comparator<Task> byStatus = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Boolean.compare(t1.isCompleted(), t2.isCompleted());
            }
        };
      System.out.println(NL + "=== Задачи по статусу ===");
      Collections.sort(list, byStatus);
      for(Task task : list){
          task.printInfo();
      }
    }

    public String buildReport() {
        StringBuilder sb = new StringBuilder(250);
        sb.append("=== ОТЧЁТ ПО ЗАДАЧАМ ===").append(NL).append(NL);
        sb.append("Всего задач: ").append(tasks.size());

        int done = 0;
        for(Task task : tasks){
            if(task.isCompleted()){
                done++;
            }
        }
        sb.append(NL).append("Выполнено: ").append(done);
        sb.append(NL).append("Не выполнено: ").append(tasks.size()-done);
        sb.append(NL).append(NL).append("Список задач:").append(NL);

        for(int i = 0; i < tasks.size(); i++){
            sb.append(i+1).append(". ").append(tasks.get(i)).append(NL);
        }
        sb.append(NL).append("=== КОНЕЦ ОТЧЁТА ===");
        return sb.toString();
    }

    public void saveToFile(Path path) throws IOException{
        List<String> lines = new ArrayList<>();
        for (Task task : tasks){
            String line = task.getTitle() + " | " + task.getPriority() + " | " + task.isCompleted();
            lines.add(line);
        }
        Files.write(path, lines);
    }

    public void loadFromFile(Path path) throws IOException{
        List<String> lines = Files.readAllLines(path);
        for(String line : lines){
            String[] parts = line.split(Pattern.quote(" | "));
            String title = parts[0];
            int priority = Integer.parseInt(parts[1]);
            boolean completed = Boolean.parseBoolean(parts[2]);
            Task task = new Task(title, priority, completed);
            try{
                addTask(task);
            } catch (TaskAlreadyExistsException e) {
                System.out.println("Задача уже загружена/ задача с таким именем уже существует.");
                continue;
            }
        }
    }

}
