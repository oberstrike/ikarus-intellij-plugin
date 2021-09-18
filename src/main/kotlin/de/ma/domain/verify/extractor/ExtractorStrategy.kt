package de.ma.domain.verify.extractor

import com.intellij.psi.PsiCodeBlock
import de.ma.domain.verify.VerifyExpression
import de.ma.domain.verify.extractor.types.ExtractorType
import de.ma.domain.verify.extractor.types.GivenExpressionExtractor
import de.ma.domain.verify.extractor.types.VerifyExpressionExtractor

object ExtractorStrategy {

    @JvmStatic
    private val extractors = listOf(
        GivenExpressionExtractor(),
        VerifyExpressionExtractor()
    )

    fun extract(extractorType: ExtractorType, psiCodeBlock: PsiCodeBlock): List<VerifyExpression> {
        return extractors.find { it.type == extractorType }?.extract(psiCodeBlock) ?: listOf()
    }

}
