<template>
  <div>
    <ui-textfield
      v-model="text"
      label="overlay.editor.text-component.text"
    />
  </div>
  <div>
    <ui-textfield
      v-model="font"
      label="overlay.editor.text-component.font"
    />
  </div>
  <div>
    <ui-textfield
      v-model="fontSize"
      label="overlay.editor.text-component.font-size"
    />
  </div>
  <div>
    <ui-color-picker
      v-model="selectedColor"
      label="overlay.editor.text-component.color"
    />
  </div>
</template>

<script setup lang="ts">
import UiColorPicker from "@/ds/form/colorpicker/UiColorPicker.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import TextComponent from "@/overlay/editor/textComponent/textComponent"
import { computed, PropType } from "vue"

const emits = defineEmits(["update:modelValue"])

const props = defineProps({
  modelValue: {
    type: Object as PropType<TextComponent>,
    default: null,
  },
})

const text = computed({
  get: () => props.modelValue.text,
  set: (value) => {
    const newModel = props.modelValue
    newModel.text = value
    emits("update:modelValue", newModel)
  },
})

const font = computed({
  get: () => props.modelValue.font,
  set: (value) => {
    const newModel = props.modelValue
    newModel.font = value
    emits("update:modelValue", newModel)
  },
})

const fontSize = computed({
  get: () => props.modelValue.fontSize,
  set: (value) => {
    const newModel = props.modelValue
    newModel.fontSize = value
    emits("update:modelValue", newModel)
  },
})

const selectedColor = computed({
  get: () => props.modelValue.color,
  set: (value) => {
    const newModel = props.modelValue
    newModel.color = value
    emits("update:modelValue", newModel)
  },
})
</script>