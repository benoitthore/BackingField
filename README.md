## Install

`implementation 'com.benoitthore.backingfield:core:0.9.1'`


## Backing fields for Kotlin extension variables 

The BackingField delegate allows backing fields on classes without having to extend them by storing keys and values in a map with WeakReferences so it doesn't create memory leaks.

When creating a BackingField, a Thread is started and waits until the Garbage Collector kicks in. When this happens the map is cleared so it only keeps WeakReferences that are still holding on to a value.

It can be used like so:
```Kotlin
  var SomeClass.extraValue : String by BackingField { "DefaultValue" }
  
  ...
  
  val someInstance = SomeClass()
  val someOtherInstance = SomeClass()
  someInstance.extraValue = "value1"
  someOtherInstance.extraValue = "value2"  
```


Limitations :

- This does not work on primitive types 
- Not as performant as a extending the class and adding an extra property

## Warning

This extension is far from being perfect, it allocates an object every time get/set is called. The goal of this library is to provide an easy way of adding backing fields for **testing, debugging and prototyping purposes only**
