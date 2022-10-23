<template>
  <label
    v-if="label"
    :for="id"
  >{{ $t(label) }}</label>
  <ColorPicker
    :id="id"
    theme="light"
    :color="selectedColor"
    :sucker-hide="true"
    :sucker-canvas="suckerCanvas"
    @change-color="changeColor"
  />
</template>

<script setup lang="ts">
import { v4 as uuid } from "uuid"
import { computed, ref } from "vue"
import { ColorPicker } from "vue-color-kit"
import "vue-color-kit/dist/vue-color-kit.css"

const emits = defineEmits(["update:modelValue"])
const props = defineProps({
  modelValue: {
    type: String,
    default: "#000000",
  },
  id: {
    type: String,
    default: () => uuid(),
  },
  label: {
    type: String,
    default: null,
  },
})


const selectedColor = computed({
  get() {
    return props.modelValue
  },

  set(value) {
    return emits("update:modelValue", value)
  },
})
const suckerCanvas = ref(null)

function changeColor(newColor: { hex: string }) {
  selectedColor.value = newColor.hex
}
</script>