package com.yuhyfe.thebuggerbot.project.exception;

public class ProjectNotFoundException extends ProjectException {
    public ProjectNotFoundException(String name) {
        super("Project '" + name + "' not found");
    }
}
