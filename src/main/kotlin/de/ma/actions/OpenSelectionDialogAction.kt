package de.ma.actions

import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.lang.java.parser.JavaParser
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
import de.ma.dialogs.SelectionDialog
import de.ma.dialogs.SelectionModel

class OpenSelectionDialogAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        getElementAt(event)
    }

    private fun Editor.isValid() {

    }

    private fun getElementAt(event: AnActionEvent) {
        val dataContext = event.dataContext

        val project = event.project ?: return
        val editor = dataContext.getData(CommonDataKeys.EDITOR)
        val psiFile = dataContext.getData(CommonDataKeys.PSI_FILE) ?: return

        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val codeStylist = CodeStyleManager.getInstance(project);

        val offset = editor?.caretModel?.offset ?: return
        val element = psiFile.findElementAt(offset) ?: return

        val expression = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression::class.java) ?: return
        val arguments = expression.argumentList.expressions
        val argumentsNames = arguments.map { argument -> argument.text }

        val qualifier = expression.methodExpression.qualifierExpression?.text ?: return

        val referenceName = expression.methodExpression.referenceName

        println("QualifierName: $qualifier")
        println("ReferenceName: $referenceName")
        println("Arguments: ${argumentsNames.joinToString(prefix = "[", postfix = "]")}")


        val newExpression = factory.createVariableDeclarationStatement(
            "name",
            PsiType.BOOLEAN.deepComponentType,
            null
        )

        val parentMethod = PsiTreeUtil.getParentOfType(expression, PsiMethod::class.java) ?: return
        val lastElement = parentMethod.lastChild

        WriteCommandAction.runWriteCommandAction(project){
            lastElement.add(newExpression)
        }

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
            val selectionModel = SelectionModel()

            SelectionDialog(currentProject, selectionModel).show()
        }
    }


}
