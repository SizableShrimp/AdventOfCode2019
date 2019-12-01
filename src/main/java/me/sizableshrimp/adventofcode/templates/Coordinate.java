package me.sizableshrimp.adventofcode.templates;

import lombok.Value;

/**
 * A 2-dimensional coordinate object that holds an x and a y value.
 */
@Value
public class Coordinate {

    public int x, y;

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

    public int distance(int x2, int y2) {
        return Math.abs(x - x2) + Math.abs(y - y2);
    }

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
