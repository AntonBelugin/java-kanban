import Service.TaskManager;
import Task.Epic;
import Task.TaskStatus;
import Task.Task;
import Task.Subtask;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        System.out.println("Поехали!");

        taskManager.makeTask(new Task( "Помыть посуду", "помыть посуду горячей водой"));
        taskManager.makeTask(new Task( "Пропылесосить", "пропылесосить все комнаты"));

        taskManager.makeEpic(new Epic("Переезд", "Переехать в другую квартиру"));
        taskManager.makeEpic(new Epic("Уборка", "Убраться в новой квартире"));

        taskManager.makeSubtask(new Subtask("Собрать коробки", "собрать в коробки вещи"), 3);
        taskManager.makeSubtask(new Subtask("Упаковать кошку", "не забыть лоток"), 3);

        taskManager.makeSubtask(new Subtask("Пропылесосить коридор", "2 раза"), 4);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtaskById());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        Task task = taskManager.getTaskById(2);
        task.name =
        epic.name = "Генеральная уборка";
        taskManager.updateEpic(updateEpic);
        taskManager.updateTask(2, new Task("Пропылесосить и помыть",
                "пропылесосить все комнаты"), TaskStatus.IN_PROGRESS);

        Epic epic = taskManager.getEpicById(4);
        epic.name = "Генеральная уборка";
        taskManager.updateEpic(new Epic(4, "Генеральная уборка", "пропылесосить все комнаты"));

        taskManager.updateSubtask(5, new Subtask("Собрать коробки",
                "собрать в коробки вещи"), TaskStatus.IN_PROGRESS);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtaskById());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.updateTask(2, taskManager.getTaskById(2), TaskStatus.DONE);
        taskManager.updateSubtask(5, taskManager.getIdSubtask(5), TaskStatus.DONE);
        taskManager.updateSubtask(6, taskManager.getIdSubtask(6), TaskStatus.IN_PROGRESS);
        taskManager.updateEpic(4, new Epic("Генеральная уборка",
                "Убраться в новой квартире 2 раза"));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtaskById());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.updateTask(2, taskManager.getTaskById(2), TaskStatus.DONE);
        taskManager.updateSubtask(5, taskManager.getIdSubtask(5), TaskStatus.DONE);
        taskManager.updateSubtask(6, taskManager.getIdSubtask(6), TaskStatus.DONE);
        taskManager.updateSubtask(7, taskManager.getIdSubtask(7), TaskStatus.DONE);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtaskById());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.deleteSubtask(7);
        System.out.println(taskManager.getEpicById(4));
        System.out.println();

        taskManager.deleteTask(2);
        taskManager.deleteEpic(4);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtaskById());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.clearSubtasks();

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtaskById());

    }
}
