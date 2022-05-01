<template>
  <div class="border">
    <label>{{ $t("feature.outgoingEvents") }}</label>
    <span v-if="!model.events">{{ $t("feature.noOutgoingEvents") }}</span>
    <div v-else>
      <div
        v-for="event in model.events"
        :key="event.id"
      >
        <label :for="event.id">{{ model.field }} ( {{ event.id }} )</label>
        <input
          :id="event.id"
          v-model="event.text"
          type="text"
        >
        <input
          type="button"
          :value="$t('common.delete')"
          @click="deleteEvent(event)"
        >
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { OutgoingEventsFormItem } from "@/features/components/description/OutgoingEventsFormItem"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import { computed, PropType } from "vue"

const props = defineProps({
  modelValue: {
    type: Object as PropType<OutgoingEventsFormItem>,
    required: true,
  },
})

const emit = defineEmits(["update:modelValue"])

const model = computed<OutgoingEventsFormItem>({
  get() {
    return props.modelValue
  },

  set(value) {
    return emit("update:modelValue", value)
  },
})

const deleteEvent = (event: OutgoingEvent) => {
  const { id, field, events } = model.value
  model.value = new OutgoingEventsFormItem(
    id,
    field,
    events.filter((e) => e.id != event.id),
  )
}
</script>

<style scoped></style>