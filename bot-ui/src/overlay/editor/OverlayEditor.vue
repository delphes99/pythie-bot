<template>
  <loading-promise :loading-promise="loadPromise">
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
        <div class="shrink-0 w-60 flex flex-col">
          <editor-add-component @add-text="addText" />
          <editor-component-list
            v-model:selection="selection"
            :components="components"
          />
        </div>
        <div class="shrink overflow-auto">
          <editor-preview
            v-model:selection="selection"
            :overlay="overlay"
          />
        </div>
        <div class="shrink-0 w-60">
          <editor-props
            v-model:selection="selection"
            class="grow"
          />
        </div>
      </div>
    </div>
  </loading-promise>
</template>

<script setup lang="ts">
import LoadingPromise from "@/common/components/common/LoadingPromise.vue"
import { useLoadingPromise } from "@/common/composition/UseLoadingPromise"
import UiButton from "@/ds/button/UiButton.vue"
import UiButtonType from "@/ds/button/UiButtonType"
import EditorAddComponent from "@/overlay/editor/EditorAddComponent.vue"
import EditorComponentList from "@/overlay/editor/EditorComponentList.vue"
import EditorPreview from "@/overlay/editor/EditorPreview.vue"
import EditorProps from "@/overlay/editor/EditorProps.vue"
import TextComponent from "@/overlay/editor/textComponent"
import { OverlayElement } from "@/overlay/OverlayElement"
import OverlayRepository from "@/overlay/OverlayRepository"
import { computed, inject, ref, watch } from "vue"

const props = defineProps({
  overlayId: {
    type: String,
    required: true,
  },
})

const backendUrl = inject("backendUrl") as string
const repository = new OverlayRepository(backendUrl)
const loadPromise = useLoadingPromise(repository.get(props.overlayId))

const overlay = loadPromise.data
const selection = ref<OverlayElement | null>(null)
const components = computed(() => {
  return overlay.value ? overlay.value.elements : []
})

function addText() {
  const newComponent = new TextComponent(100, 100, "my text")
  if (overlay.value) {
    overlay.value.elements = [...overlay.value.elements, newComponent]
  }
  selection.value = newComponent
}

watch(
  () => selection.value,
  (newValue) => {
    if (overlay.value && newValue) {
      const component = overlay.value.elements.find(
        (element) => element.id === newValue?.id,
      )
      if (
        component instanceof TextComponent &&
        newValue instanceof TextComponent &&
        !component.equals(newValue)
      ) {
        overlay.value.elements = [
          ...overlay.value?.elements.filter((e) => e.id !== component.id),
          newValue,
        ]
      }
    }
  },
)

watch(
  () => overlay.value?.elements,
  (newV, oldV) => {
    if (
      newV &&
      oldV &&
      overlay.value &&
      JSON.stringify(newV) !== JSON.stringify(oldV)
    ) {
      if (!repository.save(overlay.value)) {
        //TODO better error management
        alert("Save error")
      }
    }
  },
)
</script>
