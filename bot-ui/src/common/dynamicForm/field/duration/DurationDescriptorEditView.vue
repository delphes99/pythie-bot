<template>
  {{ descriptor.description }}
  <UiTextfield
      :id="descriptor.fieldName"
      v-model.number="actualHours"
      label="common.duration.hours"
  />
  <UiTextfield
      :id="descriptor.fieldName"
      v-model.number="actualMinutes"
      label="common.duration.minutes"
  />
  <UiTextfield
      :id="descriptor.fieldName"
      v-model.number="actualSeconds"
      label="common.duration.seconds"
  />
</template>

<script lang="ts" setup>
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue";
import {DurationDescriptor} from "@/common/dynamicForm/field/duration/DurationDescriptor";
import {computed, PropType} from "vue";

const emits = defineEmits<{
  modifyDescriptor: [descriptor: DurationDescriptor]
}>()

const props = defineProps({
  descriptor: {
    type: Object as PropType<DurationDescriptor>,
    required: true,
  }
})

const actualHours = computed({
  get() {
    return props.descriptor.actualValue.hours
  },
  set(newValue) {
    emits('modifyDescriptor', props.descriptor.withHours(newValue))
  }
})

const actualMinutes = computed({
  get() {
    return props.descriptor.actualValue.minutes
  },
  set(newValue) {
    emits('modifyDescriptor', props.descriptor.withMinutes(newValue))
  }
})

const actualSeconds = computed({
  get() {
    return props.descriptor.actualValue.seconds
  },
  set(newValue) {
    emits('modifyDescriptor', props.descriptor.withSeconds(newValue))
  }
})

</script>