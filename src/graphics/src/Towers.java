package graphics.src;

public enum Towers {
    FREE(false),
    OCCUPIED(true),
    Cannon(true),
    Archer(true);

    private final boolean value;

    Towers(boolean value){this.value = value;}

    public boolean ocupide(){
        return value;
    }
}
