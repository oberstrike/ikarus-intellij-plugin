package de.ma.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class SelectionDialog(
    project: Project,
) : DialogWrapper(project) {

    init {
        title = "Demo"
        init()
        isResizable = false
    }


    override fun createCenterPanel(): JComponent {
        return ComposePanel().apply {
            setBounds(0, 0, 400, 300)
            setContent {
                MaterialTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Row {
                            Column {
                                Text("Hallo aus Compose!!")
                            }
                        }


                    }

                }
            }

        }
    }
}


