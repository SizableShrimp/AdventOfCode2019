/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.use;

import lombok.RequiredArgsConstructor;

import java.util.function.IntSupplier;

@RequiredArgsConstructor
public class IntArraySupplier implements IntSupplier {
    private final int[] array;
    private int index;

    @Override
    public int getAsInt() {
        if (index == array.length)
            return array[array.length-1];
        return array[index++];
    }
}
