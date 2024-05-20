<template>
  <ListByTag :tag="descriptor.formFamily" :title="descriptor.description"/>
</template>

<script lang="ts" setup>
import ListByTag from "@/common/dynamicForm/displayForm/FormListByTag.vue";
import {FieldFormList} from "@/common/dynamicForm/field/formList/FieldFormList";
import {PropType} from "vue";

defineProps({
  descriptor: {
    type: Object as PropType<FieldFormList>,
    required: true,
  }
})

</script>