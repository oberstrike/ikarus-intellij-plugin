package de.ma.domain.verify.codeblock.types

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiExpressionList
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl
import de.ma.domain.verify.VerifyExpression
import de.ma.domain.verify.codeblock.IExtractor
import de.ma.util.findChildByClass

class VerifyExpressionExtractor(
    private val psiCodeBlock: PsiCodeBlock
) : IExtractor<VerifyExpression> {

    companion object {
        private const val verifyStatement = "verify"
    }

    /*
    finds all verify expressions in the codeBlock
    Algorithm:
    1. get all lines which contains a verifyExpression
    2. extract

     */
    override fun extract(): List<VerifyExpression> {
        return psiCodeBlock.findChildByClass<PsiMethodCallExpressionImpl>() {
            it.text.contains(verifyStatement) && it.parent::class.java != PsiReferenceExpressionImpl::class.java
        }.map { methodExpression ->
            val referenceName = (methodExpression.firstChild as PsiReferenceExpression?)?.referenceName
            val qualifier = ((methodExpression.findChildByClass<PsiReferenceExpression>())).getOrNull(2)?.text
            val arguments = (methodExpression.lastChild as PsiExpressionList).expressions.map { it.text }

            VerifyExpression(
                qualifier = qualifier ?: "",
                referenceName = referenceName ?: "",
                arguments = arguments
            )
        }

    }


}