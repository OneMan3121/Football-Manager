package ua.oneman.footballmanagerbackend.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(String name) {
        super("Team not found with name: " + name);
    }
}
