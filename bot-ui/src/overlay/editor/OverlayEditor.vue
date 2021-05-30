<template>
  <loading-promise :loading-promise="loadPromise">
    <div class="flex flex-row">
      <div class="w-1/6">
        <editor-props :bus="bus"></editor-props>
      </div>
      <div class="w-5/6">
        <editor-preview :overlay="overlay" :bus="bus"></editor-preview>
      </div>
    </div>
  </loading-promise>
</template>

<script lang="ts">
import LoadingPromise from "@/common/components/common/LoadingPromise.vue";
import { useLoadingPromise } from "@/common/composition/UseLoadingPromise.ts";
import EditorPreview from "@/overlay/editor/EditorPreview.vue";
import EditorProps from "@/overlay/editor/EditorProps.vue";
import OverlayRepository from "@/overlay/OverlayRepository.ts";
import mitt from "mitt";
import { defineComponent } from "vue";

export default defineComponent({
  name: `OverlayEditor`,
  components: { LoadingPromise, EditorPreview, EditorProps },
  props: {
    overlayId: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const bus = mitt();

    const loadPromise = useLoadingPromise(
      OverlayRepository.get(props.overlayId)
    );

    return {
      loadPromise,
      overlay: loadPromise.data,
      bus
    };
  }
});
</script>
