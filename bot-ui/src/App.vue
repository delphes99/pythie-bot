<template>
  <div>
    <status-bar class="fixed top-0 right-0 h-16" />
    <div class="mt-16">
      <Menu class="fixed top-16 left-0" />
      <div class="ml-16 h-full overflow-auto">
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
