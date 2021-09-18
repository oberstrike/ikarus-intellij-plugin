package de.ma.domain.verify.extractor.types

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl
import de.ma.domain.verify.VerifyExpression
import de.ma.domain.verify.extractor.IExtractor
import de.ma.util.containsAny
import de.ma.util.findChildByClass
import de.ma.util.hasArgumentExpressions
import de.ma.util.textContains

class GivenExpressionExtractor : IExtractor {

    override val type: ExtractorType = ExtractorType.GIVEN

    companion object {

        private const val givenStatement = "given"

        private val givenReturnStatements = listOf("willReturn", "willThrow", "willAnswer")
    }


    /*
    Finds all given Expressions.
    Algorithm:
        1. find all MethodCalls wich contain a given statement but not the whole line just the given(x.toString()) expression
        2. get the first argument expression (x.toString())
        3. use this expression to create a VerifyExpression
     */
    override fun extract(psiCodeBlock: PsiCodeBlock): List<VerifyExpression> {
        return psiCodeBlock.findChildByClass(PsiMethodCallExpressionImpl::class.java) { methodCallExpression ->
            methodCallExpression.textContains(givenStatement)
                    && !methodCallExpression.text.containsAny(givenReturnStatements)
                    && methodCallExpression.hasArgumentExpressions()
        }.map { it.argumentList.expressions.first() as PsiMethodCallExpression }
            .map { methodCallExpression ->
                with(methodCallExpression) {
                    VerifyExpression(
                        qualifier = methodExpression.qualifierExpression?.text ?: "",
                        referenceName = methodExpression.referenceName ?: "",
                        arguments = argumentList.expressions.map { it.text }
                    )
                }
            }

    }

}