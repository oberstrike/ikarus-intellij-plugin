package de.ma.domain.verify.extractor

import com.intellij.psi.PsiCodeBlock
import de.ma.domain.verify.VerifyExpression
import de.ma.domain.verify.extractor.types.ExtractorType

interface IExtractor {
    val type: ExtractorType

    fun extract(psiCodeBlock: PsiCodeBlock): List<VerifyExpression>
}

