<template>
  <label
    v-if="label"
    :for="id"
    class="block text-titleColor text-sm font-semibold mb-2"
  >{{ $t(label) }}</label>
  <ColorPicker
    :id="id"
    ref="colorPicker"
    :theme="pickerTheme"
    :color="modelValue"
    :sucker-hide="true"
    @change-color="changeColor"
  />
</template>

<script setup lang="ts">
import { Themes } from "@/common/components/common/theme/Themes"
import { useApplicationTheme } from "@/common/components/common/theme/UseApplicationTheme"
import { v4 as uuid } from "uuid"
import { ref, watch } from "vue"
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

const storedTheme = useApplicationTheme()

function getPickerTheme() {
  switch (storedTheme.theme) {
    case Themes.DARK:
      return "dark"
    default:
      return "light"
  }
}

const pickerTheme = getPickerTheme()

const colorPicker = ref()

watch(
  () => props.modelValue,
  (newColor) => {
    if(newColor) {
      colorPicker.value.selectColor(newColor)
    }
  },
)

function changeColor(newColor: { hex: string }) {
  return emits("update:modelValue", newColor.hex)
}
</script>