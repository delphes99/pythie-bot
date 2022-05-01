<template>
  <div
    v-if="isOpen"
    class="fixed top-0 left-0 w-screen h-screen bg-black bg-opacity-50 flex items-center justify-center"
    @click="close()"
  >
    <div
      id="modal"
      class="w-3/4 min-h-1/3 opacity-100 default-background rounded shadow-lg"
      @click.stop
    >
      <panel :title="title">
        <slot />
      </panel>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue"
import Panel from "@/common/components/common/Panel.vue"

export default defineComponent({
  name: "CommonModal",
  components: { Panel },
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
    title: {
      type: String,
      required: true,
    },
  },
  emits: ["update:isOpen"],
  setup(props, { emit }) {
    const close = () => {
      emit("update:isOpen", false)
    }

    return {
      close,
    }
  },
})
</script>
