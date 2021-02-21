<template>
  <div class="relative">
    <router-link to="/twitch">
      <img src="@/assets/twitch.png">
    </router-link>
    <div class="absolute -bottom-1 -right-1 z-2 rounded-full border-8 shadow-lg"  />
  </div>
</template>

<script lang="ts">
export default {
  name: `TwitchStatus`
}
</script>