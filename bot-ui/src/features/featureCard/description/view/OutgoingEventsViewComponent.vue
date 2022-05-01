<template>
  <div class="border">
    <div
      v-for="event in props.item.events"
      :key="event.id"
    >
      <component
        :is="selectViewComponentFor(event)"
        :event="event"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
import { OutgoingEventsFormItem } from "@/features/featureCard/description/OutgoingEventsFormItem"
import { selectViewComponentFor } from "@/features/outgoingevents/OutgoingEventSelection"
import { PropType } from "vue"

const props = defineProps({
  item: {
    type: Object as PropType<OutgoingEventsFormItem>,
    required: true,
  },
})
</script>

<style scoped></style>