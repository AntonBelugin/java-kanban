import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> subtasksEpic;
    public Status status;

    public Epic(String name, String description) {
        super(name, description);
        subtasksEpic = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Эпик №: " + id +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус: " + status +
                '.';
    }
}
