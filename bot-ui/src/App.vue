<template>
  <status-bar />
  <div class="flex">
    <Menu class="w-52" />
    <router-view/>
  </div>
</template>

<script lang="ts">
import Menu from "@/components/Menu.vue";
import StatusBar from "@/components/StatusBar.vue";

export default {
  name: "bot-ui",
  components: {
    StatusBar,
    Menu
  }
}
</script>
