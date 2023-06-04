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
          <ui-button label="common.edit"/>
          <ui-button
              :type="UiButtonType.Warning"
              label="common.delete"
          />
        </template>
      </ui-card>
    </ui-card-panel>
  </ui-panel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButtonType from "@/common/designSystem/button/ui-button.type"
import UiButton from "@/common/designSystem/button/ui-button.vue"
import UiCardPanel from "@/common/designSystem/card/ui-card-panel.vue"
import UiCard from "@/common/designSystem/card/ui-card.vue"
import UiPanel from "@/common/designSystem/panel/ui-panel.vue"
import {autowired} from "@/common/utils/injection.util";
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