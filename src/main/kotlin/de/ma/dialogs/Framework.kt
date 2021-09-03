package de.ma.dialogs


sealed class Framework(
    val name: String
) {

    object Junit5: Framework("Junit5")

    object Junit4: Framework("Junit4")


    override fun toString(): String {
        return name
    }


}
