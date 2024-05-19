<template>
  {{ descriptor.description }}
  <ui-textfield
      :id="descriptor.fieldName"
      v-model.number="actualHours"
      label="common.duration.hours"
  />
  <ui-textfield
      :id="descriptor.fieldName"
      v-model.number="actualMinutes"
      label="common.duration.minutes"
  />
  <ui-textfield
      :id="descriptor.fieldName"
      v-model.number="actualSeconds"
      label="common.duration.seconds"
  />
</template>

<script lang="ts" setup>
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue";
import {FieldDuration} from "@/common/dynamicForm/field/duration/field-duration";
import {PropType, ref} from "vue";

const emits = defineEmits<{
  modifyDescriptor: [descriptor: FieldDuration]
}>()

const props = defineProps({
  descriptor: {
    type: Object as PropType<FieldDuration>,
    required: true,
  }
})

const actualHours = ref(props.descriptor.actualValue.hours)

const actualMinutes = ref(props.descriptor.actualValue.minutes)

const actualSeconds = ref(props.descriptor.actualValue.seconds)

</script>