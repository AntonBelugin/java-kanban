package convertors;

import task.Subtask;
import task.Task;

public class SubtaskConvertor {

    public String toString(Task subtask) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,", subtask.getId(),subtask.getTaskType(),
                subtask.getName(), subtask.getTaskStatus(), subtask.getDescription(),
                subtask.getEpicId(), subtask.getStartTime(), subtask.getDuration().toMinutes());

    }
}
