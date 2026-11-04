import taskLogic.Task;
import taskLogic.TaskAlreadyExistsException;
import taskLogic.TaskManager;
import taskLogic.TaskNotFoundException;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        try {
            manager.addTask(new Task("Купить молоко", 1));
            manager.addTask(new Task("Учить программирование", 3));
            manager.addTask(new Task("Обнять жену", 3));
            manager.addTask(new Task("Помыть посуду", 2));
            manager.addTask(new Task("Почитать книгу", 1));
            manager.addTask(new Task("Купить молоко", 1));
        } catch (TaskAlreadyExistsException e) {
            System.out.println("Поймали: " + e.getMessage());
        }
        try {
            manager.markCompleted("Обнять жену");
            manager.markCompleted("Купить молоко");
            manager.markCompleted("Несуществующая");
        } catch (TaskNotFoundException e) {
            System.out.println("Поймали: " + e.getMessage());
        }

        Path file = Path.of("tasks.txt");

        try {
            manager.saveToFile(file);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }

        TaskManager loadedManager = new TaskManager();

        try{
            loadedManager.loadFromFile(file);
            System.out.println("\n=== Загружено из файла ===");
            loadedManager.printAll();
        } catch (IOException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }

    }
}
