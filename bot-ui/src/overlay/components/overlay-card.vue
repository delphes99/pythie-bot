<template>
  <ui-card :title="overlay.title">
    {{ overlay.resolution.width }} x {{ overlay.resolution.height }}
    <template #actions>
      <ui-button
          :router-link="`/overlay/${overlay.id}`"
          label="common.edit"
      />
      <ui-button
          :type="UiButtonType.Secondary"
          label="common.delete"
          @on-click="$emit('deleted')"
      />
    </template>
  </ui-card>
</template>

<script lang="ts" setup>
import UiButtonType from "@/common/designSystem/button/ui-button.type"
import UiButton from "@/common/designSystem/button/ui-button.vue"
import UiCard from "@/common/designSystem/card/ui-card.vue"
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