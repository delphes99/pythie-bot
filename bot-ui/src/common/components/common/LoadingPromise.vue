<template>
  <div v-if="isLoading">
    Loading
  </div>
  <slot v-else />
</template>

<script lang="ts">
import { LoadPromise } from "@/common/composition/UseLoadingPromise"
import { defineComponent, PropType } from "vue"

export default defineComponent({
  name: "LoadingPromise",
  props: {
    loadingPromise: {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      type: Object as PropType<LoadPromise<any>>,
      required: true,
    },
  },
  setup(props) {
    return {
      isLoading: props.loadingPromise.isLoading,
    }
  },
})
</script>
