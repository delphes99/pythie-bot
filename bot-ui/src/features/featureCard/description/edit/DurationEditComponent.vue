<template>
  <ui-textfield
    v-model="model.value"
    :label="model.field + ' (s)'"
  />
</template>
<script setup lang="ts">
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import { DurationFormItem } from "@/features/featureCard/description/DurationFormItem"
import { computed, PropType } from "vue"

const props = defineProps({
  modelValue: {
    type: Object as PropType<DurationFormItem>,
    required: true,
  },
})
const emit = defineEmits(["update:modelValue"])

const model = computed({
  get() {
    return props.modelValue
  },

  set(value) {
    return emit("update:modelValue", value)
  },
})
</script>

<style scoped></style>