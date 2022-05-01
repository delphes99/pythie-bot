<template>
  {{ $t('configuration.outgoingEvents.' + event.type) }}
  Response : {{ event.text }}
  Channel : {{ event.channel }}
</template>
<script setup lang="ts">
import TwitchOutgoingSendMessage from "@/features/outgoingevents/twitch/twitch-outgoing-send-message/TwitchOutgoingSendMessage"
import {PropType} from "vue"

const props = defineProps({
  event: {
    type: Object as PropType<TwitchOutgoingSendMessage>,
    required: true,
  },
})
</script>