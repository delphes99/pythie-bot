<template>
  <list-by-tag :tag="descriptor.formFamily" :title="descriptor.description"/>
</template>

<script lang="ts" setup>
import {FormListDescriptor} from "@/common/dynamicForm/formList/form-list-descriptor";
import ListByTag from "@/common/dynamicForm/listByTag/ListByTag.vue";
import {PropType} from "vue";

const emits = defineEmits<{
  modifyDescriptor: [descriptor: FormListDescriptor]
}>()

const props = defineProps({
  descriptor: {
    type: Object as PropType<FormListDescriptor>,
    required: true,
  }
})

</script>