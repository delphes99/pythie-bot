<template>
  <div
    class="w-screen flex flex-row items-center justify-around primary-color z-50"
  >
    <div class="flex-grow">
      <h1 class="pl-2 text-2xl font-extrabold text-white">Pythie-bot</h1>
    </div>
    <div class="flex-grow-0 right-0 mx-3">
      <div
        class="flex flex-row-reverse items-center space-x-reverse space-x-3 h-16 px-5"
      >
        <obs-status></obs-status>
        <twitch-status></twitch-status>
        <discord-status></discord-status>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import DiscordStatus from "@/discord/components/DiscordStatus.vue";
import ObsStatus from "@/obs/components/ObsStatus.vue";
import TwitchStatus from "@/twitch/components/TwitchStatus.vue";

export default {
  name: `StatusBar`,
  components: { ObsStatus, TwitchStatus, DiscordStatus }
};
</script>
