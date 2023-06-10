<template>
  <div>
    <label
        v-if="title"
        class="block text-titleColor text-sm font-semibold mb-2"
    >{{ $t(title) }}</label>
    <RadioGroup v-model="model">
      <div class="flex flex-row items-center space-x-2 my-2">
        <RadioGroupOption v-for="option in options.values"
                          :key="option.id"
                          v-slot="{ checked }"
                          :value="option.value"
                          as="template">
          <span
              :class="checked ? 'bg-primaryColor' : 'bg-backgroundColor'"
              class="px-2 py-1 borderborder-primaryContainerBackground rounded-lg shadow-sm cursor-pointer">
            {{ option.label() }}
          </span>
        </RadioGroupOption>
      </div>
    </RadioGroup>
  </div>
</template>

<script lang="ts" setup>
import {Options} from "@/common/designSystem/form/radio/Options"
import {RadioGroup, RadioGroupOption,} from '@headlessui/vue'
import {useVModel} from "@vueuse/core";
import {PropType} from "vue"

const props = defineProps({
  modelValue: {
    type: [Object, String] as PropType<unknown>,
    required: true,
  },
  name: {
    type: String,
    required: true,
  },
  options: {
    type: Object as PropType<Options<unknown>>,
    required: true,
  },
  title: {
    type: String,
    default: null,
    nullable: true,
  },
})

const emits = defineEmits(["update:modelValue"])

const model = useVModel(props, "modelValue", emits)
</script>