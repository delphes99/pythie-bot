<template>
  <loading-promise :loading-promise="loadPromise">
    <div class="flex flex-row">
      <div class="w-1/6" :v-if="selection">
        <editor-props
          v-model:selection="selection"
          @add-text="addText"
        ></editor-props>
        <editor-component-list :components="components"></editor-component-list>
      </div>
      <div class="w-5/6">
        <editor-preview
          :overlay="overlay"
          v-model:selection="selection"
        ></editor-preview>
      </div>
    </div>
  </loading-promise>
</template>

<script lang="ts">
import LoadingPromise from "@/common/components/common/LoadingPromise.vue";
import { useLoadingPromise } from "@/common/composition/UseLoadingPromise.ts";
import EditorComponentList from "@/overlay/editor/EditorComponentList.vue";
import EditorPreview from "@/overlay/editor/EditorPreview.vue";
import EditorProps from "@/overlay/editor/EditorProps.vue";
import TextComponent, { fromObject } from "@/overlay/editor/textComponent.ts";
import { OverlayElement } from "@/overlay/OverlayElement.js";
import OverlayRepository from "@/overlay/OverlayRepository.ts";
import { computed, defineComponent, ref, watch } from "vue";

export default defineComponent({
  name: `OverlayEditor`,
  components: {
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
    const loadPromise = useLoadingPromise(
      OverlayRepository.get(props.overlayId)
    );
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
          const component = overlay.value?.elements.find(
            element => element.id === newValue?.id
          );
          if (
            component instanceof TextComponent &&
            newValue instanceof TextComponent
          ) {
            const updatedElement = {
              ...component,
              left: newValue.left,
              top: newValue.top,
              text: newValue.text
            };

            overlay.value.elements = [
              ...overlay.value?.elements.filter(e => e.id !== component.id),
              fromObject(updatedElement)
            ];
          }
        }
      }
    );

    watch(
      () => components.value,
      _ => {
        //TODO save
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
