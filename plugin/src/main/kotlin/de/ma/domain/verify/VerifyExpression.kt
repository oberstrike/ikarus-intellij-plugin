package de.ma.domain.verify

data class VerifyExpression(
    val qualifier: String,
    val referenceName: String,
    val arguments: List<String>
)