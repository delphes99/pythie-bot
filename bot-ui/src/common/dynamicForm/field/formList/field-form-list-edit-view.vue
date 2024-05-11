<template>
  <list-by-tag :tag="descriptor.formFamily" :title="descriptor.description"/>
</template>

<script lang="ts" setup>
import ListByTag from "@/common/dynamicForm/field/formList/form-list-by-tag.vue";
import {FormListDescriptor} from "@/common/dynamicForm/field/formList/form-list-descriptor";
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