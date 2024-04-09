package convertors;

import task.Task;

public class EpicConvertor {
    public String toString(Task epic) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,", epic.getId(),epic.getTaskType(),
                epic.getName(), epic.getTaskStatus(), epic.getDescription(),
                epic.getEpicId(), epic.getStartTime(), epic.getDuration().toMinutes());
    }

}
