<template>
  <div
    class="flex flex-row items-center p-2 bg-primaryColor text-primaryTextColor"
    @click="toggle"
  >
    <svg
      xmlns="http://www.w3.org/2000/svg"
      class="h-3 fill-primaryTextColor mr-1"
      :class="!isCollapsed ? 'transform rotate-90' : ''"
      viewBox="0 0 1000 1000"
    >
      <path
        d="M79.9,990V10l840.2,485.5L79.9,990z"
      />
    </svg>
    <div>
      {{ $t(title) }}
    </div>
  </div>
  <div
    v-if="!isCollapsed"
    class="bg-backgroundColor backgroundTextColor p-2"
  >
    <slot />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue"

defineProps({
  title: {
    type: String,
    required: true,
  },
})

const isCollapsed = ref(false)

const toggle = () => {
  console.log("toggle")
  return isCollapsed.value = !isCollapsed.value
}
</script>

<style scoped>

</style>