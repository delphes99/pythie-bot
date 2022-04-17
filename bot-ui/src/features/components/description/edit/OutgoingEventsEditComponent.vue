<template>
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
  </div>
</template>
<script setup lang="ts">
import {OutgoingEventsFormItem} from "@/features/components/description/OutgoingEventsFormItem"
import {computed, PropType} from "vue"

const props = defineProps({
  modelValue: {
    type: Object as PropType<OutgoingEventsFormItem>,
    required: true,
  },
})

const emit = defineEmits(['update:modelValue'])

const model = computed({
  get () {
    return props.modelValue
  },

  set (value) {
    return emit('update:modelValue', value)
  },
})
</script>

<style scoped></style>