<template>
  <div
      id="preview"
      :style="`width: ${overlay.resolution.width}px; height: ${overlay.resolution.height}px; position: relative;`"
  >
    <DraggableElement
        v-for="(element, index) in components"
        :key="element.id"
        :component="components[index]"
    />
  </div>
</template>
<script lang="ts" setup>
import DraggableElement from "@/overlay/editor/DraggableElement.vue"
import {useOverlayEditorStore} from "@/overlay/editor/useOverlayEditorStore"
import Overlay from "@/overlay/Overlay"
import {storeToRefs} from "pinia"
import {PropType} from "vue"

defineProps({
  overlay: {
    type: Object as PropType<Overlay>,
    required: true,
  },
})

const store = useOverlayEditorStore()
const {components} = storeToRefs(store)

</script>

<style>
#preview {
  background-image: repeating-linear-gradient(
      to bottom,
      transparent,
      transparent 20px,
      #dddddd 1px,
      #dddddd 21px
  ),
  repeating-linear-gradient(
      to right,
      #eeeeee,
      #eeeeee 20px,
      #dddddd 1px,
      #dddddd 21px
  );
}
</style>
