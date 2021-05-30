<template>
  <panel title="Features">
    <card-panel>
      <overlay-card
        v-for="overlay in overlays"
        :key="overlay"
        :overlay="overlay"
      ></overlay-card>
    </card-panel>
  </panel>
</template>

<script>
import CardPanel from "@/common/components/common/CardPanel.vue";
import Panel from "@/common/components/common/Panel.vue";
import OverlayCard from "@/overlay/components/OverlayCard.vue";
import OverlayRepository from "@/overlay/OverlayRepository.ts";
import { ref } from "vue";

export default {
  name: `OverlaysList`,
  components: { OverlayCard, CardPanel, Panel },
  setup() {
    const overlays = ref([]);

    async function getFeatures() {
      overlays.value = await OverlayRepository.list();
    }

    getFeatures();

    return {
      overlays
    };
  }
};
</script>
