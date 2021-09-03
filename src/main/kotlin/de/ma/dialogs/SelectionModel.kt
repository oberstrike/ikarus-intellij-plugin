package de.ma.dialogs

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty

class SelectionModel {

    private val _frameworkProperty: SimpleObjectProperty<Framework> = SimpleObjectProperty(
        Framework.Junit5
    )

    var framework: Framework
        get() = _frameworkProperty.value
        set(newValue) = _frameworkProperty.set(newValue)

    fun frameworkProperty(): Property<Framework> = _frameworkProperty

}