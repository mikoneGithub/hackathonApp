package de.ams.hackathon

import android.graphics.Point

// walkable == 0bxxxxxxx1
val fWall = 0
val fEmpty = 1
val fCoin = 3
val fOrigin = 7
val fDestination = 5

val levels = longArrayOf(
    33, 0x1ffffffff, 0x100111004, 0x1df7557d5, 0x110455455, 0x15fdd55d5, 0x150115111, 0x177f75d7f, 0x114004101, 0x155df7ffd, 0x144410505, 0x1f77df575, 0x110441111, 0x15f77df5d, 0x141141145, 0x17dd5f5d5, 0x115110511, 0x1d5775d77, 0x111544541, 0x17d55557f, 0x144455511, 0x177df5d55, 0x144004145, 0x15fdfdffd, 0x140104105, 0x177d5fd75, 0x104544141, 0x1dd575d75, 0x111104415, 0x177fd77d5, 0x105054455, 0x1dd755d55, 0x000110111, 0x1ffffffff,
    33, 0x1ffffffff, 0x140004000, 0x15ff557dd, 0x144151445, 0x175ddd5d5, 0x145111115, 0x15dd7fd75, 0x104500111, 0x1ff5f7fdd, 0x100410055, 0x17ffdfdd5, 0x144050111, 0x157d57f7f, 0x110510101, 0x17f5f77fd, 0x104514445, 0x175d55d55, 0x104545151, 0x1775dd75f, 0x144411041, 0x155f55d5d, 0x115044551, 0x1fd7f7d75, 0x101114545, 0x17fd5d55d, 0x140141015, 0x17df7f7f5, 0x145140441, 0x15555dd7d, 0x114444545, 0x177ffd575, 0x010001101, 0x1ffffffff,
    33, 0x1ffffffff, 0x110404040, 0x1d75f7775, 0x115410545, 0x15555fd5f, 0x151541141, 0x17f75d575, 0x101444515, 0x17d57fddd, 0x145500451, 0x15d55f5d7, 0x145550111, 0x1755d7575, 0x105114545, 0x1f575d575, 0x144051541, 0x157f7775d, 0x110405445, 0x1f75dd5d5, 0x104111515, 0x17fd755f5, 0x101505415, 0x1fd5fd555, 0x101101145, 0x17777ff7d, 0x114440045, 0x1d5ddfdd5, 0x110441115, 0x17df7f75d, 0x145111545, 0x1575d5575, 0x010404011, 0x1ffffffff,
    33, 0x1ffffffff, 0x111400000, 0x1555ffdfd, 0x155510415, 0x15555df55, 0x144044055, 0x17fff77d5, 0x100000401, 0x17ffff5fd, 0x110404401, 0x1d5df7ffd, 0x105110001, 0x17f75fd7f, 0x144540511, 0x1f5d7f555, 0x105141545, 0x17555d5f5, 0x101551515, 0x1ff55755d, 0x100551141, 0x17dd7df7d, 0x140504041, 0x15f5f57fd, 0x151105405, 0x155f7d5f5, 0x144045105, 0x177fd5f7d, 0x140014451, 0x1777d75d7, 0x114510505, 0x1ddd7fd7d, 0x001000001, 0x1ffffffff,
    33, 0x1ffffffff, 0x114000000, 0x155ff7ffd, 0x144010101, 0x17f7dfddf, 0x110450441, 0x157d5777d, 0x140551141, 0x17fd5d75f, 0x110411451, 0x1d7555dd5, 0x151145115, 0x15df7d7d5, 0x155111045, 0x1557d775d, 0x105450451, 0x1fd5575d7, 0x101111141, 0x177fd575d, 0x144044411, 0x15df5fd77, 0x144541101, 0x17755777d, 0x144554445, 0x17dddddd5, 0x104101551, 0x1f7ddf55d, 0x110441555, 0x17f77d555, 0x111041045, 0x155fdffd5, 0x044000011, 0x1ffffffff,
    33, 0x1ffffffff, 0x100004010, 0x17f77d7dd, 0x111141001, 0x1ddddff5d, 0x150400141, 0x1577fff5f, 0x155410051, 0x1557d7dd5, 0x155150405, 0x155d5f7ff, 0x144541101, 0x17d57d57d, 0x141141411, 0x15ff5fff5, 0x144050005, 0x175ddf7dd, 0x104511005, 0x1d5d75f55, 0x110144155, 0x17f75fd5d, 0x104451051, 0x15ddd5fd7, 0x141114115, 0x17f757d55, 0x141140455, 0x1dddf7555, 0x114510551, 0x17755fd7d, 0x101140111, 0x1fdf7ff57, 0x000000041, 0x1ffffffff,
    33, 0x1ffffffff, 0x140041040, 0x15f7ddf57, 0x151044111, 0x15dd77dfd, 0x150114405, 0x1575d5df5, 0x110501005, 0x17757f7fd, 0x144044401, 0x17df75f77, 0x101105151, 0x1ffdf755d, 0x100051505, 0x1d5f5d5f5, 0x114151505, 0x175d575f5, 0x115510415, 0x1dd5fffd5, 0x111500055, 0x17755f7d5, 0x140540015, 0x15fd7fffd, 0x154111041, 0x155dd755d, 0x105510555, 0x1dd577d55, 0x101140111, 0x17f77f5f7, 0x141141415, 0x15df5d575, 0x004010501, 0x1ffffffff,
    33, 0x1ffffffff, 0x104011010, 0x157dd57d5, 0x150044445, 0x155f5ff7d, 0x154540405, 0x15d57f5dd, 0x145001151, 0x1757fdf57, 0x111410011, 0x1df5dfd7d, 0x111044541, 0x17dff55dd, 0x101015411, 0x1fd7d57f7, 0x100105411, 0x17ddfd7dd, 0x145441041, 0x1d575f75f, 0x114114441, 0x177df55dd, 0x151041005, 0x15ff7f7fd, 0x144104045, 0x155df5fd5, 0x114451011, 0x1f775d7fd, 0x114541011, 0x175d7ffdf, 0x104000441, 0x17d7ff57d, 0x001000101, 0x1ffffffff,
    33, 0x1ffffffff, 0x111000010, 0x1d57fdd75, 0x114444505, 0x177dd75fd, 0x110411111, 0x1df57dd57, 0x111544451, 0x1755577dd, 0x114151101, 0x155d57d75, 0x145110145, 0x1fd7f7f5d, 0x101045045, 0x177ddd7f7, 0x144110401, 0x17dd75ddd, 0x100444155, 0x177df5f55, 0x110505105, 0x17755d5fd, 0x145151505, 0x15dfd7575, 0x145044445, 0x17555dfdd, 0x115551045, 0x1755577d5, 0x144554411, 0x1dfd7557d, 0x110545505, 0x17755dddd, 0x004110041, 0x1ffffffff,
    33, 0x1ffffffff, 0x111104000, 0x15555dfdd, 0x144445015, 0x17fff57f5, 0x100011041, 0x17f5dfd7f, 0x141504501, 0x1dd77d7f5, 0x115111115, 0x1775d7d5d, 0x100411541, 0x17f7dd57d, 0x141045041, 0x15d7757df, 0x115514111, 0x1f55d7d75, 0x105041145, 0x175fff75d, 0x114400411, 0x1575ffdf5, 0x145444405, 0x1757557fd, 0x155051111, 0x155fddd57, 0x141050455, 0x17f7577d5, 0x110414515, 0x157ddd555, 0x144111455, 0x17f557575, 0x000444101, 0x1ffffffff,
)

class Engine {

    val north = 0
    val east = 1
    val south = 2
    val west = 3

    val rows = 33
    val columns = 33
    var level = 0

    val origin: Point = Point(0, 1)
    val destination: Point = Point(columns - 1, rows - 2)
    var path: IntArray = intArrayOf()
    var step = 0

    val world = Array(columns) { IntArray(rows) }
    var position = Point(origin.x, origin.y)
    var lastPosition = Point(origin.x, origin.y)

    fun reset() {
        for (y in 0 until rows - 1) {
            var row = levels[1 + (1 + rows) * level + y]

            for (x in 0 until columns - 1) {
                world[x][y] = if ((row % 2L) == 1L) fWall else fEmpty

                row = row shr 1
            }
        }

        world[0][1] = fOrigin
        world[columns-1][rows-2] = fDestination

        position = Point(origin.x, origin.y)
        lastPosition = Point(origin.x, origin.y)
        step = 0

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