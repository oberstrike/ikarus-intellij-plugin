package de.ma.domain.verify

import com.intellij.psi.*

class VerifyExpressionCreator(private val factory: PsiElementFactory) {


    fun create(givenExpression: GivenExpression): PsiExpression {


        return factory.createExpressionFromText(
            "verify(${givenExpression.qualifier}, times(1)).${givenExpression.referenceName}(${givenExpression.arguments.joinToString()})",
            null
        )
    }

}