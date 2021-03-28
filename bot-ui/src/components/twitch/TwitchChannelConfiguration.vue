<template>
  <h1>{{ channelName }}</h1>
  <div class="w-full flex flex-row">
    <router-link :to="`/twitch/${channelName}/rewards`" class="link" active-class="active-link">
      Rewards
    </router-link>
    <router-link :to="`/twitch/${channelName}/features`" class="link" active-class="active-link">
      Features
    </router-link>
  </div>
  <div class="p-4 bg-gray-300 bg-black">
    <router-view></router-view>
  </div>
</template>

<script lang="ts">

export default {
  name: "TwitchChannelConfiguration",
  props: ['channelName']
}
</script>

<style lang="css" scoped>
  .link {
    @apply mr-2 px-3 bg-gray-300 text-center
  }

  .active-link {
    @apply bg-gray-500
  }
</style>