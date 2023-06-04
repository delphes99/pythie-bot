<script setup lang="ts" generic="T">
import {UiTabbleColumnDefinition} from "@/common/designSystem/table/ui-tabble.column.definition";
import {UiTableInjectionKeys} from "@/common/designSystem/table/ui-table.injection";
import {autowired} from "@/common/utils/injection.util";
import {getCurrentInstance, onBeforeUnmount} from "vue";

const {registerColumn, unregisterColumn} = autowired(UiTableInjectionKeys.COLUMN_REGISTRATION);

const props = defineProps({
  headerName: {
    type: String,
    required: true,
  },
  propertyName: {
    type: String,
    required: false,
  }
});

defineSlots<{
  default: (props: { item?: T }) => any;
}>();

const columnDefinition = new UiTabbleColumnDefinition(
    props.headerName,
    getCurrentInstance()!.slots.default,
    props.propertyName,
)

registerColumn(columnDefinition)

onBeforeUnmount(() => unregisterColumn(columnDefinition.id));

</script>

<template>
  <slot/>
</template>