<script setup lang="ts" generic="T">
defineProps<{
  item: T;
}>();
</script>

<template>
  <td>
    <slot :item="item"
    ></slot>
  </td>
</template>