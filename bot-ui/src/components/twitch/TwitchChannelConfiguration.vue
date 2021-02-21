<template>
  <h1>{{channelName}}</h1>
  <router-view></router-view>
</template>

<script lang="ts">

export default {
  name: "TwitchChannelConfiguration",
  props: ['channelName']
}
</script>