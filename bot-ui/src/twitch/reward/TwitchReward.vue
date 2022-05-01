<template>
  <ui-panel title="Rewards">
    <ui-card-panel>
      <ui-card
        v-for="reward in rewards"
        :key="reward.title"
        :title="reward.title"
      >
        {{ reward.cost }}
        <template #actions>
          <button class="primary-button">
            Edit
          </button>
          <button class="secondary-button">
            Delete
          </button>
        </template>
      </ui-card>
    </ui-card-panel>
  </ui-panel>
</template>

<script setup lang="ts">
import UiCard from "@/common/components/common/card/UiCard.vue"
import UiCardPanel from "@/common/components/common/card/UiCardPanel.vue"
import UiPanel from "@/common/components/common/panel/UiPanel.vue"
import { inject, ref } from "vue"

const props = defineProps({
  channelName: {
    type: String,
    required: true,
  },
})

const backendUrl = inject("backendUrl")
const rewards = ref([])

async function getRewards() {
  const response = await fetch(`${backendUrl}/twitch/${props.channelName}/rewards`)
  rewards.value = await response.json()
}

getRewards()
</script>