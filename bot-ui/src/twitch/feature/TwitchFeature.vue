<template>
  <panel title="Features"> </panel>
</template>

<script lang="ts">
import Panel from "@/common/components/common/Panel.vue";
import { defineComponent } from "vue";

export default defineComponent({
  name: "TwitchFeatures",
  components: { Panel },
  props: {
    channelName: String
  }
});
</script>
