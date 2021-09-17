package de.ma.domain.verify

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl
import com.intellij.psi.util.PsiTreeUtil
import de.ma.util.PsiFileWriter
import de.ma.util.findChildByClass
import kotlin.jvm.Throws

class VerifyGenerateAction : AnAction() {

    private val noProjectWasFoundMessage = "There is no java project found"

    private val noExpressionSelectedMessage = "There is no expression selected"

    private val noJavaFileSelectedMessage = "There is no Java-File open"

    companion object {
        @JvmStatic
        private val logger: Logger = Logger.getInstance(VerifyGenerateAction::class.java)
    }

    @Throws(ReadingException::class)
    private fun perform(event: AnActionEvent) {
        val dataContext = event.dataContext

        val project = event.project ?: throw ReadingException(noProjectWasFoundMessage)
        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val psiFile = dataContext.getData(CommonDataKeys.PSI_FILE) ?: throw ReadingException(noJavaFileSelectedMessage)
        val caretModel = dataContext.getData(CommonDataKeys.EDITOR)?.caretModel
        val offset = caretModel?.offset ?: throw ReadingException(noExpressionSelectedMessage)

        val elementAtOffset = psiFile.findElementAt(offset)

        val codeBlock = PsiTreeUtil.getParentOfType(elementAtOffset, PsiCodeBlock::class.java) ?: throw  ReadingException(
            noExpressionSelectedMessage
        )

        val expressionStatementToCopy =
            codeBlock.children.filterIsInstance<PsiExpressionStatement>().firstOrNull() ?: throw ReadingException(
                noExpressionSelectedMessage
            )

        val verifyPsiElementCreator = VerifyPsiElementCreator(factory, expressionStatementToCopy)

        val psiFileWriter = PsiFileWriter(psiFile)

        val allGivenExpressions = getAllGivenExpressions(codeBlock)

        val allExistingVerifyExpressions = getAllVerifyExpressions(codeBlock)


        for (givenExpression in allGivenExpressions) {
            if (!allExistingVerifyExpressions.contains(givenExpression)) {
                val verifyPsiElement = verifyPsiElementCreator.create(givenExpression)
                psiFileWriter.addElementToBase(codeBlock, verifyPsiElement)
            }
        }
    }

    private fun getAllVerifyExpressions(codeBlock: PsiCodeBlock): List<VerifyExpression> {

        return codeBlock.findChildByClass(PsiMethodCallExpressionImpl::class.java) {
            it.text.contains("verify") && it.parent::class.java != PsiReferenceExpressionImpl::class.java
        }.map { methodExpression ->
            val referenceName = (methodExpression.firstChild as PsiReferenceExpression).referenceName
            val qualifier = ((methodExpression.findChildByClass(PsiReferenceExpressionImpl::class.java)))[2].text
            val arguments = (methodExpression.lastChild as PsiExpressionList).expressions.map { it.text }

            VerifyExpression(
                qualifier = qualifier,
                referenceName = referenceName!!,
                arguments = arguments
            )
        }

    }

    private fun getAllGivenExpressions(codeBlock: PsiCodeBlock): List<VerifyExpression> =
        codeBlock.children.filterIsInstance<PsiExpressionStatement>()
            .map { it.firstChild } //given(x.sort()).willReturn(returnValue)
            .filterIsInstance<PsiMethodCallExpression>()
            .filter { it.children.isNotEmpty() && it.text.contains("given") && it.text.contains("willReturn") }
            .map { it.firstChild } //given(x.sort()).willReturn(returnValue)
            .filter { it.children.isNotEmpty() }
            .map { it.firstChild } //given(x.sort()).willReturn(returnValue)
            .filterIsInstance<PsiMethodCallExpression>()
            .map { it.argumentList.expressions } //given(x.sort())
            .filter { it.isNotEmpty() }
            .map { it.firstOrNull() } //x.sort()
            .filterIsInstance<PsiMethodCallExpression>()
            .map { callExpression ->
                with(callExpression) {
                    VerifyExpression(
                        qualifier = methodExpression.qualifierExpression?.text ?: "",
                        referenceName = methodExpression.referenceName ?: "",
                        arguments = argumentList.expressions.map { it.text }
                    )
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


    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)

        e.presentation.isEnabled = project != null
                && editor != null
    }

}