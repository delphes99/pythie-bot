<template>
  <status-bar />
  <div class="flex">
    <Menu class="w-52" />
    <div class="m-3 w-full">
      <router-view />
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
