package game

import javax.swing.JFrame

class SnakeGame : JFrame() {

    init {
        add(GameBoard())
        isResizable = false
        pack()
        title = "Kotlin Snake"
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
    }

}