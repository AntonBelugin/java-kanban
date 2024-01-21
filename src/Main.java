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

        Task task = taskManager.getTaskById(2);
        task.name = "Пропылесосить и помыть";
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);

        Epic epic = taskManager.getEpicById(4);
        epic.name = "Генеральная уборка";
        taskManager.updateEpic(epic);

        Subtask subtask = taskManager.getSubtaskById(5);
        subtask.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasksByEpic(3));
        System.out.println();

        Task task2 = taskManager.getTaskById(2);
        task2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task2);

        Subtask subtask2 = taskManager.getSubtaskById(5);
        subtask2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);

        Subtask subtask3 = taskManager.getSubtaskById(6);
        subtask3.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask3);

        Subtask subtask5 = taskManager.getSubtaskById(7);
        subtask5.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask5);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasksByEpic(3));
        System.out.println();

        Subtask subtask4 = taskManager.getSubtaskById(6);
        subtask4.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask4);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println();

        taskManager.deleteSubtask(7);
        System.out.println(taskManager.getEpicById(4));
        System.out.println();

        taskManager.deleteTask(2);
        taskManager.deleteEpic(4);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println();

        taskManager.clearSubtasks();

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasksByEpic(3));
        System.out.println();

        taskManager.clearSubtasks();

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());

    }
}
