package me.sizableshrimp.adventofcode.templates;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * A 2-dimensional coordinate object that holds an x and a y value.
 */
@Value
public class Coordinate {

    public int x, y;

    @AllArgsConstructor
    public enum Direction {
        NORTH(0, 0, -1), EAST(90, 1, 0), SOUTH(180, 0, 1), WEST(270, -1, 0);

        public int degrees;
        public int x;
        public int y;

        public static Direction getDirection(int degrees) {
            if (degrees < 0)
                degrees = 360 + degrees;
            degrees %= 360;
            for (Direction direction : values()) {
                if (direction.degrees == degrees)
                    return direction;
            }

            return null;
        }
    }

    /**
     * Creates a new {@link Coordinate} based on the offset of x and y given in parameters added to the current
     * coordinate.
     *
     * @param x The offset-x to add to the coordinate x.
     * @param y The offset-y to add to the coordinate y.
     * @return A new {@link Coordinate} made from adding the offset-x to the base x and offset-y to the base y.
     */
    public Coordinate resolve(int x, int y) {
        return new Coordinate(this.x + x, this.y + y);
    }

    public Coordinate resolve(Direction direction) {
        return resolve(direction.x, direction.y);
    }

    /**
     * Finds the Manhattan distance from this coordinate to (0, 0).
     *
     * @return The Manhattan distance from this coordinate to (0, 0).
     */
    public int distance() {
        return distance(0, 0);
    }

    /**
     * Finds the Manhattan distance from this coordinate to (x2, y2).
     *
     * @param x2 The x of the other coordinate.
     * @param y2 The y of the other coordinate.
     * @return The Manhattan distance from this coordinate to (x2, y2).
     */
    public int distance(int x2, int y2) {
        return Math.abs(x - x2) + Math.abs(y - y2);
    }

    /**
     * Finds the Manhattan distance from this coordinate to the other coordinate specified.
     *
     * @param other The coordinate object to compare with.
     * @return The Manhattan distance from this coordinate to the other coordinate specified.
     */
    public int distance(Coordinate other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public static Coordinate parse(String coord) {
        try {
            String[] arr = coord.split(",");
            int x = Integer.parseInt(arr[0]);
            int y = Integer.parseInt(arr[1]);
            return new Coordinate(x, y);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new Coordinate(0, 0);
        }
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
