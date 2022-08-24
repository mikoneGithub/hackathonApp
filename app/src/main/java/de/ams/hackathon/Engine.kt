package de.ams.hackathon

import android.graphics.Point
import kotlin.random.Random

val fWall = 0
val fEmpty = 1
val fCoin = 3
val fDestination = 5

class Engine {

    val north = 0
    val east = 1
    val south = 2
    val west = 3

    val rows = 20
    val columns = 20

    val origin: Point = Point(1, 1)
    val destination: Point = Point(9, 9)
    var path: IntArray = intArrayOf()
    var step = 0

    val world = Array(columns) { IntArray(rows) }
    var position = Point(origin.x, origin.y)
    var lastPosition = Point(origin.x, origin.y)

    fun reset() {
        for (x in 0..rows - 1) {
            for (y in 0..columns - 1) {
                if ((x == 0) or (y == 0) or (x == columns - 1) or (y == rows - 1)) {
                    world[x][y] = fWall
                } else {
                    world[x][y] = fEmpty
                }
            }
        }
    }

    fun step() {
        reset()

        print(step)
        print(path.size)

        if (step >= path.size) {
            step = 0
            position = Point(origin.x, origin.y)
            return
        }

        val old = Point(position.x, position.y)
        world[position.x][position.y] = 1

        when (path[step]) {
            0 -> position.y -= 1
            1 -> position.x += 1
            2 -> position.y += 1
            3 -> position.x -= 1
        }

        if ((position.x >= columns) or (position.y >= rows) or (position.x < 0) or (position.y < 0)) {
            print("You are out of bounds")
            position = old
        } else
        // all walkable fields have bit 0 set
            if (world[position.x][position.y] and 1 == 0) {
                // fail
                print("You walked into a wall")
                position = old
            } else {
                lastPosition = Point(old.x, old.y)
                world[position.x][position.y] = 2
            }

        step++

        /*
        for (x in 0..rows - 1) {
           for (y in 0..columns - 1) {
              world[x][y] = Random.nextInt(0, 3)
           }
        }
        */

    }

}