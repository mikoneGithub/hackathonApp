package de.ams.hackathon

import android.graphics.Point

// walkable == 0bxxxxxxx1
val fWall = 0
val fEmpty = 1
val fCoin = 3
val fOrigin = 7
val fDestination = 5

val levels = longArrayOf(
    33, 0x1ffffffff, 0x100111004, 0x1df7557d5, 0x110455455, 0x15fdd55d5, 0x150115111, 0x177f75d7f, 0x114004101, 0x155df7ffd, 0x144410505, 0x1f77df575, 0x110441111, 0x15f77df5d, 0x141141145, 0x17dd5f5d5, 0x115110511, 0x1d5775d77, 0x111544541, 0x17d55557f, 0x144455511, 0x177df5d55, 0x144004145, 0x15fdfdffd, 0x140104105, 0x177d5fd75, 0x104544141, 0x1dd575d75, 0x111104415, 0x177fd77d5, 0x105054455, 0x1dd755d55, 0x110111, 0x1ffffffff)

class Engine {

    val north = 0
    val east = 1
    val south = 2
    val west = 3

    val rows = 33
    val columns = 33

    val origin: Point = Point(0, 1)
    val destination: Point = Point(columns - 1, rows - 2)
    var path: IntArray = intArrayOf()
    var step = 0

    val world = Array(columns) { IntArray(rows) }
    var position = Point(origin.x, origin.y)
    var lastPosition = Point(origin.x, origin.y)

    fun reset() {
        val level = 0

        for (y in 0 until rows - 1) {
            var row = levels[1 + (1 + rows) * level + y]

            for (x in 0 until columns - 1) {
                world[x][y] = if ((row % 2L) == 1L) fWall else fEmpty

                row = row shr 1
            }
        }

        world[0][1] = fOrigin
        world[columns-1][rows-2] = fDestination

        return
    }

    fun step() : Boolean {
        print(step)
        print(path.size)

        if (step >= path.size) {
            step = 0
            position = Point(origin.x, origin.y)
            return false
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
                return false
            } else {
                lastPosition = Point(old.x, old.y)
                world[position.x][position.y] = 2
            }

        step++
        return true

        /*
        for (x in 0..rows - 1) {
           for (y in 0..columns - 1) {
              world[x][y] = Random.nextInt(0, 3)
           }
        }
        */

    }

}