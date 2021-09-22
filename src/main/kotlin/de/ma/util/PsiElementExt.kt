package de.ma.util

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethodCallExpression

fun PsiElement.findChildBy(block: (PsiElement) -> Boolean = { _ -> true}): List<PsiElement> {
    val result = mutableListOf<PsiElement>()

    for (child in children) {
        val childResult = child.findChildBy(block)

        if (block.invoke(child)) {
            result.add(child)
        }

        result.addAll(childResult)
    }
    return result
}

inline fun <reified T : PsiElement> PsiElement.findChildByClass(
    block: (T) -> Boolean = { _ ->  true}
): List<T> {
    return findChildBy()
        .filterIsInstance<T>()
        .filter(block)
}

fun PsiMethodCallExpression.hasArgumentExpressions() = argumentList.expressions.isNotEmpty()

fun PsiElement.textContains(textToFind: String) = text.contains(textToFind)