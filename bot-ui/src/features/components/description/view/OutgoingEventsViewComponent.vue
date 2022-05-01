<template>
  <div class="border">
    <div
      v-for="event in props.item.events"
      :key="event.id"
    >
      {{ event.type }}
    </div>
  </div>
</template>
<script setup lang="ts">
import { OutgoingEventsFormItem } from "@/features/components/description/OutgoingEventsFormItem"
import { PropType } from "vue"

const props = defineProps({
  item: {
    type: Object as PropType<OutgoingEventsFormItem>,
    required: true,
  },
})
</script>

<style scoped></style>