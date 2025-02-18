package com.tms.controller;

import com.tms.model.Task;
import com.tms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/tms")
    public String hello(){
        return "Task Management System";
    }

    //POST /tasks: Create a new task (title, description, status, assigned user).
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        return taskService.createTask(task);
    }

    //GET /tasks: Retrieve all tasks.
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return taskService.getAllTasks();
    }

    //GET /tasks/:id: Retrieve a specific task by ID
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Optional<Task>> getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    //PUT /tasks/:id: Update a task's details.
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Optional<Task>> updateTask(@PathVariable Long id, @RequestBody Task task){

        return taskService.updateTask(id, task);
    }

    //DELETE /tasks/:id: Delete a task.
    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable Long id){
        return taskService.deleteTask(id);
    }
}
