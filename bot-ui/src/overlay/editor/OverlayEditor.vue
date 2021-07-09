<template>
  <loading-promise :loading-promise="loadPromise">
    <div class="flex flex-row flex-grow h-full">
      <div class="w-60 flex flex-col">
        <editor-add-component @add-text="addText" />
        <editor-component-list
          :components="components"
          v-model:selection="selection"
        ></editor-component-list>
      </div>
      <div
        class="mx-60 h-full flex flex-shrink-0 flex-grow items-center justify-center"
      >
        <editor-preview
          :overlay="overlay"
          v-model:selection="selection"
        ></editor-preview>
      </div>
      <div class="w-60">
        <editor-props
          v-model:selection="selection"
          class="flex-grow"
        ></editor-props>
      </div>
    </div>
  </loading-promise>
</template>

<script lang="ts">
import LoadingPromise from "@/common/components/common/LoadingPromise.vue";
import { useLoadingPromise } from "@/common/composition/UseLoadingPromise.ts";
import EditorAddComponent from "@/overlay/editor/EditorAddComponent.vue";
import EditorComponentList from "@/overlay/editor/EditorComponentList.vue";
import EditorPreview from "@/overlay/editor/EditorPreview.vue";
import EditorProps from "@/overlay/editor/EditorProps.vue";
import TextComponent, { fromObject } from "@/overlay/editor/textComponent.ts";
import { OverlayElement } from "@/overlay/OverlayElement.js";
import OverlayRepository from "@/overlay/OverlayRepository.ts";
import { computed, defineComponent, inject, ref, watch } from "vue";

export default defineComponent({
  name: `OverlayEditor`,
  components: {
    EditorAddComponent,
    LoadingPromise,
    EditorComponentList,
    EditorPreview,
    EditorProps
  },
  props: {
    overlayId: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const backendUrl = inject("backendUrl") as string;
    const repository = new OverlayRepository(backendUrl);
    const loadPromise = useLoadingPromise(repository.get(props.overlayId));

    const overlay = loadPromise.data;
    const selection = ref<OverlayElement | null>(null);
    const components = computed(() => {
      return overlay.value ? overlay.value.elements : [];
    });

    function addText() {
      const newComponent = new TextComponent(100, 100, "my text");
      if (overlay.value) {
        overlay.value.elements = [...overlay.value.elements, newComponent];
      }
      selection.value = newComponent;
    }

    watch(
      () => selection.value,
      newValue => {
        if (overlay.value && newValue) {
          const component = overlay.value.elements.find(
            element => element.id === newValue?.id
          );
          if (
            component instanceof TextComponent &&
            newValue instanceof TextComponent &&
            !component.equals(newValue)
          ) {
            overlay.value.elements = [
              ...overlay.value?.elements.filter(e => e.id !== component.id),
              newValue
            ];
          }
        }
      }
    );

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
            alert("Save error");
          }
        }
      }
    );

    return {
      loadPromise,
      overlay,
      components,
      addText,
      selection
    };
  }
});
</script>
