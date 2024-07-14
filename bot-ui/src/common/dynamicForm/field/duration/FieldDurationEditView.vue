<template>
  {{ field.description }}
  <ui-textfield
      :id="field.fieldName"
      v-model.number="actualHours"
      label="common.duration.hours"
  />
  <ui-textfield
      :id="field.fieldName"
      v-model.number="actualMinutes"
      label="common.duration.minutes"
  />
  <ui-textfield
      :id="field.fieldName"
      v-model.number="actualSeconds"
      label="common.duration.seconds"
  />
</template>

<script lang="ts" setup>
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue";
import {FieldDuration} from "@/common/dynamicForm/field/duration/FieldDuration";
import {PropType, ref} from "vue";

const emits = defineEmits<{
  modifyDescriptor: [descriptor: FieldDuration]
}>()

const props = defineProps({
  field: {
    type: Object as PropType<FieldDuration>,
    required: true,
  }
})

const actualHours = ref(props.field.actualValue.hours)

const actualMinutes = ref(props.field.actualValue.minutes)

const actualSeconds = ref(props.field.actualValue.seconds)

</script>