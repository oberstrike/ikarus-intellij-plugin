package de.ma.domain.verify.extractor.types

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiExpressionList
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl
import de.ma.domain.verify.VerifyExpression
import de.ma.domain.verify.extractor.IExtractor
import de.ma.util.findChildByClass

class VerifyExpressionExtractor : IExtractor {

    override val type: ExtractorType = ExtractorType.VERIFY

    companion object {
        private const val verifyStatement = "verify"
    }

    /*
    finds all verify expressions in the codeBlock
    Algorithm:
    1. get all lines which contains a verifyExpression
    2. extract

     */
    override fun extract(psiCodeBlock: PsiCodeBlock): List<VerifyExpression> {
        return psiCodeBlock.findChildByClass(PsiMethodCallExpressionImpl::class.java) {
            it.text.contains(verifyStatement) && it.parent::class.java != PsiReferenceExpressionImpl::class.java
        }.map { methodExpression ->
            val referenceName = (methodExpression.firstChild as PsiReferenceExpression?)?.referenceName
            val qualifier = ((methodExpression.findChildByClass(PsiReferenceExpressionImpl::class.java))).getOrNull(2)?.text
            val arguments = (methodExpression.lastChild as PsiExpressionList).expressions.map { it.text }

            VerifyExpression(
                qualifier = qualifier ?: "",
                referenceName = referenceName ?: "",
                arguments = arguments
            )
        }

    }


}