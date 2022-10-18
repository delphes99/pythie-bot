<template>
  <div v-if="isLoading">
    Loading
  </div>
  <slot v-else />
</template>

<script setup lang="ts">
import { LoadPromise } from "@/common/composition/UseLoadingPromise"
import { PropType, toRefs } from "vue"

const props = defineProps({
  loadingPromise: {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    type: Object as PropType<LoadPromise<any>>,
    required: true,
  },
})

// loadingPromise attributes are reactive
// eslint-disable-next-line vue/no-setup-props-destructure
const isLoading = props.loadingPromise.isLoading
</script>
