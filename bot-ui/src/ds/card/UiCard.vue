<template>
  <div class="p-2 lg:w-1/4 md:w-1/3 w-1/2">
    <div class="border-primaryColor border-2 h-full flex flex-col">
      <div class="flex flex-row shrink space-x-2">
        <slot name="icon" />
        <h1
          v-if="title"
          class="w-full h-full p-2 bg-primaryColor text-primaryTextColor text-xl"
        >
          {{ title }}
        </h1>
      </div>
      <div class="grow shrink-0 p-2">
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