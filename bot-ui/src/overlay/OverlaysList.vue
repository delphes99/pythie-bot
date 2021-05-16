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
import Panel from "@/common/components/common/Panel";
import { ref } from "vue";
import CardPanel from "@/common/components/common/CardPanel";
import OverlayCard from "@/overlay/components/OverlayCard";
import OverlayRepository from "@/overlay/OverlayRepository";

export default {
  name: `OverlaysList`,
  components: { OverlayCard, CardPanel, Panel },
  setup() {
    const overlays = ref([]);

    async function getFeatures() {
      overlays.value = OverlayRepository.list();
    }

    getFeatures();

    return {
      overlays
    };
  }
};
</script>
