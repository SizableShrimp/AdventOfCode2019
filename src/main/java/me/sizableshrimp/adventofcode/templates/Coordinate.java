/*
 * AdventOfCode2019
 * Copyright (C) 2022 SizableShrimp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.sizableshrimp.adventofcode.templates;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Arrays;
import java.util.Map;

/**
 * A 2-dimensional coordinate object that holds an x and a y value.
 */
@Value
public class Coordinate {
    public static final Coordinate ZERO = new Coordinate(0, 0);
    public int x, y;

    /**
     * The four cardinal directions, associated with degrees and relative x, y positions
     * where NORTH is at 0 degrees and a relative x,y of (0,-1).
     */
    @AllArgsConstructor
    public enum Direction {
        NORTH(0, 0, -1), EAST(90, 1, 0), SOUTH(180, 0, 1), WEST(270, -1, 0);

        public final int degrees;
        public final int x;
        public final int y;

        public static Direction getDirection(int degrees) {
            if (degrees < 0)
                degrees = 360 + degrees;
            degrees %= 360;
            for (Direction direction : values()) {
                if (direction.degrees == degrees)
                    return direction;
            }

            return Direction.NORTH;
        }

        public static Map<Character, Direction> getDirections(char up, char right, char down, char left) {
            return Map.of(up, NORTH, right, EAST, down, SOUTH, left, WEST);
        }

        public Direction opposite() {
            return values()[(ordinal() + 2) % values().length];
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

    public Coordinate resolve(Coordinate other) {
        return resolve(other.x, other.y);
    }

    public Coordinate resolve(Direction direction) {
        return resolve(direction.x, direction.y);
    }

    /**
     * Finds the Manhattan distance from this coordinate to (0, 0).
     *
     * @return The Manhattan distance from this coordinate to (0, 0).
     */
    public int distanceZero() {
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
        return distance(other.x, other.y);
    }

    /**
     * Finds the relative {@link Direction Direction} needed to get from this coordinate to the other coordinate specified.
     * Returns null if the two coordinates are not within one tile in cardinal directions away.
     *
     * @param other Another coordinate used to find the relative direction towards.
     * @return The {@link Direction Direction} needed to move from this coordinate to the other coordinate specified,
     * or null if this is not possible in one move in cardinal directions.
     */
    public Direction relative(Coordinate other) {
        return Arrays.stream(Direction.values()).filter(d -> resolve(d).equals(other)).findAny().orElse(null);
    }

    /**
     * Checks if both x and y values are in the range 0, inclusive, to {@code size}, exclusive.
     *
     * @param size The size dimensions to check.
     * @return Whether both x and y values fit the constraints of the range.
     */
    public boolean isValid(int size) {
        return this.x >= 0 && this.x < size && this.y >= 0 && this.y < size;
    }

    /**
     * Parses a coordinate in the format "x,y".
     *
     * @param coord The input string of which to parse a coordinate.
     * @return A new {@link Coordinate} object.
     */
    public static Coordinate parse(String coord) {
        String[] arr = coord.split(",");
        int x = Integer.parseInt(arr[0]);
        int y = Integer.parseInt(arr[1]);
        return new Coordinate(x, y);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }
}
