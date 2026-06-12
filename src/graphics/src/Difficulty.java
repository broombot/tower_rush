package graphics.src;

public enum Difficulty {
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard");

    private final String displayName;
    Difficulty(String displayName) { this.displayName = displayName; }
    @Override public String toString() { return displayName; }
}
