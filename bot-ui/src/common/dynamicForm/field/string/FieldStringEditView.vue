<template>
  <UiTextfield
      :id="field.fieldName"
      v-model="model"
      :label="field.description"
  />
</template>

<script lang="ts" setup>
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue";
import {FieldString} from "@/common/dynamicForm/field/string/FieldString";
import {PropType} from "vue";

defineProps({
  field: {
    type: Object as PropType<FieldString>,
    required: true,
  },
})
const model = defineModel<string>({required: true})
</script>