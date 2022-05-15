<template>
  <div class="border-b border-2 mb-2">
    <div class="menu-colors p-2">
      <h1 class="inline text-2xl font-medium title-font">
        {{ $t(title) }}
      </h1>
    </div>
    <div class="p-4">
      <slot />
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    required: true,
  },
})
</script>