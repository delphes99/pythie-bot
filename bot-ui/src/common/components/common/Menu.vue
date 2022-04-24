<template>
  <nav
    class="h-full flex flex-col primary-color text-black overflow-hidden"
    :class="expand ? 'w-52' : widthClass"
    :onmouseenter="mouseIn"
    :onmouseleave="mouseOut"
  >
    <router-link
      class="router-link"
      to="/"
    >
      <img
        src="@/common/assets/home.svg"
        width="20"
      >
      {{ $t("home") }}
    </router-link>
    <router-link
      class="router-link"
      to="/features"
    >
      <img
        src="@/common/assets/features.svg"
        width="20"
      >
      {{ $t("features") }}
    </router-link>
    <router-link
      class="router-link"
      to="/overlay"
    >
      <img
        src="@/common/assets/overlay.svg"
        width="20"
      >
      {{ $t("overlay") }}
    </router-link>
    <router-link
      class="router-link"
      to="/medias"
    >
      <img
        src="@/common/assets/medias.svg"
        width="20"
      >
      {{ $t("medias.title") }}
    </router-link>
    <router-link
      class="router-link"
      to="/settings"
    >
      <img
        src="@/common/assets/settings.svg"
        width="20"
      >
      {{ $t("settings.title") }}
    </router-link>
    <router-link
      class="router-link"
      to="/about"
    >
      <img
        src="@/common/assets/about.svg"
        width="20"
      >
      {{ $t("about") }}
    </router-link>
  </nav>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue"

export default defineComponent({
  name: "GlobalMenu",
  props: {
    widthClass: {
      type: String,
      required: true,
    },
  },
  setup() {
    const expand = ref(false)

    const mouseIn = () => {
      expand.value = true
    }
    const mouseOut = () => {
      expand.value = false
    }

    return {
      expand,
      mouseIn,
      mouseOut,
    }
  },
})
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
