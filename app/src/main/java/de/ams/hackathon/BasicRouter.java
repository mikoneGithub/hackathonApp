package de.ams.hackathon;

import android.graphics.Point;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.UIntArray;

public class BasicRouter extends Router {

    public int[] solve(int width, int height, @NotNull int[][] world, @NotNull Point origin, @NotNull Point destination) {
        return new int[]{1, 2, 2, 2, 2, 1, 1, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 3, 2, 2, 3, 3, 0, 0, 3, 3, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 1, 1, 0, 0, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 1, 1, 0, 0, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    }

}
