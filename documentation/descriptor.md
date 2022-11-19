# Descriptor
The purpose of Descriptor mechanism is to be able to generate a UI from a representation of backend objets.
## ObjectDescriptor
ObjectDescriptor is a model of an existing object with some metadatas for all its properties.
## PropertyDescriptor
There are 3 parts in the descriptor:
- structural description : all the metadatas necessary to generate the object structure (type, name...)
- metadatas: all the metadatas necessary to generate the UI (label, description, constraints...)
- value: the value of the property