package com.yuhyfe.thebuggerbot.project.exception;

public class ProjectAlreadyArchivedException extends ProjectException {
    public ProjectAlreadyArchivedException(String name) {
        super("Project '" + name + "' is already archived");
    }
}
