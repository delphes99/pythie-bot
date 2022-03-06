<template>
  <div class="box-border w-screen h-screen overflow-hidden">
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
</template>

<script lang="ts">
import Menu from "@/common/components/common/Menu.vue"
import StatusBar from "@/common/components/common/StatusBar.vue"

export default {
  name: "BotUi",
  components: {
    StatusBar,
    Menu,
  },
}
</script>
