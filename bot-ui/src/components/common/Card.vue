<template>
  <div class="m-0 lg:w-1/4 md:w-1/3 w-1/2 p-2">
    <div class="border shadow-md p-3">
      <h1 class="text-xl font-bold">{{title}}</h1>
      <div>
        <slot></slot>
      </div>
      <div>
        <slot name="actions"></slot>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
export default {
  name: "Card",
  props: {
    title: {
      type: String,
      required: true
    }
  }
}
</script>