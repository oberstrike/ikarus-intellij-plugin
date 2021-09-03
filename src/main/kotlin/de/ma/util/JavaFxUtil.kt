package de.ma.util

import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.text.Font


fun <T> aChoiceBox(block: ChoiceBox<T>.() -> Unit): ChoiceBox<T> {
    return ChoiceBox<T>().apply {
        block.invoke(this)
    }
}



fun aLabel(text: String, block: Label.() -> Unit): Label {
    return Label(text).apply {
        block.invoke(this)
    }
}

fun Label.font(font: Number): Label {
    return apply {
        this.font = Font(font.toDouble())
    }
}

fun aGroup(block: Group.() -> Unit): Group {
    return Group().apply {
        block.invoke(this)
    }
}

fun Group.addChild(node: Node) {
    children.add(node)
}