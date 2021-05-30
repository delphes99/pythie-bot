<template>
  <div v-if="isLoading">Loading</div>
  <div v-else class="flex flex-row">
    <div class="w-1/6">
      <editor-props :bus="bus"></editor-props>
    </div>
    <div class="w-5/6">
      <editor-preview :overlay="overlay" :bus="bus"></editor-preview>
    </div>
  </div>
</template>

<script lang="ts">
import { useLoadingPromise } from "@/common/composition/UseLoadingPromise.ts";
import EditorPreview from "@/overlay/editor/EditorPreview.vue";
import EditorProps from "@/overlay/editor/EditorProps.vue";
import OverlayRepository from "@/overlay/OverlayRepository.ts";
import mitt from "mitt";
import { defineComponent } from "vue";

export default defineComponent({
  name: `OverlayEditor`,
  components: { EditorPreview, EditorProps },
  props: {
    overlayId: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const bus = mitt();

    const { isLoading, data: overlay } = useLoadingPromise(
      OverlayRepository.get(props.overlayId)
    );

    return {
      overlay,
      bus,
      isLoading
    };
  }
});
</script>
