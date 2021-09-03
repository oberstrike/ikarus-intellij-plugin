package de.ma.util

import javafx.scene.Node
import javafx.scene.layout.GridPane

fun aGridPane(block: GridPane.() -> Unit): GridPane {
    return GridPane().apply {
        block.invoke(this)
    }
}


operator fun GridPane.plus(gridNode: GridNode) {
    add(gridNode.node, gridNode.i, gridNode.i2)
}

data class GridNode(
    val node: Node,
    val i: Int,
    val i2: Int
) {

}

fun aGridNode(node: Node, i: Int, i2: Int): GridNode {
    return GridNode(node, i, i2)
}