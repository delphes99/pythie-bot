<template>
  <nav class="h-screen bg-gray-500 p-6">
    <h1 class="h category">Connecteurs</h1>
    <router-link class="router-link" to="/">Twitch</router-link>
    <router-link class="router-link" to="/"> - Rewards</router-link>
    <router-link class="router-link" to="/"> - Commands</router-link>
    <router-link class="router-link" to="/">Discord</router-link>
    <router-link class="router-link" to="/">OBS</router-link>
    <h1 class="category">Features</h1>
    <router-link class="router-link" to="/about">Liste</router-link>
    <router-link class="router-link" to="/about">Ajouter</router-link>
    <h1 class="category">Tests</h1>
    <router-link class="router-link" to="/about">Trigger events</router-link>
    <h1 class="category">Analytics</h1>
  </nav>
</template>

<style>
  .router-link {
    @apply bg-blue-200 flex flex-col
  }
</style>

<script>
export default {
  name: 'Nav'
}
</script>