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

        taskManager.makeTask(new Task( "Помыть посуду", "помыть посуду горячей водой"));
        taskManager.makeTask(new Task( "Пропылесосить", "пропылесосить все комнаты"));

        taskManager.makeEpic(new Epic("Переезд", "Переехать в другую квартиру"));
        taskManager.makeEpic(new Epic("Уборка", "Убраться в новой квартире"));

        taskManager.makeSubtask(new Subtask(taskManager.getEpicById(3), "Собрать коробки",
                "собрать в коробки вещи"));
        taskManager.makeSubtask(new Subtask(taskManager.getEpicById(3), "Упаковать кошку",
                "не забыть лоток"));
        taskManager.makeSubtask(new Subtask(taskManager.getEpicById(4),"Пропылесосить коридор",
                "2 раза"));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println(taskManager.getSubtasksByEpic(3));
        System.out.println();
        taskManager.getTaskById(2);

        for (Task task: taskManager.getHistory()) {
            System.out.println(task);
        }

    }
}
