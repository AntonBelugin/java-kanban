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
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.updateTask(2, new Task("Пропылесосить и помыть",
                "пропылесосить все комнаты"), Status.IN_PROGRESS);
        taskManager.updateEpic(4, new Epic("Генеральная уборка",
                "Убраться в новой квартире"));
        taskManager.updateSubtask(5, new Subtask("Собрать коробки",
                "собрать в коробки вещи"), Status.IN_PROGRESS);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.updateTask(2, taskManager.getIdTask(2), Status.DONE);
        taskManager.updateSubtask(5, taskManager.getIdSubtask(5), Status.DONE);
        taskManager.updateSubtask(6, taskManager.getIdSubtask(6), Status.IN_PROGRESS);
        taskManager.updateEpic(4, new Epic("Генеральная уборка",
                "Убраться в новой квартире 2 раза"));

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.updateTask(2, taskManager.getIdTask(2), Status.DONE);
        taskManager.updateSubtask(5, taskManager.getIdSubtask(5), Status.DONE);
        taskManager.updateSubtask(6, taskManager.getIdSubtask(6), Status.DONE);
        taskManager.updateSubtask(7, taskManager.getIdSubtask(7), Status.DONE);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.deleteSubtask(7);
        System.out.println(taskManager.getIdEpic(4));
        System.out.println();

        taskManager.deleteTask(2);
        taskManager.deleteEpic(4);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getSubtasksEpic(3));
        System.out.println();

        taskManager.clearSubtasks();

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());

    }
}
