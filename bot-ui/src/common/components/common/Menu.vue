<template>
  <nav class="h-full flex flex-col primary-color text-black">
    <router-link class="router-link" to="/">
      <img src="@/common/assets/home.svg" width="20" />
      {{ $t("home") }}
    </router-link>
    <router-link class="router-link" to="/features">
      <img src="@/common/assets/features.svg" width="20" />
      {{ $t("features") }}
    </router-link>
    <router-link class="router-link" to="/overlay">
      <img src="@/common/assets/overlay.svg" width="20" />
      {{ $t("overlay") }}
    </router-link>
    <router-link class="router-link" to="/settings">
      <img src="@/common/assets/settings.svg" width="20" />
      {{ $t("settings.title") }}
    </router-link>
    <router-link class="router-link" to="/about">
      <img src="@/common/assets/about.svg" width="20" />
      {{ $t("about") }}
    </router-link>
  </nav>
</template>

<style>
.router-link {
  @apply my-1 border-black space-x-2 p-4 items-center flex flex-row font-bold;
}

.router-link:hover {
  @apply bg-blue-900 text-white shadow-2xl;
}

.router-link img {
  @apply mr-5;
  filter: invert(100%);
}
</style>

<script lang="ts">
export default {
  name: "Menu"
};
</script>
