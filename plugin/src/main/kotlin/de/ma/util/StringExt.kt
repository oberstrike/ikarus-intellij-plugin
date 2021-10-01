package de.ma.util


fun String.containsAny(listOfElements: List<String>): Boolean {
    for(element in listOfElements){
        if(this.contains(element)){
            return true
        }
    }
    return false
}