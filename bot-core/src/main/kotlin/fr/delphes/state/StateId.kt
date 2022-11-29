package fr.delphes.state

import fr.delphes.utils.uuid.uuid

@JvmInline
value class StateId(val value: String = uuid())
