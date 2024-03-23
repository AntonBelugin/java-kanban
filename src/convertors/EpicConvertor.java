package convertors;

import task.Task;

public class EpicConvertor {
    public String toString(Task epic) {
        return epic.getId() + "," + epic.getTaskType() + "," + epic.getName() +
                "," + epic.getTaskStatus() + "," + epic.getDescription();
    }

}
