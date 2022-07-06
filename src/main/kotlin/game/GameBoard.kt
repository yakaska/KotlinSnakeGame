package game

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.ImageIcon
import javax.swing.JPanel
import javax.swing.Timer
import kotlin.random.Random

class GameBoard : JPanel(), ActionListener {

    private companion object {
        const val WIDTH = 300
        const val HEIGHT = 300
        const val DOT_SIZE = 10
        const val ALL_DOTS = (WIDTH * HEIGHT) / (DOT_SIZE * DOT_SIZE)
        const val DELAY = 140
    }

    private val x = Array(ALL_DOTS) { -1 }
    private val y = Array(ALL_DOTS) { -1 }

    private var bodyLength: Int = 0
    private var appleX: Int = 0
    private var appleY: Int = 0

    private var rightDirection = false
    private var leftDirection = false
    private var upDirection = false
    private var downDirection = false

    private var inGame = true

    private val timer: Timer = Timer(DELAY, this)
    private lateinit var body: Image
    private lateinit var apple: Image
    private lateinit var head: Image

    init {
        addKeyListener(MoveAdapter())
        background = Color.BLACK
        isFocusable = true
        preferredSize = Dimension(WIDTH, HEIGHT)

        loadImages()
        initGame()
    }

    private fun loadImages() {
        var loader = ImageIcon("src/main/resources/body.png")
        body = loader.image
        loader = ImageIcon("src/main/resources/head.png")
        head = loader.image
        loader = ImageIcon("src/main/resources/apple.png")
        apple = loader.image
    }

    private fun initGame() {
        bodyLength = 3
        for (z in 0..bodyLength) {
            x[z] = 50 - z * 10
            y[z] = 50
        }
        placeApple()

        timer.start()
    }

    override fun paintComponent(graphics: Graphics?) {
        super.paintComponent(graphics)
        updateScreen(graphics!!)
    }

    private fun updateScreen(graphics: Graphics) {
        if (inGame) {
            graphics.drawImage(apple, appleX, appleY, this)
            for (z in 0..bodyLength) {
                if (z == 0) {
                    graphics.drawImage(head, x[z], y[z], this)
                } else {
                    graphics.drawImage(body, x[z], y[z], this)
                }
            }

            Toolkit.getDefaultToolkit().sync()
        } else {
            gameOver(graphics)
        }
    }

    private fun gameOver(graphics: Graphics) {
        val message = "Game Over!"
        val font = Font("GameOver", Font.BOLD, 14)
        val metrics = getFontMetrics(font)
        graphics.color = Color.RED
        graphics.font = font
        graphics.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2)
    }

    private fun placeApple() {
        var randInt = (Random.nextInt(WIDTH) / DOT_SIZE)
        appleX = randInt * DOT_SIZE
        randInt = (Random.nextInt(HEIGHT) / DOT_SIZE)
        appleY = randInt * DOT_SIZE
    }

    override fun actionPerformed(p0: ActionEvent?) {
        if (inGame) {
            checkApple()
            checkCollision()
            move()
        }
        repaint()
    }

    private fun checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyLength++
            placeApple()
        }
    }

    private fun checkCollision() {
        for (z in bodyLength downTo 1) {
            if (z > 4 && x[0] == x[z] && y[0] == y[z]) {
                inGame = false
            }
        }
        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) {
            inGame = false
        }
        if (!inGame) {
            timer.stop()
        }
    }

    private fun move() {
        for (z in bodyLength downTo 1) {
            x[z] = x[z - 1]
            y[z] = y[z - 1]
        }

        if (rightDirection)
            x[0] -= DOT_SIZE
        if (leftDirection)
            x[0] += DOT_SIZE
        if (upDirection)
            y[0] -= DOT_SIZE
        if (downDirection)
            y[0] += DOT_SIZE

    }

    private inner class MoveAdapter : KeyAdapter() {
        override fun keyPressed(e: KeyEvent?) {
            val key = e!!.keyCode
            when {
                key == KeyEvent.VK_D && !rightDirection -> {
                    leftDirection = true
                    upDirection = false
                    downDirection = false
                }
                key == KeyEvent.VK_A && !leftDirection -> {
                    rightDirection = true
                    upDirection = false
                    downDirection = false
                }
                key == KeyEvent.VK_W && !downDirection -> {
                    upDirection = true
                    rightDirection = false
                    leftDirection = false
                }
                key == KeyEvent.VK_S && !upDirection -> {
                    downDirection = true
                    rightDirection = false
                    leftDirection = false
                }
            }
        }
    }

}