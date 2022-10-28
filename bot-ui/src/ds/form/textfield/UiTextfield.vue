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
    @change="$emit('change')"
  >
</template>

<script setup lang="ts">
import { computed } from "vue"
import { v4 as uuid } from "uuid"

const props = defineProps({
  id: {
    type: String,
    default: () => uuid(),
  },
  modelValue: {
    type: String,
    require: true,
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

const emit = defineEmits(["update:modelValue", "change"])

const model = computed({
  get() {
    return props.modelValue
  },

  set(value) {
    return emit("update:modelValue", value)
  },
})
</script>