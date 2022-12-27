import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState


@Composable
fun PieceCanvas(pieceId: Int) = Canvas(
    modifier = Modifier.size(1500.dp, 800.dp).background(Color(0.95f, 0.95f, 0.95f))
) {
    val piece = when (pieceId) {
        0 -> p1
        1 -> p2
        2 -> p3
        3 -> p4
        else -> throw Error("Unknown piece id $pieceId")
    }
    piece.variants.reversed().forEachIndexed { id, path ->
        drawPathWithRectangles(path, Offset(100f + id * 250, 150f))
    }
}

const val RECTANGLE_SIZE = 20f

@Composable
fun BoardCanvas(board: Board) = Canvas(
    modifier = Modifier.size(500.dp, 500.dp)
) {
    board.insidePoints.forEach { pt ->
        drawRect(Color.LightGray, topLeft = Offset( RECTANGLE_SIZE * pt.x, 500f - RECTANGLE_SIZE * pt.y), size=Size(RECTANGLE_SIZE, RECTANGLE_SIZE))
    }
    val colors = listOf(
        Color(255, 224, 140),
        Color(143, 106, 7),
        Color(133, 26, 9),
        Color(46, 18, 5)
    )
    board.pieces.forEach { boardPiece ->
        val move = boardPiece
        val piece = getPiece(move.pieceId)
        val path = piece.getVariantId(move.variantNumber)
        val off = move.offset
        drawPathWithRectangles(path, Offset(RECTANGLE_SIZE*off.x, 500f - RECTANGLE_SIZE*off.y), colors[boardPiece.pieceId])
    }
}

fun DrawScope.drawPathWithRectangles(path: List<Point>, offset: Offset, color: Color = Color.DarkGray) {
    val offsets = path.map { p ->
        offset + Offset(p.x * RECTANGLE_SIZE, -p.y * RECTANGLE_SIZE)
    }
    for (o in offsets) {
        drawRect(color, topLeft = o, size = Size(RECTANGLE_SIZE, RECTANGLE_SIZE))
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 2000.dp, height = 1000.dp)
    ) {
        val pieceId = remember { mutableStateOf(0) }
        val solutions = solvePossibleSolutions()
        MaterialTheme {
            Row(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
//                PieceCanvas(pieceId.value)
                BoardCanvas(solutions[pieceId.value])
                Button(modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        pieceId.value = (pieceId.value + 1).mod(solutions.size)
                    }) {
                    Text("Piece (${pieceId.value})")
                }
            }
        }
    }
}