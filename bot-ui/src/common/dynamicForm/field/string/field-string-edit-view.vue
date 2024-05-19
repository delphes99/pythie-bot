<template>
  <ui-textfield
      :id="descriptor.fieldName"
      v-model="actualValue"
      :label="descriptor.description"
  />
</template>

<script lang="ts" setup>
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue";
import {FieldString} from "@/common/dynamicForm/field/string/field-string";
import {PropType, ref} from "vue";

const props = defineProps({
  descriptor: {
    type: Object as PropType<FieldString>,
    required: true,
  }
})

const actualValue = ref(props.descriptor.actualValue)
</script>