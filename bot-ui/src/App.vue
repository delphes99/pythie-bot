<template>
  <div :class="themeClass">
    <div class="box-border w-screen h-screen overflow-hidden bg-backgroundColor text-backgroundTextColor">
      <status-bar
          class="fixed top-0 right-0"
          style="height: 4rem"
      />
      <menu-panel
          class="fixed top-16 left-0"
          width-class="w-12"
      />
      <div
          class="mt-16 ml-12 p-8 h-full overflow-auto"
          style="height: calc(100% - 4rem)"
      >
        <suspense>
          <RouterView/>
          <template #fallback>
            Loading...
          </template>
        </suspense>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import MenuPanel from "@/common/designSystem/menu/MenuPanel.vue"
import StatusBar from "@/common/designSystem/menu/StatusBar.vue"
import {useApplicationTheme} from "@/common/style/UseApplicationThemeStore"

const {themeClass} = useApplicationTheme()
</script>