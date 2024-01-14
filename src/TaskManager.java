import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    // Методы для Task
    public void makeTask(Task task) {
        task.id = id;
        task.status = Status.NEW;
        tasks.put(id, task);
        id +=1;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearTasks() {
        tasks.clear();
    }

    public Task getIdTask(int id) {
        return tasks.get(id);
    }

    public void updateTask(int id, Task updateTask, Status taskStatus) {
        updateTask.id = id;
        updateTask.status = taskStatus;
        tasks.put(updateTask.getId(), updateTask);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    // Методы для Epic
    public void makeEpic(Epic epic) {
        epic.id = id;
        epic.status = Status.NEW;
        epics.put(id, epic);
        id +=1;
    }
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void clearEpics() {
        epics.clear();
    }

    public Epic getIdEpic(int id) {
        return epics.get(id);
    }

    public void updateEpic(int id, Epic updateEpic) {
        ArrayList<Subtask> tempList = epics.get(id).subtasksEpic;
        updateEpic.id = id;
        updateEpic.subtasksEpic = tempList;
        updateEpic.status = countStatus(updateEpic);
        epics.put(updateEpic.getId(), updateEpic);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }

    //Метод для рассчета статуса Epic

    public Status countStatus(Epic epic) {
        ArrayList<Subtask> subtasksEpic = epic.subtasksEpic;
        if (subtasksEpic.isEmpty()) {
            return Status.NEW;
        } else {
            int statusNew = 0;
            int statusDone = 0;
            int statusIn_progress = 0;
            for (Subtask subtask: subtasksEpic) {
                switch (subtask.status) {
                    case NEW:
                        statusNew +=1;
                        break;
                    case DONE:
                        statusDone +=1;
                        break;
                    case IN_PROGRESS:
                        statusIn_progress +=1;
                        break;
                }
            }
            if (statusDone < 1 && statusIn_progress < 1) {
                return Status.NEW;
            } else if (statusNew < 1 && statusIn_progress < 1) {
                return Status.DONE;
            } else {
                return Status.IN_PROGRESS;
            }
        }
    }


    // Методы для Subtask
    public void makeSubtask(Subtask subtask, int epicId) {
        subtask.id = id;
        subtask.epicId = epicId;
        subtask.status = Status.NEW;
        subtasks.put(id, subtask);
        Epic epic = epics.get(epicId);
        epic.subtasksEpic.add(subtask);
        epic.status = countStatus(epic);
        epics.put(epic.getId(), epic);
        id +=1;
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    public Subtask getIdSubtask(int id) {
        return subtasks.get(id);
    }

    public void updateSubtask(int id, Subtask updateSubtask, Status subtaskStatus) {
        Epic epic = epics.get(getIdSubtask(id).epicId);
        epic.subtasksEpic.remove(getIdSubtask(id));
        updateSubtask.id = id;
        updateSubtask.epicId = epic.id;
        updateSubtask.status = subtaskStatus;
        epic.subtasksEpic.add(updateSubtask);
        subtasks.put(id, updateSubtask);
        epic.status = countStatus(epic);
        epics.put(epic.getId(), epic);
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.epicId);
        epic.subtasksEpic.remove(subtask);
        subtasks.remove(id);
        epic.status = countStatus(epic);
        epics.put(epic.getId(), epic);
    }
    public ArrayList<Subtask> getSubtasksEpic(int id) {
        return getIdEpic(id).subtasksEpic;
    }
}
