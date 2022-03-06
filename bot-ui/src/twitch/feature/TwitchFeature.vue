<template>
  <panel title="Features" />
</template>

<script lang="ts">
import Panel from "@/common/components/common/Panel.vue"
import { defineComponent } from "vue"

export default defineComponent({
  name: "TwitchFeatures",
  components: { Panel },
  props: {
    channelName: {
      type: String,
      required: true,
    },
  },
})
</script>
