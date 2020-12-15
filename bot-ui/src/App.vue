<template>
  <div class="flex">
    <Nav class="w-52" />
    <div>
      <router-view />
    </div>
  </div>
</template>

<script>
import Nav from "@/components/Nav";
export default {
  components: {
    Nav
  }
}
</script>