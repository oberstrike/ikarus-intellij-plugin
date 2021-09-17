package de.ma.util

import com.intellij.psi.PsiElement

fun PsiElement.findChildBy(block: (PsiElement) -> Boolean): List<PsiElement> {
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

fun defaultFun(param: PsiElement) = true

inline fun <reified T : PsiElement> PsiElement.findChildByClass(
    klass: Class<T>,
    block: (T) -> Boolean = ::defaultFun
): List<T> {
    return findChildBy { it::class.java == klass }
        .filterIsInstance<T>()
        .filter(block)
}
