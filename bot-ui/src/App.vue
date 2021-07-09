<template>
  <div class="h-screen w-screen flex flex-col">
    <status-bar />
    <div class="flex-grow flex flex-row">
      <Menu class="w-52 flex-shrink-0" />
      <div class="p-3 w-full">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Menu from "@/common/components/common/Menu.vue";
import StatusBar from "@/common/components/common/StatusBar.vue";

export default {
  name: "bot-ui",
  components: {
    StatusBar,
    Menu
  }
};
</script>
