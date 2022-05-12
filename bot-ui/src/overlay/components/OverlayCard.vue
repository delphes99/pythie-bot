<template>
  <ui-card :title="overlay.title">
    {{ overlay.resolution.width }} x {{ overlay.resolution.height }}
    <template #actions>
      <router-link :to="`/overlay/${overlay.id}`">
        <a class="primary-button focus:shadow-outline"> Edit </a>
      </router-link>
      <ui-button
        :type="UiButtonType.Secondary"
        label="common.delete"
        @on-click="$emit('deleted')"
      />
    </template>
  </ui-card>
</template>

<script setup lang="ts">
import UiCard from "@/common/components/common/card/UiCard.vue"
import UiButton from "@/ds/button/UiButton.vue"
import UiButtonType from "@/ds/button/UiButtonType"
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