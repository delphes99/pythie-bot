<script setup lang="ts" generic="T">
import {UiTableInjectionKeys} from "@/common/designSystem/table/UiTable.injection";
import {getExtractColumnValue} from "@/common/designSystem/table/UiTableColumn.extract";
import {TransformValue, UiTableColumnDefinition} from "@/common/designSystem/table/UiTableColumnDefinition";
import {autowired} from "@/common/utils/Injection.util";
import {getCurrentInstance, onBeforeUnmount, PropType} from "vue";

const {registerColumn, unregisterColumn} = autowired(UiTableInjectionKeys.COLUMN_REGISTRATION);

const props = defineProps({
  headerName: {
    type: String,
    required: true,
  },
  propertyName: {
    type: String,
    required: false,
  },
  transform: {
    type: Function as PropType<TransformValue>,
    required: false,
  }
});

defineSlots<{
  default: (props: { item?: T }) => any;
}>();

const columnDefinition = new UiTableColumnDefinition(
    props.headerName,
    getCurrentInstance()!.slots.default,
    getExtractColumnValue(props.propertyName, props.transform),
)

registerColumn(columnDefinition)

onBeforeUnmount(() => unregisterColumn(columnDefinition.id));

</script>

<template>
  <slot/>
</template>