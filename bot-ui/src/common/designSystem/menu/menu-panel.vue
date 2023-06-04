<template>
  <nav
      :class="expand ? 'w-52' : widthClass"
      @mouseenter="mouseIn"
      @mouseleave="mouseOut"
      class="z-50 h-full flex flex-col bg-primaryColor text-primaryTextColor overflow-hidden"
  >
    <router-link
        class="router-link"
        to="/"
    >
      <img
          src="../../assets/home.svg"
          width="20"
      >
      {{ $t("home") }}
    </router-link>
    <router-link
        class="router-link"
        to="/monitoring"
    >
      <img
          src="../../assets/monitoring.svg"
          width="20"
      >
      {{ $t("monitoring.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/features"
    >
      <img
          src="../../assets/features.svg"
          width="20"
      >
      {{ $t("features.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/overlay"
    >
      <img
          src="../../assets/overlay.svg"
          width="20"
      >
      {{ $t("overlay.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/medias"
    >
      <img
          src="../../assets/medias.svg"
          width="20"
      >
      {{ $t("medias.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/settings"
    >
      <img
          src="../../assets/settings.svg"
          width="20"
      >
      {{ $t("settings.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/about"
    >
      <img
          src="../../assets/about.svg"
          width="20"
      >
      {{ $t("about") }}
    </router-link>
  </nav>
</template>

<script lang="ts" setup>
import {ref} from "vue"

defineProps({
  widthClass: {
    type: String,
    required: true,
  },
})

const expand = ref(false)

const mouseIn = () => {
  expand.value = true
}
const mouseOut = () => {
  expand.value = false
}

</script>

<style scoped>
.router-link {
  @apply w-52 my-1 border-black space-x-2 p-4 items-center flex flex-row font-bold;
}

.router-link:hover {
  @apply bg-blue-900 text-white shadow-2xl;
}

.router-link img {
  @apply mr-5;
  filter: invert(100%);
}
</style>
