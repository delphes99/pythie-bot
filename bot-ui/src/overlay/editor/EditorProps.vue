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
      :is="modelVModel.properties.propertiesComponent()"
      v-model="modelVModel"
    />
  </ui-accorion-panel>
</template>
<script setup lang="ts">
import UiAccorionPanel from "@/ds/accordionPanel/UiAccorionPanel.vue"
import UiTextfield from "@/ds/form/textfield/UiTextfield.vue"
import OverlayElement from "@/overlay/OverlayElement"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"
import { useVModel } from "@vueuse/core"
import { computed, PropType } from "vue"

const emits = defineEmits(["update:modelValue"])
const props = defineProps({
  modelValue: {
    type: Object as PropType<OverlayElement<OverlayElementProperties>>,
    default: null,
  },
})

const modelVModel = useVModel(props, "modelValue", emits)

const left = computed({
  get: () => props.modelValue.general.left,
  set: (value) => {
    emits("update:modelValue", props.modelValue.modifyGeneral(old => old.modifyLeft(value)))
  },
})

const top = computed({
  get: () => props.modelValue.general.top,
  set: (value) => {
    emits("update:modelValue", props.modelValue.modifyGeneral(old => old.modifyTop(value)))
  },
})
</script>