<template>
  <status-bar />
  <div class="flex">
    <Menu class="w-52" />
    <router-view/>
  </div>
</template>

<script>
import Menu from "@/components/Menu";
import StatusBar from "@/components/StatusBar";

export default {
  name: "bot-ui",
  components: {
    StatusBar,
    Menu
  }
}
</script>
