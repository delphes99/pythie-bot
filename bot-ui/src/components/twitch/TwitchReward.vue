<template>
  <panel title="Rewards">
    <div class="flex flex-row flex-wrap">
      <card v-for="reward in rewards" :key="reward.title" :title="reward.title">
        {{reward.cost}}
        <template v-slot:actions>
          <button class="primary-button">
            Edit
          </button>
          <button class="secondary-button">
            Delete
          </button>
        </template>
      </card>
    </div>
  </panel>
</template>

<script lang="ts">
import Panel from "@/components/common/Panel.vue";
import {defineComponent, inject, ref} from "vue";
import Card from "@/components/common/Card.vue";

interface Reward {
  title: string,
  cost: number
}

export default defineComponent({
  name: "TwitchReward",
  components: {Card, Panel},
  props: {
    channelName: String
  },
  setup(props) {
    const backendUrl = inject("backendUrl")
    const rewards = ref([])

    async function getRewards() {
      const response = await fetch(`${backendUrl}/twitch/${props.channelName}/rewards`);
      rewards.value = await response.json()
    }

    getRewards()

    return {
      rewards
    }
  }
})
</script>