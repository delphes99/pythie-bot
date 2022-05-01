<template>
  <div
    v-for="event in events"
    :key="event.id"
  >
    {{ event.outgoingEvent.type }}
  </div>
</template>

<script setup lang="ts">
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent.js"
import { ref } from "vue"
import { v4 as uuid } from "uuid"

interface EditEvent {
  id: string
  outgoingEvent: OutgoingEvent
}

const props = defineProps<{
  feature: OutgoingEvent[]
}>()

const events = ref<EditEvent[]>(
  props.feature.map((outgoingEvent) => {
    return {
      id: uuid(),
      outgoingEvent: outgoingEvent,
    }
  }),
)
</script>
