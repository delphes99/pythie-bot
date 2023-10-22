<template>
  <div>
    <label
        v-if="label"
        :for="id"
    >{{ $t(label) }}</label>
    <select
        :id="id"
        v-model="model"
        class="w-full h-10 pl-3 pr-6 text-base placeholder-gray-600 bg-white text-black border rounded-lg appearance-none focus:shadow-outline"
    >
      <option
          v-for="option in options"
          :key="option"
          :value="option.option"
      >
        {{ option.display() }}
      </option>
    </select>
    <div class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none">
      <svg
          class="w-4 h-4 fill-current"
          viewBox="0 0 20 20"
      >
        <path
            clip-rule="evenodd"
            d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
            fill-rule="evenodd"
        />
      </svg>
    </div>
  </div>
</template>
<script lang="ts" setup>
import {UiSelectOption} from "@/common/designSystem/form/select/ui-select.option"
import {useVModel} from "@vueuse/core"
import {PropType} from "vue"

const id = crypto.randomUUID()

const props = defineProps({
  modelValue: {
    type: Object as PropType<unknown>,
    default: null,
  },
  options: {
    type: Object as PropType<UiSelectOption<unknown>[]>,
    default: null,
  },
  label: {
    type: String,
    default: null,
  },
})

const emits = defineEmits(["update:modelValue"])

const model = useVModel(props, "modelValue", emits)
</script>
