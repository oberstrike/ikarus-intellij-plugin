package de.ma.util

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.JavaCodeStyleManager

class PsiFileWriter(
    private val psiFile: PsiFile
) {

    private val project by lazy { psiFile.project }

    fun addElementToBase(base: PsiElement, element: PsiElement) {
        WriteCommandAction.runWriteCommandAction(project) {
            base.add(element)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiFile)
        }
    }

}