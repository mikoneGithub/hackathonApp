package de.ams.hackathon;

import android.graphics.Point;

import org.jetbrains.annotations.NotNull;

public class Router {

    public static final int North = 0;
    public static final int East = 1;
    public static final int South = 2;
    public static final int West = 3;

    public static final int fWall = 0;
    public static final int fEmpty = 1;
    public static final int fCoin = 3;
    public static final int fDestination = 5;
    public static final int fOrigin = 7;

    public int[] solve(int width, int height, @NotNull int[][] world, @NotNull Point origin, @NotNull Point destination) {
        return new int[0];
    }

}
