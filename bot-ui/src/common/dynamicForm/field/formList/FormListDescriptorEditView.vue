<template>
  <ListByTag :tag="descriptor.formFamily" :title="descriptor.description"/>
</template>

<script lang="ts" setup>
import ListByTag from "@/common/dynamicForm/displayForm/FormListByTag.vue";
import {FormListDescriptor} from "@/common/dynamicForm/field/formList/FormListDescriptor";
import {PropType} from "vue";

defineEmits<{
  modifyDescriptor: [descriptor: FormListDescriptor]
}>()

defineProps({
  descriptor: {
    type: Object as PropType<FormListDescriptor>,
    required: true,
  }
})

</script>