<template>
  <template v-if="routerLink">
    <router-link :to="routerLink">
      <a
        class="px-5 py-2.5 text-base rounded-lg focus:shadow-outline"
        :class="classes"
      >{{ $t(label) }}</a>
    </router-link>
  </template>
  <template v-else-if="link">
    <a
      :href="link"
      class="px-5 py-2.5 text-base rounded-lg focus:shadow-outline"
      :class="classes"
      @click="onClick"
    >{{
      $t(label)
    }}</a>
  </template>
  <template v-else>
    <button
      type="button"
      class="px-5 py-2.5 text-base rounded-lg focus:shadow-outline"
      :class="classes"
    >
      {{ $t(label) }}
    </button>
  </template>
</template>

<script setup lang="ts">
import { UiButtonType } from "@/ds/button/UiButtonType"
import { computed } from "vue"

const props = defineProps({
  label: {
    type: String,
    required: true,
  },
  type: {
    type: String as () => UiButtonType,
    default: UiButtonType.Primary,
  },
  routerLink: {
    type: String,
    default: null,
  },
  link: {
    type: String,
    default: null,
  },
})

const emit = defineEmits(["on-click"])

const classes = computed(() => {
  return {
    "primary-button": props.type == UiButtonType.Primary,
    "secondary-button": props.type == UiButtonType.Secondary,
    "warning-button": props.type == UiButtonType.Warning,
  }
})

const onClick = () => {
  emit("on-click")
}
</script>
