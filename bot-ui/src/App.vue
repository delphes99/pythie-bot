<template>
  <status-bar />
  <div class="flex">
    <Nav class="w-52" />
    <div>
      <router-view/>
    </div>
  </div>
</template>

<script>
import Nav from "@/components/Nav";
import StatusBar from "@/components/StatusBar";

export default {
  name: "bot-ui",
  components: {
    StatusBar,
    Nav
  }
}
</script>
