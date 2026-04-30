
/**
 * Value and its Definition of Grid Cel Types
 * -1| IN VALUD INPUT
 * 0 | PLACEABLE AREA
 * 1 | BLOCKED AREA
 * 2 | PROJECTILE BLOCKING AREA
 * 3 | INTERSECTION OF TWO OR MORE PATHS
 * 4 | PATH 1
 * 5 | PATH 2
 * 6 | PATH 3
 * code supports unlimited amount of Paths you can just add more with last value++ and last path++
 */
package towerrush;

public enum TileType {

    IN_VALID(-1),
    PLACEABLE(0),
    BLOCKED(1),
    PROJECTILE_BLOCKING(2),
    INTERSECTION(3),
    PATH1(4),
    PATH2(5),
    PATH3(6);


    private final int value;

    TileType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TileType fromInt(int value) {
        for (TileType type : TileType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return IN_VALID;
    }
}
