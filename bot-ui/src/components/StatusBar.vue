<template>
  <div class="flex flex-row items-center justify-around primary-color">
    <div class="flex-grow">
      <h1 class="pl-2 text-2xl font-extrabold text-white">Pythie-bot</h1>
    </div>
    <div class="right-0 mx-auto">
      <div class="flex flex-row-reverse items-center space-x-reverse space-x-3 h-16 px-5">
        <twitch-status></twitch-status>
        <discord-status></discord-status>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import DiscordStatus from "@/components/discord/DiscordStatus.vue";
import TwitchStatus from "@/components/twitch/TwitchStatus.vue";

export default {
  name: `StatusBar`,
  components: {TwitchStatus, DiscordStatus}
}
</script>