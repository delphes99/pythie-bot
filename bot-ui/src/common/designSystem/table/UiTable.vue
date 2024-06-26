<script lang="ts" setup generic="T">
import {UiTableInjectionKeys} from "@/common/designSystem/table/UiTable.injection";
import {UiTableColumnDefinition} from "@/common/designSystem/table/UiTableColumnDefinition";
import {PropType, provide, ref} from "vue"

defineProps({
  emptyMessage: {
    type: String,
    default: null,
  },
  data: {
    type: Array as PropType<T[]>,
    default: () => [],
  },
})

defineSlots<{
  default: (props: { item?: T }) => any;
}>();

const columns = ref<(UiTableColumnDefinition)[]>([]);

function registerColumn(column: UiTableColumnDefinition) {
  columns.value.push(column)
}

function unregisterColumn(columnId: string) {
  columns.value = columns.value.filter(column => column.id !== columnId)
}

provide(UiTableInjectionKeys.COLUMN_REGISTRATION, {
  registerColumn,
  unregisterColumn
})
</script>

<template>
  <div class="w-full">
    <div v-if="!data || data.length === 0">
      {{ emptyMessage || $t("common.noData") }}
    </div>
    <div v-else>
      <table class="w-full">
        <tr
            class="border-b border-primaryColor"
        >
          <th
              v-for="column in columns"
              :key="column.id"
              class="font-bold text-left text-backgroundTextColor"
          >
            {{ $t(column.headerName) }}
          </th>
        </tr>
        <tr
            v-for="(item, index) in data"
            :key="index"
            class="border-b border-secondaryColor text-backgroundTextColor"
        >
          <td
              v-for="column in columns"
              :key="column.id"
              class="p-2 text-left text-backgroundTextColor"
          >
            <template v-if="column.render">
              <component :is="column.render" :item="item"></component>
            </template>
            <template v-else-if="column.extractValue">
              {{ column.extractValue(item) }}
            </template>
          </td>
        </tr>
      </table>
      <template v-show="false">
        <slot></slot>
      </template>
    </div>
  </div>
</template>