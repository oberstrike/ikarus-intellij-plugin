package de.ma.util


fun String.containsAny(listOfElements: List<String>): Boolean {
    if (listOfElements.contains(this)) {
        return true
    }
    return false
}