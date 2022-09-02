package de.ams.hackathon;

import android.graphics.Point;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class AdvancedRouter extends Router {
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

    private void clearDistances() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.distance[x][y] = 0xffff;
            }
        }
    }

    public int[] solve(int width, int height, @NotNull int[][] world, @NotNull Point origin, @NotNull Point destination) {
        this.world = Arrays.stream(world).map(int[]::clone).toArray(int[][]::new);
        this.distance = new int[width][height];
        this.width = width;
        this.height = height;

        // roll up the path
        ArrayList<Integer> path = new ArrayList();

        // count the coins
        int coins = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (this.world[x][y] == fCoin) {
                    coins++;
                }
            }
        }

        int oX = origin.x;
        int oY = origin.y;

        boolean exiting = false;

        // collect all coins
        while (coins > 0) {
            int dist = 9999;
            int cx = 0, cy = 0;

            // find the path to the closest coin
            clearDistances();
            handleNeighbors(oX, oY, -1);

            // find the closest coin
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (this.world[x][y] == fCoin && distance[x][y] < dist) {
                        cx = x;
                        cy = y;
                        dist = distance[x][y];
                    }
                }
            }
            System.out.print("Going for coin at ");
            System.out.print(cx);
            System.out.print(":");
            System.out.println(cy);

            int cost = distance[cx][cy];
            this.world[cx][cy] = fEmpty;

            int nextX = cx;
            int nextY = cy;

            ArrayList<Integer> segment = new ArrayList();

            while (cx != oX || cy != oY) {
                cost--;

                // find a location with cost - 1
                if ((cx > 0) && (this.distance[cx - 1][cy] == cost)) {
                    cx--;
                    segment.add(Router.East);
                }
                else if ((cy > 0) && (this.distance[cx][cy - 1] == cost)) {
                    cy--;
                    segment.add(Router.South);
                }
                else if ((cx < width - 1) && (this.distance[cx + 1][cy] == cost)) {
                    cx++;
                    segment.add(Router.West);
                }
                else if ((cy < height - 1) && (this.distance[cx][cy + 1] == cost)) {
                    cy++;
                    segment.add(Router.North);
                }
            }

            for (int idx = segment.size() - 1; idx >= 0; idx--) {
                path.add(segment.get(idx));
            }

            oX = nextX;
            oY = nextY;

            coins--;

            // hack to chase the exit
            if (coins == 0 && !exiting) {
                exiting = true;
                coins = 1;
                this.world[destination.x][destination.y] = fCoin;
            }
        }

        // generate the path output
        int[] output = new int[path.size()];

        for (int idx = 0; idx < path.size(); idx++) {
            output[idx] = path.get(idx);
        }

        return output;
    }

}

