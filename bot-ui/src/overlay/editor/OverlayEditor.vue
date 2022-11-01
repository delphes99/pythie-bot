<template>
  <div class="flex flex-col h-full">
    <div class="w-full shrink-0 flex flex-row">
      <h1 class="flex justify-center text-3xl font-bold p-2">
        {{ overlay.title }}
      </h1>
      <div>
        <ui-button
          :type="UiButtonType.Secondary"
          :router-link="`/overlay`"
          label="common.back"
        />
      </div>
    </div>
    <div
      class="shrink overflow-auto flex flex-row bg-gray-200"
    >
      <div class="w-60 shrink-0 overflow-auto flex flex-col">
        <editor-add-component @add-text="addText" />
        <editor-component-list
          v-model="selection"
          :components="components"
        />
      </div>
      <div class="shrink overflow-auto">
        <editor-preview
          v-model="selection"
          :components="components"
          :overlay="overlay"
        />
      </div>
      <div class="w-60 shrink-0 overflow-auto">
        <editor-props
          v-model="selection"
          class="grow"
        />
      </div>
    </div>
  </div>
</template>

<script async setup lang="ts">
import UiButton from "@/ds/button/UiButton.vue"
import UiButtonType from "@/ds/button/UiButtonType"
import EditorAddComponent from "@/overlay/editor/EditorAddComponent.vue"
import EditorComponentList from "@/overlay/editor/EditorComponentList.vue"
import EditorPreview from "@/overlay/editor/EditorPreview.vue"
import EditorProps from "@/overlay/editor/EditorProps.vue"
import TextComponent from "@/overlay/editor/textComponent/textComponent"
import Overlay from "@/overlay/Overlay"
import { OverlayElement } from "@/overlay/OverlayElement"
import OverlayRepository from "@/overlay/OverlayRepository"
import { inject, ref, watch } from "vue"

const props = defineProps({
  overlayId: {
    type: String,
    required: true,
  },
})

const backendUrl = inject("backendUrl") as string
const repository = new OverlayRepository(backendUrl)

const overlay = await repository.get(props.overlayId)
const selection = ref<OverlayElement | null>(null)
const components = ref<OverlayElement[]>(overlay.elements)

function addText() {
  const newComponent = new TextComponent(100, 100, "my text", "#000000", "Roboto", "20")

  components.value = [...components.value, newComponent]
  selection.value = newComponent
}

watch(
  () => selection.value,
  (newValue) => {
    if (newValue) {
      const component = components.value.find(
        (element) => element.id === newValue.id,
      )
      if (component && !component.equals(newValue)) {
        components.value = [
          ...components.value.filter((e) => e.id !== component.id),
          newValue,
        ]
      } else if(!component) {
        components.value = [...components.value, newValue]
      }
    }
  },
  {
    deep: true,
  },
)

watch(
  () => components.value,
  (newV, oldV) => {
    if (
      newV &&
      oldV &&
      JSON.stringify(newV) !== JSON.stringify(oldV)
    ) {
      const newOverlay = new Overlay(
        overlay.id,
        overlay.title,
        overlay.resolution,
        components.value,
      )

      if (!repository.save(newOverlay)) {
        //TODO better error management
        alert("Save error")
      }
    }
  },
  {
    deep: true,
  },
)
</script>
