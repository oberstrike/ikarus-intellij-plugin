package de.ma.domain.verify

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import de.ma.domain.verify.codeblock.*
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

    @Throws(VerifyGenerationException::class)
    private fun perform(event: AnActionEvent) {
        val dataContext = event.dataContext

        val selectedProject = event.project ?: throw VerifyGenerationException(noProjectWasFoundMessage)
        val elementFactory = JavaPsiFacade.getInstance(selectedProject).elementFactory
        val selectedPsiFile = dataContext.getData(CommonDataKeys.PSI_FILE) ?: throw VerifyGenerationException(noJavaFileSelectedMessage)
        val caretModel = dataContext.getData(CommonDataKeys.EDITOR)?.caretModel
        val offset = caretModel?.offset ?: throw VerifyGenerationException(noExpressionSelectedMessage)

        val elementAtOffset = selectedPsiFile.findElementAt(offset)

        val methodCodeBlock =
            PsiTreeUtil.getParentOfType(elementAtOffset, PsiCodeBlock::class.java) ?: throw  VerifyGenerationException(
                noExpressionSelectedMessage
            )


        val expressionStatementToCopy =
            methodCodeBlock.children.filterIsInstance<PsiExpressionStatement>().firstOrNull() ?: throw VerifyGenerationException(
                noExpressionSelectedMessage
            )

        val verifyPsiElementCreator = VerifyPsiElementCreator(elementFactory, expressionStatementToCopy)

        val psiFileWriter = PsiFileWriter(selectedPsiFile)

        val extractor = CodeBlockExtractor(methodCodeBlock)

        val allGivenExpressions = extractor.extractGivenExpressions()

        val allExistingVerifyExpressions = extractor.extractVerifyExpressions()

        for (givenExpression in allGivenExpressions) {
            if (!allExistingVerifyExpressions.contains(givenExpression)) {
                val verifyPsiElement = verifyPsiElementCreator.create(givenExpression)
                psiFileWriter.addElementToBase(methodCodeBlock, verifyPsiElement)
            }
        }
    }


    override fun actionPerformed(event: AnActionEvent) {
        try {
            perform(event)
        } catch (exception: VerifyGenerationException) {
            logger.error(exception.message)
        }
    }


    override fun update(anActionEvent: AnActionEvent) {
        with(anActionEvent){
            presentation.isEnabled = project != null
                    && getData(CommonDataKeys.EDITOR) != null

        }
    }

}