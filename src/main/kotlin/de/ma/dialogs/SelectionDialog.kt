package de.ma.dialogs

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import de.ma.util.*
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.layout.GridPane
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel


class SelectionDialog(
    project: Project,
    private val model: SelectionModel
) : DialogWrapper(project) {

    private val width = 300.0

    private val height = 200.0

    private val controller: SelectionController

    init {
        title = "Test DialogWrapper"
        controller = SelectionController()
        init()
    }

    override fun createCenterPanel(): JComponent {
        val dialogPanel = JPanel(BorderLayout())

        val fxPanel = JFXPanel()
        fxPanel.preferredSize = Dimension(width.toInt(), height.toInt())

        Platform.setImplicitExit(false)

        Platform.runLater {
            initFX(fxPanel)
        }

        dialogPanel.add(fxPanel, BorderLayout.CENTER)
        return dialogPanel
    }


    private fun initFX(fxPanel: JFXPanel): Scene {
        val root = aGroup {
            addChild(
                aGridPane {
                    this + aGridNode(aLabel("Auswahl des Frameworks") { font(11.0) }, 1, 0)
                    this + aGridNode(aLabel("Framework") { font(9.0) }, 1, 1)
                    this + aGridNode(aChoiceBox<Framework> {
                        items.addAll(controller.frameworkValues)
                        layoutX = 10.0
                        layoutY = 10.0
                        valueProperty().bindBidirectional(model.frameworkProperty())
                    }, 2, 1)
                }

            )
        }

        val scene = Scene(root, width, height)
        fxPanel.scene = scene

        return scene
    }


    override fun doOKAction() {
        val alertContent = model.framework.name

        Platform.runLater {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Framework"
            alert.contentText = alertContent
            alert.showAndWait()
        }



        super.doOKAction()
    }

}


