package de.ma.domain.verify

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiExpressionStatement

class VerifyPsiElementCreator(
    private val factory: PsiElementFactory,
    private val expressionStatementToCopy: PsiExpressionStatement,
) {

    companion object {
        private const val verify = "org.mockito.Mockito.verify"
        private const val times = "org.mockito.Mockito.times(1)"
    }

    fun create(verifyExpression: VerifyExpression): PsiElement {

        val createExpressionFromText = factory.createExpressionFromText(
            "$verify(${verifyExpression.qualifier}, $times).${verifyExpression.referenceName}(${verifyExpression.arguments.joinToString()})",
            null
        )
        return expressionStatementToCopy.copy().apply {
            firstChild.replace(createExpressionFromText)
        }
    }
}