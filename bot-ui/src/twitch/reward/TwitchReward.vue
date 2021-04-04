<template>
  <panel title="Rewards">
    <card-panel>
      <card v-for="reward in rewards" :key="reward.title" :title="reward.title">
        {{ reward.cost }}
        <template v-slot:actions>
          <button class="primary-button">
            Edit
          </button>
          <button class="secondary-button">
            Delete
          </button>
        </template>
      </card>
    </card-panel>
  </panel>
</template>

<script lang="ts">
import Panel from "@/common/components/common/Panel.vue";
import { defineComponent, inject, ref } from "vue";
import Card from "@/common/components/common/Card.vue";
import CardPanel from "@/common/components/common/CardPanel.vue";

interface Reward {
  title: string;
  cost: number;
}

export default defineComponent({
  name: "TwitchReward",
  components: { CardPanel, Card, Panel },
  props: {
    channelName: String
  },
  setup(props) {
    const backendUrl = inject("backendUrl");
    const rewards = ref([]);

    async function getRewards() {
      const response = await fetch(
        `${backendUrl}/twitch/${props.channelName}/rewards`
      );
      rewards.value = await response.json();
    }

    getRewards();

    return {
      rewards
    };
  }
});
</script>
