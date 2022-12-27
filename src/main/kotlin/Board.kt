data class Point(val x: Int, val y: Int)
data class Piece(val name: String, val path: List<Point>) {
    val variants = path2Points(path).let { p ->
        listOf(
            p,
            p.map { (x, y) -> Point(-y, x) },
            p.map { (x, y) -> Point(-x, -y) },
            p.map { (x, y) -> Point(y, -x) },
            p.map { (x, y) -> Point(x, -y) },
            p.map { (x, y) -> Point(y, x) },
            p.map { (x, y) -> Point(-x, y) },
            p.map { (x, y) -> Point(-y, -x) },
        )
    }

    fun getVariantId(id: Int): List<Point> {
        assert(id >= 0 && id < variants.size)
        return variants[id]
    }

    private fun path2Points(p: List<Point>): List<Point> {
        return p.runningFold(Point(0, 0)) { acc, pt ->
            Point(acc.x + pt.x, acc.y + pt.y)
        }
    }
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

val p1 = Piece("BrightWood", pathFromAlternatingLengths(listOf(5, 3, 2, 1, 2, 4, 3, 3, 4)))
val p2 = Piece("StripedWood", pathFromAlternatingLengths(listOf(3, 4, 3, 2, 2, 1, 2, 1, 1, 3, 2, 1, 2)))
val p3 = Piece("RedWood", pathFromAlternatingLengths(listOf(3, 1, 2, 3, 1, 3, 2, 1, 3, 2, 3, 2, 2)))
val p4 = Piece("DarkWood", pathFromAlternatingLengths(listOf(3, 2, 3, 3, 1, 2, 4, 2, 1, 3, 3)))

fun getPiece(pieceId: Int): Piece {
    return when (pieceId) {
        0 -> p1
        1 -> p2
        2 -> p3
        3 -> p4
        else -> throw Error("Invalid piece ID. Only 0-3 does exist")
    }
}

data class PieceOnBoard(val pieceId: Int, val variantNumber: Int, val offset: Point)

data class Board(val insidePoints: Set<Point>, val pieces: List<PieceOnBoard>) {

    fun getPossibleMoves(pieceId: Int): List<PieceOnBoard> {
        val result = mutableListOf<PieceOnBoard>()
        val variants = getPiece(pieceId).variants
        variants.forEachIndexed { varId, points ->
            insidePoints.forEach { insidePt ->
                val elements = points.map { pt ->
                    Point(insidePt.x + pt.x, insidePt.y + pt.y)
                }
                if (insidePoints.containsAll(elements)) {
                    val clash = pieces.fold(false) { acc, p ->
                        acc || doPiecesClash(elements, p)
                    }
                    if (!clash) {
                        result.add(PieceOnBoard(pieceId, varId, insidePt))
                    }
                }
            }
        }
        return result
    }

    private fun doPiecesClash(piece1Points: List<Point>, existingPiece: PieceOnBoard): Boolean {
        val existing = getPiece(existingPiece.pieceId).getVariantId(existingPiece.variantNumber)
        val off = existingPiece.offset
        val existingPositions = existing.map { pt ->
            Point(off.x + pt.x, off.y + pt.y)
        }
        piece1Points.forEach {
            if (existingPositions.contains(it)) {
                return true
            }
        }
        return false
    }

}

fun boardFromOffsetsAndLengths(input: List<Pair<Int, Int>>): Set<Point> {
    val result = mutableSetOf<Point>()
    input.forEachIndexed { idY, d ->
        val (offset, length) = d
        result.addAll(
            List(length) { idX -> Point(offset + idX, idY) }
        )
    }
    return result
}

val defaultBoard = Board(
    boardFromOffsetsAndLengths(
        listOf(
            Pair(0, 9),
            Pair(0, 9),
            Pair(0, 10),
            Pair(0, 10),
            Pair(0, 10),
            Pair(0, 13),
            Pair(0, 14),
            Pair(1, 15),
            Pair(3, 13),
            Pair(3, 15),
            Pair(3, 16),
            Pair(5, 14),
            Pair(5, 14),
            Pair(5, 14),
            Pair(6, 13),
            Pair(9, 10),
            Pair(11, 8),
            Pair(11, 8),
            Pair(11, 8),
        )
    ), mutableListOf()
)

fun solvePossibleSolutions(): List<Board> {
    val result = mutableListOf<Board>()
    val stack = ArrayDeque<Board>()
    stack.add(defaultBoard)
    var iter = 0
    while (stack.isNotEmpty()) {
        val current = stack.removeFirst()
        val (insidePoints, pieces) = current
//        if (pieces.size == 3) {
//            result.add(current)
//            print("X")
//            continue
//        }

        if (pieces.size == 4) {
            result.add(current)
            print("X")
            continue
        }
        if (iter.mod(100) == 0) {
            println()
            print("(${stack.size}) ")
        }
        print(".")
        current.getPossibleMoves(pieces.size).forEach {
            val newPieces = pieces.toMutableList()
            newPieces.add(it)
            stack.addFirst(Board(insidePoints, newPieces))
        }
        iter++
    }
    println()
    println("Number of 3-piece solutions: ${result.size}")
    return result
}

fun main() {

}