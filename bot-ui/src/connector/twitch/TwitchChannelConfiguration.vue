<template>
  <h1>{{ channelName }}</h1>
  <div class="w-full flex flex-row">
    <RouterLink
        :to="`/twitch/${channelName}/rewards`"
        active-class="active-link"
        class="link"
    >
      Rewards
    </RouterLink>
    <RouterLink
        :to="`/twitch/${channelName}/features`"
        active-class="active-link"
        class="link"
    >
      Features
    </RouterLink>
  </div>
  <div class="p-4 bg-gray-300 bg-black">
    <RouterView/>
  </div>
</template>

<script lang="ts" setup>
defineProps({
  channelName: {
    type: String,
    required: true,
  },
})
</script>

<style lang="css" scoped>
.link {
  @apply mr-2 px-3 bg-gray-300 text-center;
}

.active-link {
  @apply bg-gray-500;
}
</style>
