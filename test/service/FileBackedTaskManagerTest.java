package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;

import java.io.File;
import java.io.IOException;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File tempFile;

    {
        try {
            tempFile = File.createTempFile("data", "csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    FileBackedTaskManager createManager() {
        return new FileBackedTaskManager(tempFile);
    }

}
