<template>
  <UiTextfield
      :id="descriptor.fieldName"
      v-model="actualValue"
      :label="descriptor.description"
  />
</template>

<script lang="ts" setup>
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue";
import {StringDescriptor} from "@/common/dynamicForm/field/string/StringDescriptor";
import {computed, PropType} from "vue";

const emits = defineEmits<{
  modifyDescriptor: [descriptor: StringDescriptor]
}>()

const props = defineProps({
  descriptor: {
    type: Object as PropType<StringDescriptor>,
    required: true,
  }
})

const actualValue = computed({
  get() {
    return props.descriptor.actualValue
  },
  set(newValue) {
    emits('modifyDescriptor', props.descriptor.withValue(newValue))
  }
})
</script>