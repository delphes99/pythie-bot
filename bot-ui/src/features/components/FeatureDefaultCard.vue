<template>
  <div v-if="feature.channel">Channel : {{ feature.channel }}</div>
  <div v-if="feature.trigger">Command : {{ feature.trigger }}</div>
</template>

<script type="ts">
import Feature from "@/twitch/feature/type/Feature";

export default {
  name: 'feature-default-card',
  props: {
    feature: Feature
  }
}
</script>
