<template>
  <div>
    <div class="bg-primaryContainerBackground text-primaryTextColor shadow h-full flex flex-col">
      <div class="flex flex-row shrink space-x-2">
        <slot name="icon"/>
        <h1
            v-if="title"
            class="w-full h-full p-2 text-xl"
        >
          {{ title }}
        </h1>
      </div>
      <div class="grow shrink-0 p-2">
        <slot/>
      </div>
      <div>
        <slot name="actions"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
defineProps({
  title: {
    type: String,
    default: null,
  },
})
</script>