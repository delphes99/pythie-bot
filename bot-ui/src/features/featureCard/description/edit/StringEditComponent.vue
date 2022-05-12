<template>
  <ui-textfield
    v-model="model.value"
    :label="model.field"
  />
</template>
<script setup lang="ts">
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import { StringFormItem } from "@/features/featureCard/description/StringFormItem"
import { computed, PropType } from "vue"

const props = defineProps({
  modelValue: {
    type: Object as PropType<StringFormItem>,
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