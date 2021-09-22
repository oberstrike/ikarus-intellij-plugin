package de.ma.domain.verify.codeblock

interface IExtractor <T>{

    fun extract(): List<T>
}

