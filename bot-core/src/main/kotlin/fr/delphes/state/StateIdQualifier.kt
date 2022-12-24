package fr.delphes.state

import fr.delphes.utils.uuid.uuid

@JvmInline
value class StateIdQualifier(val value: String = uuid())
