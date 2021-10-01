package de.ma.domain.verify.codeblock

import com.intellij.psi.PsiCodeBlock
import de.ma.domain.verify.codeblock.types.GivenExpressionExtractor
import de.ma.domain.verify.codeblock.types.VerifyExpressionExtractor

class CodeBlockExtractor(
    codeBlock: PsiCodeBlock
) {
    private val givenExpressionExtractor = GivenExpressionExtractor(codeBlock)

    private val verifyExpressionExtractor = VerifyExpressionExtractor(codeBlock)

    fun extractGivenExpressions() = givenExpressionExtractor.extract()

    fun extractVerifyExpressions() = verifyExpressionExtractor.extract()


}
