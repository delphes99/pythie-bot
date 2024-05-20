<template>
  <UiCard :title="overlay.title">
    {{ overlay.resolution.width }} x {{ overlay.resolution.height }}
    <template #actions>
      <UiButton
          :router-link="`/overlay/${overlay.id}`"
          label="common.edit"
      />
      <UiButton
          :type="UiButtonType.Secondary"
          label="common.delete"
          @on-click="$emit('deleted')"
      />
    </template>
  </UiCard>
</template>

<script lang="ts" setup>
import UiButton from "@/common/designSystem/button/UiButton.vue"
import UiButtonType from "@/common/designSystem/button/UiButtonType"
import UiCard from "@/common/designSystem/card/UiCard.vue"
import Overlay from "@/overlay/Overlay"
import {PropType} from "vue"

defineProps({
  overlay: {
    type: Object as PropType<Overlay>,
    required: true,
  },
})

defineEmits(["deleted"])
</script>