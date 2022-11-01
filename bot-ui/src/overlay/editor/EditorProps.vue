<template>
  <ui-accorion-panel
    v-if="modelValue"
    title="overlay.editor.selected-item.title"
  >
    <div class="flex flex-col">
      <div>{{ $t("overlay.editor.selected-item.id") }} : {{ modelValue.id }}</div>
      <div>{{ $t("overlay.editor.selected-item.type") }} : {{ modelValue.type }}</div>
    </div>
  </ui-accorion-panel>
  <ui-accorion-panel
    v-if="modelValue"
    title="overlay.editor.properties"
  >
    <div>
      <ui-textfield
        v-model="left"
        label="overlay.editor.X"
      />
    </div>
    <div>
      <ui-textfield
        v-model="top"
        label="overlay.editor.Y"
      />
    </div>
    <component
      :is="componentSpecifProps()"
      v-model="model"
    />
  </ui-accorion-panel>
</template>
<script setup lang="ts">
import UiAccorionPanel from "@/ds/accordionPanel/UiAccorionPanel.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import TextComponentProperties from "@/overlay/editor/textComponent/textComponentProperties.vue"
import { OverlayElement } from "@/overlay/OverlayElement"
import { useVModel } from "@vueuse/core"
import { computed, PropType } from "vue"

const emits = defineEmits(["update:modelValue"])
const props = defineProps({
  modelValue: {
    type: Object as PropType<OverlayElement>,
    default: null,
  },
})

const model = useVModel(props, "modelValue", emits)

function componentSpecifProps(): any {
  return TextComponentProperties
}

const left = computed({
  get: () => props.modelValue.left,
  set: (value) => {
    const newModel = props.modelValue
    newModel.left = value
    emits("update:modelValue", newModel)
  },
})

const top = computed({
  get: () => props.modelValue.top,
  set: (value) => {
    const newModel = props.modelValue
    newModel.top = value
    emits("update:modelValue", newModel)
  },
})
</script>