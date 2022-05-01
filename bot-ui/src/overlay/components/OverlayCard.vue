<template>
  <ui-card :title="overlay.title">
    {{ overlay.resolution.width }} x {{ overlay.resolution.height }}
    <template #actions>
      <router-link :to="`/overlay/${overlay.id}`">
        <a class="primary-button focus:shadow-outline"> Edit </a>
      </router-link>
      <button
        class="secondary-button"
        @click="$emit('deleted')"
      >
        Delete
      </button>
    </template>
  </ui-card>
</template>

<script setup lang="ts">
import UiCard from "@/common/components/common/card/UiCard.vue"
import Overlay from "@/overlay/Overlay"
import { PropType } from "vue"

defineProps({
  overlay: {
    type: Object as PropType<Overlay>,
    required: true,
  },
})

defineEmits(["deleted"])
</script>