<template>
  <label
      v-if="label"
      :for="id"
      class="block text-titleColor text-sm font-semibold mb-2"
  >{{ $t(label) }}</label>
  <ColorPicker
      :id="id"
      ref="colorPicker"
      :color="modelValue"
      :sucker-hide="true"
      :theme="pickerTheme"
      @change-color="changeColor"
  />
</template>

<script lang="ts" setup>
import {darkMonochromeTheme, darkTheme} from "@/common/utils/theme/Themes";
import {useApplicationTheme} from "@/common/utils/theme/UseApplicationThemeStore"
import {v4 as uuid} from "uuid"
import {computed, ref, watch} from "vue"
import {ColorPicker} from "vue-color-kit"
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

const {currentTheme} = useApplicationTheme()

const pickerTheme = computed(() => {
  switch (currentTheme.theme) {
    case darkTheme:
    case darkMonochromeTheme:
      return "dark"
    default:
      return "light"
  }
})

const colorPicker = ref()

watch(
    () => props.modelValue,
    (newColor) => {
      if (newColor) {
        colorPicker.value.selectColor(newColor)
      }
    },
)

function changeColor(newColor: { hex: string }) {
  return emits("update:modelValue", newColor.hex)
}
</script>