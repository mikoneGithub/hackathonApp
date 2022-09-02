package de.ams.hackathon;

import android.graphics.Point;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.UIntArray;

public class BasicRouter extends Router {

    private int[][] world;
    private int[][] distance;

    private int width;
    private int height;

    public void handleNeighbors(int x, int y, int cost) {
        if (x >= 0 && y >= 0 && x < this.width && y < this.height && world[x][y] % 2 == 1 && cost < distance[x][y]) {
            distance[x][y] = cost;
            cost += 1;

            handleNeighbors(x - 1, y, cost);
            handleNeighbors(x + 1, y, cost);
            handleNeighbors(x, y - 1, cost);
            handleNeighbors(x, y + 1, cost);
        }
    }

    public int[] solve(int width, int height, @NotNull int[][] world, @NotNull Point origin, @NotNull Point destination) {
        this.world = world;
        this.distance = new int[width][height];
        this.width = width;
        this.height = height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.distance[x][y] = 0xffff;
            }
        }

        handleNeighbors(origin.x, origin.y, -1);

        // roll up the path
        ArrayList<Integer> path = new ArrayList();
        int x = destination.x;
        int y = destination.y;
        int cost = distance[x][y];

        while (x != origin.x || y != origin.y) {
            System.out.print(x);
            System.out.print(":");
            System.out.println(y);
            cost--;

            // find a location with cost - 1
            if ((x > 0) && (this.distance[x - 1][y] == cost)) {
                x--;
                path.add(Router.East);
            }
            else if ((y > 0) && (this.distance[x][y - 1] == cost)) {
                y--;
                path.add(Router.South);
            }
            else if ((x < width - 1) && (this.distance[x + 1][y] == cost)) {
                x++;
                path.add(Router.West);
            }
            else if ((y < height - 1) && (this.distance[x][y + 1] == cost)) {
                y++;
                path.add(Router.North);
            }
        }

        int[] output = new int[path.size()];

        for (int idx = 1; idx <= path.size(); idx++) {
            output[idx - 1] = path.get(path.size() - idx);
        }

        return output;
    }

}
