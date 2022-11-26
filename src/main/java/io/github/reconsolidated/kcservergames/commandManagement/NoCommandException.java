package io.github.reconsolidated.kcservergames.commandManagement;

public class NoCommandException extends RuntimeException {
    public NoCommandException() {
        super("No command found");
    }
}
