import service.InMemoryHistoryManager;
import service.Managers;
import service.TaskManager;
import task.Epic;
import task.Task;
import task.Subtask;

public class Main {

    public static void main(String[] args) {

        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        System.out.println("Поехали!");

        taskManager.makeTask(new Task("Помыть посуду", "помыть посуду горячей водой"));
        taskManager.makeTask(new Task("Пропылесосить", "пропылесосить все комнаты"));

        taskManager.makeEpic(new Epic("Переезд", "Переехать в другую квартиру"));
        taskManager.makeEpic(new Epic("Уборка", "Убраться в новой квартире"));

        taskManager.makeSubtask(new Subtask(taskManager.getEpicById(3), "Собрать коробки",
                "собрать в коробки вещи"));
        taskManager.getEpicById(3);
        taskManager.makeSubtask(new Subtask(taskManager.getEpicById(4), "Упаковать кошку",
                "не забыть лоток"));
        taskManager.getEpicById(3);
        taskManager.makeSubtask(new Subtask(taskManager.getEpicById(3),"Пропылесосить коридор",
                "2 раза"));

        taskManager.getTaskById(2);
        taskManager.makeSubtask(new Subtask(taskManager.getEpicById(3), "Собрать коробки",
              "собрать в коробки вещи"));
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getSubtaskById(7);
        taskManager.getEpicById(4);
        taskManager.getTaskById(2);

        printHistory(taskManager);

        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.deleteEpic(4);
        taskManager.deleteSubtask(7);

        printHistory(taskManager);

        taskManager.deleteEpic(3);
        taskManager.deleteTask(1);

        printHistory(taskManager);

    }
    private static void printHistory(TaskManager taskManager) {
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();
    }
}
