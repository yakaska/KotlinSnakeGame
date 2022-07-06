import game.SnakeGame
import java.awt.EventQueue

fun main() {
    EventQueue.invokeLater {
        val game = SnakeGame()
        game.isVisible = true
    }
}