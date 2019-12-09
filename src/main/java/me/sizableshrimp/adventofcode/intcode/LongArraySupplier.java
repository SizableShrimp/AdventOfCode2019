/*
 * Copyright (c) 2019 SizableShrimp.
 * This file is provided under version 3 of the GNU Lesser General Public License.
 */

package me.sizableshrimp.adventofcode.intcode;

import lombok.RequiredArgsConstructor;

import java.util.function.LongSupplier;

@RequiredArgsConstructor
public class LongArraySupplier implements LongSupplier {
    private final long[] array;
    private int index;

    @Override
    public long getAsLong() {
        return array[index++];
    }
}
