package com.example.tictactoeonline.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoeonline.data.GameState

const val PADDING = 10

@Composable
fun DrawBoard(
    state : GameState,
    modifier : Modifier = Modifier,
    playerXColor : Color = Color.Magenta,
    playerOColor : Color = Color.Blue,
    onTapField : (x: Int , y: Int) -> Unit

){

   Canvas(modifier = modifier
       .pointerInput(true){
            detectTapGestures {
                val x = 3 * it.x.toInt() / size.width
                val y = 3 * it.y.toInt() / size.height
                onTapField(x, y)
            }
   }){

       val tileSize = size.height / 4f

        drawField()


        state.field.forEachIndexed { indexY, chars ->

            chars.forEachIndexed { indexX, c ->

                c?.let {char ->

                    val x = calculateOffset(indexX)
                    val y = calculateOffset(indexY)

                    val center = Offset(
                        x = size.width * (x / 6f),
                        y = size.width * (y / 6f)

                    )

                    if(char == 'X')
                        drawX(
                            color = playerXColor,
                            size = Size(tileSize, tileSize),
                            center = center
                        )

                    else
                        drawO(
                            color = playerOColor,
                            size = Size(tileSize, tileSize),
                            center = center
                        )
                }

            }

        }

   }

}

private fun DrawScope.drawO(
    color: Color,
    center : Offset,
    size : Size
){

    drawCircle(color = color,
            center = center,
            radius = size.width /2f,
            style = Stroke(width = 4.dp.toPx())
        )

}


private fun DrawScope.drawX(
    color: Color,
    center : Offset,
    size : Size
){


    drawLine(
        color = color,
        start = Offset(
            x = center.x - size.width / 2f,
            y = center.y - size.height / 2f
        ),
        end = Offset(
            x = center.x + size.width / 2f,
            y = center.y + size.height / 2f
        ),
        strokeWidth = 4.dp.toPx(),
        cap = StrokeCap.Round

    )

    drawLine(
        color = color,
        start = Offset(
            x = center.x - size.width / 2f,
            y = center.y + size.height / 2f
        ),
        end = Offset(
            x = center.x + size.width / 2f,
            y = center.y - size.height / 2f
        ),
        strokeWidth = 4.dp.toPx(),
        cap = StrokeCap.Round
    )


}


private fun DrawScope.drawField(){

    drawLine(
        color = Color.Black,
        start = Offset(
            x = size.width * (1 / 3f),
            y = 0f
        ),
        end = Offset(
            x = size.width * (1 / 3f),
            y = size.height
        ),
        cap = StrokeCap.Round,
        strokeWidth = 3.dp.toPx()
    )

    drawLine(
        color = Color.Black,
        start = Offset(
            x = size.width * (2 / 3f),
            y = 0f
        ),
        end = Offset(
            x = size.width * (2 / 3f),
            y = size.height
        ),
        cap = StrokeCap.Round,
        strokeWidth = 3.dp.toPx()
    )

    drawLine(
        color = Color.Black,
        start = Offset(
            x = 0f,
            y = size.height * (1 / 3f)
        ),
        end = Offset(
            x = size.width,
            y = size.height * (1 / 3f)
        ),
        cap = StrokeCap.Round,
        strokeWidth = 3.dp.toPx()
    )

    drawLine(
        color = Color.Black,
        start = Offset(
            x = 0f,
            y = size.height * (2 / 3f)
        ),
        end = Offset(
            x = size.width,
            y = size.height * (2 / 3f)
        ),
        cap = StrokeCap.Round,
        strokeWidth = 3.dp.toPx()
    )




}

@Preview(showBackground = true)
@Composable
private fun ShowField(){
    DrawBoard(
        state = GameState(
            field = arrayOf(
                arrayOf('O', null, 'X'),
                arrayOf(null, 'O', null),
                arrayOf('X', null, 'X')
            )
        ),
        modifier = Modifier.size(300.dp).padding(10.dp),
        onTapField = { _, _ -> }
    )

}

private fun calculateOffset(index : Int) : Int{
        return when(index){
            0 -> 1
            1 -> 3
            else -> 5
        }

}