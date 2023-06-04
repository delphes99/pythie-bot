<template>
  <div class="flex flex-col h-full">
    <div class="w-full shrink-0 flex flex-row">
      <h1 class="flex justify-center text-3xl font-bold p-2">
        {{ overlay.title }}
      </h1>
      <div>
        <ui-button
            :router-link="`/overlay`"
            :type="UiButtonType.Secondary"
            label="common.back"
        />
      </div>
    </div>
    <div
        class="shrink overflow-auto flex flex-row bg-gray-200"
    >
      <div class="w-60 shrink-0 overflow-auto flex flex-col">
        <ui-button
            label="overlay.editor.addText"
            @on-click="addText"
        />
        <ui-button
            label="overlay.editor.addImage"
            @on-click="addImage"
        />
        <editor-component-list/>
      </div>
      <div class="shrink overflow-auto">
        <editor-preview
            v-model:selection="selection"
            :components="components"
            :overlay="overlay"
        />
      </div>
      <div class="w-60 shrink-0 overflow-auto">
        <editor-props/>
      </div>
    </div>
  </div>
</template>

<script async lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButton from "@/common/designSystem/button/UiButton.vue"
import UiButtonType from "@/common/designSystem/button/UiButtonType"
import {autowired} from "@/common/utils/injection.util";
import ImageComponent from "@/overlay/editor/component/imageComponent/ImageComponent"
import TextComponent from "@/overlay/editor/component/textComponent/TextComponent"
import EditorComponentList from "@/overlay/editor/EditorComponentList.vue"
import EditorPreview from "@/overlay/editor/EditorPreview.vue"
import EditorProps from "@/overlay/editor/EditorProps.vue"
import {useOverlayEditorStore} from "@/overlay/editor/useOverlayEditorStore"
import Overlay from "@/overlay/Overlay"
import OverlayElement from "@/overlay/OverlayElement"
import {OverlayElementGeneralProperties} from "@/overlay/OverlayElementGeneralProperties"
import {OverlayElementProperties} from "@/overlay/OverlayElementProperties"
import OverlayRepository from "@/overlay/OverlayRepository"
import {ref, watch} from "vue"

const props = defineProps({
  overlayId: {
    type: String,
    required: true,
  },
})

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const repository = new OverlayRepository(backendUrl)

const overlay = await repository.get(props.overlayId)

const store = useOverlayEditorStore()
store.init(overlay.elements)

const selection = ref<OverlayElement<OverlayElementProperties> | null>(null)
const components = ref<OverlayElement<OverlayElementProperties>[]>(overlay.elements)

function addText() {
  const newComponent = new OverlayElement(
      new OverlayElementGeneralProperties(0, 0),
      new TextComponent("my text", "#000000", "Roboto", "20"),
  )

  store.addComponent(newComponent)
  store.selection(newComponent)
}

function addImage() {
  const newComponent = new OverlayElement(
      new OverlayElementGeneralProperties(0, 0),
      new ImageComponent(""),
  )

  store.addComponent(newComponent)
  store.selection(newComponent)
}

watch(
    () => components.value,
    (newSelection) => {
      saveOverlay(newSelection)
    },
    {deep: true},
)

function saveOverlay(newState: OverlayElement<OverlayElementProperties>[]) {
  const newOverlay = new Overlay(
      overlay.id,
      overlay.title,
      overlay.resolution,
      newState,
  )

  if (!repository.save(newOverlay)) {
    //TODO better error management
    alert("Save error")
  }
}

watch(
    () => store.$state.components,
    (newState, oldState) => {
      if (
          newState &&
          oldState &&
          JSON.stringify(newState) !== JSON.stringify(oldState)
      ) {
        saveOverlay(newState)
      }
    },
    {
      deep: true,
    },
)
</script>
