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
      <ui-icon name="home" class="menu-icon"/>
      {{ $t("home") }}
    </router-link>
    <router-link
        class="router-link"
        to="/monitoring"
    >
      <ui-icon name="monitoring" class="menu-icon"/>
      {{ $t("monitoring.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/features"
    >
      <ui-icon name="features" class="menu-icon"/>
      {{ $t("features.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/overlay"
    >
      <ui-icon name="overlay" class="menu-icon"/>
      {{ $t("overlay.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/medias"
    >
      <ui-icon name="medias" class="menu-icon"/>
      {{ $t("medias.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/settings"
    >
      <ui-icon name="settings" class="menu-icon"/>
      {{ $t("settings.title") }}
    </router-link>
    <router-link
        class="router-link"
        to="/about"
    >
      <ui-icon name="about" class="menu-icon"/>
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

.menu-icon,
.router-link img {
  @apply mr-5 w-5 h-5 fill-backgroundTextColor;
}
</style>
