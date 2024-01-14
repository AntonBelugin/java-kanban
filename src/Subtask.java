public class Subtask extends Task {
    int epicId;
    public Status status;
    public Subtask(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "Субзадача №: " + id +
                ", Эпик №: " + epicId +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус: " + status +
                '.';
    }
}
