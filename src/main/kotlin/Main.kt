import kotlin.math.abs

const val VALID_INPUTS = "X_O"
const val EMPTY_PLACE = " "

fun main() {

    val game = TicTacToe()

    game.displayState()

    while (true) {
        val move = readln()
        val xCoordinate = move.split(" ").first().uppercase()
        val yCoordinate = move.split(" ").last().uppercase()

        if (xCoordinate.toIntOrNull() == null  && yCoordinate.toIntOrNull() == null ) {
            println("You should enter numbers")
        }
        else if(xCoordinate.toInt() !in 1..3 || yCoordinate.toInt() !in 1..3){
            println("Coordinates should be from 1 to 3!")
        }
        else if (!game.isEmpty(xCoordinate.toInt(),yCoordinate.toInt())){
            println("This cell is occupied! Choose another one!")
        }
        else {
            game.updateTheGrid(xCoordinate.toInt(),yCoordinate.toInt(),game.player)
            game.displayState()
            if (game.analyzeGame()) break

        }

    }
}


enum class Players {PLAYER_X,PLAYER_O}

class TicTacToe {

    private val grid = MutableList(3) { MutableList(3) { EMPTY_PLACE } }

    var player = Players.PLAYER_X

    fun displayState() {

        println("---------")
        for (row in grid) {
            print("| ")
            for (col in row) {
                print("$col ")
            }
            println("|")
        }
        println("---------")
    }

    fun isEmpty(xCoordinate: Int,yCoordinate: Int) : Boolean{
        return grid[xCoordinate-1][yCoordinate-1] == EMPTY_PLACE
    }

    fun updateTheGrid(xCoordinate: Int,yCoordinate: Int,player: Players = Players.PLAYER_X) {
        grid[xCoordinate-1][yCoordinate-1] = if (player == Players.PLAYER_X) "X" else "O"
        this.player = if (player == Players.PLAYER_X) Players.PLAYER_O else Players.PLAYER_X
    }


    /**
     * Game not finished when neither side has three in a row but the grid still has empty cells.
     * Draw when no side has a three in a row and the grid has no empty cells.
     * X wins when the grid has three X’s in a row (including diagonals).
     * O wins when the grid has three O’s in a row (including diagonals).
     * Impossible when the grid has three X’s in a row as well as three O’s in a row,
     * or there are a lot more X's than O's or vice versa (the difference should
     * be 1 or 0; if the difference is 2 or more, then the game state is impossible).
     ******************************************************************************************************************/
    fun analyzeGame() : Boolean {

        if (!isImpossible()) {
            when {
                checkWinner() == null && hasEmptyCell() -> return false
                checkWinner() == null && !hasEmptyCell() -> println("Draw")
                checkWinner() != null -> println("${checkWinner()} wins")
            }
        } else {
            println("Impossible")
        }

        return true
    }

    private fun isImpossible(): Boolean {

        // check all number of x and number of o
        // check the difference should not be > then 1
        var numberOfOs = 0
        var numberOfXs = 0
        grid.forEach { row ->
            row.forEach {
                if (it == "O") numberOfOs++ else if (it == "X") numberOfXs++
            }
        }
        if (abs(numberOfXs - numberOfOs) > 1) return true

        // now check if all the row items are x or o
        var allX = false
        var allO = false
        for (row in grid) {
            if (row.all { it == "X" }) allX = true
            if (row.all { it == "O" }) allO = true
        }
        if (allX && allO) return true


        // second check in columns are all x or o
        var allColX: Int
        var allColO: Int
        var isAllColX = false
        var isAllColO = false

        for (row in grid.indices) {
            allColX = 0
            allColO = 0

            for (col in grid[row].indices) {
                if (grid[col][row] == "X") allColX++
                if (grid[col][row] == "O") allColO++
            }
            if (allColX == 3) isAllColX = true
            if (allColO == 3) isAllColO = true
        }
        return isAllColX && isAllColO

    }


    private fun checkWinner(): String? {

        // Check if the row element are x or o
        for (row in grid) {
            if (row.all { it == "X" }) return "X"
            if (row.all { it == "O" }) return "O"
        }

        // second check in columns are all x or o
        var allColX: Int
        var allColO: Int
        var isAllColX = false
        var isAllColO = false

        for (row in grid.indices) {
            allColX = 0
            allColO = 0

            for (col in grid[row].indices) {
                if (grid[col][row] == "X") allColX++
                if (grid[col][row] == "O") allColO++
            }
            if (allColX == 3) isAllColX = true
            if (allColO == 3) isAllColO = true
        }

        if (isAllColX) return "X"
        if (isAllColO) return "O"

        // Now check the cross form
        // the cross form only has two possible form with two variable
        if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0] != EMPTY_PLACE) {
            return grid[0][0]
        } else if (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[0][2] != EMPTY_PLACE) {
            return grid[0][2]
        }

        return null
    }

    private fun hasEmptyCell(): Boolean {
        grid.forEach { row ->
            row.forEach {
                if (it == EMPTY_PLACE) return true
            }
        }
        return false
    }

    fun getInput(): Boolean {

        val input = readln()

        if (input.length != 9) return false

        // first convert the input to mutable list
        var index = 0
        for (row in 0..2) {
            for (col in 0..2) {
                if (input[index] in VALID_INPUTS) {
                    grid[row][col] = input[index].toString()
                    index++
                } else {
                    println("invalid operation")
                }
            }
        }

        return true
    }
}