package de.ma.domain.verify

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiMethodCallExpression

data class GivenExpression(
    val qualifier: String,
    val referenceName: String,
    val arguments: List<String>
) {
    companion object {
        fun fromExpression(expression: PsiMethodCallExpression): GivenExpression {
            return with(expression) {
                GivenExpression(
                    qualifier = methodExpression.qualifierExpression?.text ?: "",
                    referenceName = methodExpression.referenceName ?: "",
                    arguments = argumentList.expressions.map { it.text }
                )
            }

        }
    }
}