<template>
  <div
    v-if="isOpen"
    class="fixed top-0 left-0 w-screen h-screen bg-black bg-opacity-50 flex items-center justify-center"
    v-on:click="close()"
  >
    <div
      id="modal"
      class="w-3/4 min-h-1/3 opacity-100 bg-white rounded shadow-lg"
      v-on:click.stop
    >
      <panel :title="title">
        <slot></slot>
      </panel>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import Panel from "@/common/components/common/Panel.vue";

export default defineComponent({
  name: "Modal",
  components: { Panel },
  emits: ["update:isOpen"],
  props: {
    isOpen: Boolean,
    title: String
  },
  setup(props, { emit }) {
    const close = () => {
      emit("update:isOpen", false);
    };

    return {
      close
    };
  }
});
</script>
