package de.ma.domain.verify

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import de.ma.domain.verify.extractor.*
import de.ma.domain.verify.extractor.types.ExtractorType
import de.ma.util.*
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

        val codeBlock =
            PsiTreeUtil.getParentOfType(elementAtOffset, PsiCodeBlock::class.java) ?: throw  ReadingException(
                noExpressionSelectedMessage
            )

        val expressionStatementToCopy =
            codeBlock.children.filterIsInstance<PsiExpressionStatement>().firstOrNull() ?: throw ReadingException(
                noExpressionSelectedMessage
            )

        val verifyPsiElementCreator = VerifyPsiElementCreator(factory, expressionStatementToCopy)

        val psiFileWriter = PsiFileWriter(psiFile)

        val allGivenExpressions = ExtractorStrategy.extract(ExtractorType.GIVEN, codeBlock)

        val allExistingVerifyExpressions = ExtractorStrategy.extract(ExtractorType.VERIFY, codeBlock)

        for (givenExpression in allGivenExpressions) {
            if (!allExistingVerifyExpressions.contains(givenExpression)) {
                val verifyPsiElement = verifyPsiElementCreator.create(givenExpression)
                psiFileWriter.addElementToBase(codeBlock, verifyPsiElement)
            }
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


    override fun update(anActionEvent: AnActionEvent) {
        with(anActionEvent){
            presentation.isEnabled = project != null
                    && getData(CommonDataKeys.EDITOR) != null

        }
    }

}