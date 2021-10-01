package de.ma.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import de.ma.dialogs.SelectionDialog

class OpenSelectionDialogAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        getClazz(event)
    }

    private fun Editor.isValid() {

    }


    private fun getClazz(event: AnActionEvent) {
        val currentProject = event.project
        val dataContext = event.dataContext
        val editor = dataContext.getData(CommonDataKeys.EDITOR)
        val file = dataContext.getData(CommonDataKeys.PSI_FILE) ?: return

        val data = CommonDataKeys.EDITOR.getData(dataContext)


        if (currentProject != null && editor != null) {
            val clazz = PsiTreeUtil.getParentOfType(file.findElementAt(editor.caretModel.offset), PsiClass::class.java)
            checkNotNull(clazz)

            SelectionDialog(currentProject).show()
        }
    }


}
