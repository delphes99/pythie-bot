<template>
  <nav class="flex flex-row items-center justify-around nav-color">
    <div class="flex-grow">
      <h1 class="pl-2 text-2xl font-extrabold text-white">Pythie-bot</h1>
    </div>
    <div class="right-0 mx-auto">
      <div class="flex flex-row-reverse items-center space-x-reverse space-x-3 h-16 px-5">
        <twitch-status></twitch-status>
        <discord-status></discord-status>
      </div>
    </div>
  </nav>
</template>

<script>
import DiscordStatus from "@/components/DiscordStatus";
import TwitchStatus from "@/components/TwitchStatus";

export default {
  name: `StatusBar`,
  components: {TwitchStatus, DiscordStatus}
}
</script>