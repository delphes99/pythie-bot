<template>
  <nav class="h-screen flex flex-col nav-color">
    <router-link class="router-link" to="/">
      <img src="@/assets/home.svg" width="20" />
      Home
    </router-link>
    <router-link class="router-link" to="/about">
      <img src="@/assets/about.svg" width="20" />
      About
    </router-link>
  </nav>
</template>

<style>
  .router-link {
    @apply my-1 border-black space-x-2 p-2 items-center flex flex-row font-bold
  }

  .router-link:hover {
    @apply bg-blue-900 text-white
  }

  .router-link:hover img {
    filter: invert(100%);
  }

  .router-link img {
    @apply mr-2
  }
</style>

<script lang="ts">
export default {
  name: 'Menu'
}
</script>