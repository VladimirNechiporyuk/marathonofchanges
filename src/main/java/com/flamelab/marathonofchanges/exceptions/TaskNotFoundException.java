package com.flamelab.marathonofchanges.exceptions;

public class TaskNotFoundException extends RuntimeException  {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
