<template>
  <UiPanel title="Rewards">
    <UiCardPanel>
      <UiCard
          v-for="reward in rewards"
          :key="reward.title"
          :title="reward.title"
      >
        {{ reward.cost }}
        <template #actions>
          <UiButton label="common.edit"/>
          <UiButton
              :type="UiButtonType.Warning"
              label="common.delete"
          />
        </template>
      </UiCard>
    </UiCardPanel>
  </UiPanel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue"
import UiButtonType from "@/common/designSystem/button/UiButtonType"
import UiCard from "@/common/designSystem/card/UiCard.vue"
import UiCardPanel from "@/common/designSystem/card/UiCardPanel.vue"
import UiPanel from "@/common/designSystem/panel/UiPanel.vue"
import {autowired} from "@/common/utils/Injection.util";
import {ref} from "vue"

const props = defineProps({
  channelName: {
    type: String,
    required: true,
  },
})

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const rewards = ref([])

async function getRewards() {
  const response = await fetch(`${backendUrl}/twitch/${props.channelName}/rewards`)
  rewards.value = await response.json()
}

getRewards()
</script>