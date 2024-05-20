<template>
  <component :is="icon" v-bind="$attrs"/>
</template>

<script setup lang="ts">
import {defineAsyncComponent} from 'vue';

const props = defineProps({
  name: {
    type: String,
    required: true,
  },
});

const icon = defineAsyncComponent(() =>
    import(`./${props.name}.svg?component`)
);
</script>