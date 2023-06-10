<template>
  <label
      v-if="label"
      :for="id"
      class="block text-titleColor text-sm font-semibold mb-2"
  >{{ $t(label) }}</label>
  <Switch
      v-model="model"
      :class="model ? 'bg-primaryColor' : 'bg-backgroundColor'"
      class="relative inline-flex h-6 w-11 items-center rounded-full"
      :id="id"
  >
    <span class="sr-only">Enable notifications</span>
    <span
        :class="model ? 'translate-x-6 bg-primaryColorHover' : 'translate-x-1 bg-primaryContainerBackground'"
        class="inline-block h-4 w-4 transform rounded-full transition"
    />
  </Switch>
</template>

<script setup lang="ts">
import {Switch} from '@headlessui/vue'
import {useVModel} from "@vueuse/core";
import {v4 as uuid} from "uuid";

const props = defineProps({
  id: {
    type: String,
    default: () => uuid(),
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
  label: {
    type: String,
    default: null,
  },
})

const emits = defineEmits(["update:modelValue"])

const model = useVModel(props, "modelValue", emits)
</script>