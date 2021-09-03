package de.ma.domain.verify

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiMethodUtil
import com.intellij.psi.util.PsiTreeUtil
import kotlin.jvm.Throws

class GenerateVerifyExpressionAction : AnAction() {

    private val noProjectWasFoundMessage = "There is no java project found"

    private val noExpressionSelectedMessage = "There is no expression selected"

    private val noJavaFileSelectedMessage = "There is no Java-File open"

    companion object {
        @JvmStatic
        private val logger: Logger = Logger.getInstance(GenerateVerifyExpressionAction::class.java)
    }

    @Throws(ReadingException::class)
    private fun perform(event: AnActionEvent) {
        val dataContext = event.dataContext

        val project = event.project ?: throw ReadingException(noProjectWasFoundMessage)
        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val psiFile = dataContext.getData(CommonDataKeys.PSI_FILE) ?: throw ReadingException(noJavaFileSelectedMessage)
        val offset = dataContext.getData(CommonDataKeys.EDITOR)?.caretModel?.offset ?: throw ReadingException(noExpressionSelectedMessage)


        val element = psiFile.findElementAt(offset)


        val expression =
            PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression::class.java) ?: throw ReadingException(
                noExpressionSelectedMessage
            )
        val givenExpression = GivenExpression.fromExpression(expression)

        val verifyExpression = VerifyExpressionCreator(factory).create(givenExpression)


        val parentMethod = PsiTreeUtil.getParentOfType(expression, PsiMethod::class.java)

        val lastElement = parentMethod!!.lastChild

        WriteCommandAction.runWriteCommandAction(project) {
            lastElement.add(verifyExpression)
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        try {
            perform(event)
        } catch (exception: ReadingException) {
            logger.error(exception.message)
        }
    }

    class ReadingException(message: String) : RuntimeException(message)

}