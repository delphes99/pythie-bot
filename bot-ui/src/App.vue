<template>
  <div :class="isDark() ? 'dark ' : ''">
    <div class="box-border w-screen h-screen overflow-hidden default-background">
      <status-bar
        class="fixed top-0 right-0"
        style="height: 4rem"
      />
      <Menu
        class="fixed top-16 left-0"
        width-class="w-12"
      />
      <div
        class="mt-16 ml-12 p-4 h-full overflow-auto"
        style="height: calc(100% - 4rem)"
      >
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Menu from "@/common/components/common/Menu.vue"
import StatusBar from "@/common/components/common/StatusBar.vue"
import { Themes } from "@/common/components/common/theme/Themes"
import { useApplicationTheme } from "@/common/components/common/theme/UseApplicationTheme"

const storedTheme = useApplicationTheme()

const isDark = () => storedTheme.theme === Themes.DARK
</script>