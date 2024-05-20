<template>
  <label
      v-if="label"
      :for="id"
      class="block text-titleColor text-sm font-semibold mb-2"
  >{{ $t(label) }}</label>
  <input
      :id="id"
      v-model="model"
      :name="name"
      :type="password ? 'password' : 'text'"
      class="w-full py-2 px-3 border border-primaryColor rounded-md shadow-sm text-sm leading-4 font-medium  bg-inputColor text-inputTextColor focus:outline-none focus:ring-2;"
  >
</template>

<script lang="ts" setup>
import {useVModel} from "@vueuse/core";

const props = defineProps({
  id: {
    type: String,
    default: () => crypto.randomUUID(),
  },
  modelValue: {
    type: String,
    default: "",
    required: true,
  },
  label: {
    type: String,
    default: null,
  },
  name: {
    type: String,
    default: (props: { id: string }) => props.id,
  },
  password: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(["update:modelValue"])

const model = useVModel(props, 'modelValue', emit)
</script>