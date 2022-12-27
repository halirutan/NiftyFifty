data class Point(val x: Int, val y: Int)
data class Piece(val name: String, val path: List<Point>) {
    val variants = listOf(
        path,
        path.reversed(),
        path.map { (x, y) -> Point(x, -y) },
        path.map { (x, y) -> Point(x, -y) }.reversed()
    )
}

fun pathFromAlternatingLengths(lengths: List<Int>): List<Point> {
    var isXDir = true
    val result = mutableListOf<Point>()
    for (l in lengths) {
        val p = if (isXDir) Point(1, 0) else Point(0, 1)
        result.addAll(List(l) { p })
        isXDir = !isXDir
    }
    return result
}

val p1 = Piece("BrightWood", pathFromAlternatingLengths(listOf(6, 3, 2, 1, 2, 4, 3, 3, 4)))
val p2 = Piece("StripedWood", pathFromAlternatingLengths(listOf(4, 4, 3, 2, 2, 1, 1, 3, 2, 1, 2)))
val p3 = Piece("RedWood", pathFromAlternatingLengths(listOf(4, 1, 2, 3, 1, 3, 2, 1, 3, 2, 3, 2, 2)))
val p4 = Piece("DarkWood", pathFromAlternatingLengths(listOf(4, 2, 3, 3, 1, 2, 4, 2, 1, 3, 3)))


fun main(args: Array<String>) {
    println("Hello World!")
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}