package com.tms.service;

import com.tms.model.Task;
import com.tms.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    public ResponseEntity<List<Task>> getAllTasks() {

        List<Task> list = taskRepository.findAll();

        if (list.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        }

    }

    public ResponseEntity<Task> createTask(Task task) {
        taskRepository.save(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    public ResponseEntity<Optional<Task>> getTaskById(Long id) {
        return new ResponseEntity<>(taskRepository.findById(id), HttpStatus.FOUND);
    }

    public ResponseEntity<Optional<Task>> updateTask(Long id, Task task) {
        Optional<Task> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            Task updatedTask = existingTask.get();
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setStatus(task.getStatus());
            updatedTask.setAssigned_user(task.getAssigned_user());
            taskRepository.save(updatedTask);
            return ResponseEntity.ok(Optional.of(updatedTask));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    public String deleteTask(Long id) {
        taskRepository.deleteById(id);
        return "Task id : " + id + " deleted successfully";
    }
}
