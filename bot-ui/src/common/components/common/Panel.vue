<template>
  <div class="border-b border-2 mb-2">
    <div class="primary-color p-2">
      <h1 class="inline text-2xl font-medium title-font">
        {{ title }}
      </h1>
    </div>
    <div>
      <slot />
    </div>
  </div>
</template>

<script lang="ts">
export default {
  name: "CommonPanel",
  props: {
    title: {
      type: String,
      required: true,
    },
  },
}
</script>
