<template>
  Command :
  <div>Channel : {{ feature.channel }}</div>
  <div>Command : {{ feature.trigger }}</div>
</template>
<script type="ts">
import TwitchCommand from "@/twitch/feature/type/TwitchCommand.ts"
import {defineComponent} from "vue";

export default defineComponent({
  name: 'feature-command-card',
  props: {
    feature: TwitchCommand
  },
})
</script>
