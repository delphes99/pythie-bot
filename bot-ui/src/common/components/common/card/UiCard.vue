<template>
  <div class="m-0 lg:w-1/4 md:w-1/3 w-1/2 p-2">
    <div class="border shadow-md p-3 h-full flex flex-col">
      <div class="flex flex-row flex-shrink space-x-2">
        <slot name="icon" />
        <h1
          v-if="title"
          class="text-xl font-bold"
        >
          {{ title }}
        </h1>
      </div>
      <div class="flex-grow flex-shrink-0">
        <slot />
      </div>
      <div>
        <slot name="actions" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    default: null,
  },
})
</script>