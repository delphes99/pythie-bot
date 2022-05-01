<template>
  <div class="w-full">
    <div v-if="!props.data">
      {{ emptyMessage || $t("common.noData") }}
    </div>
    <div v-else>
      <table class="w-full">
        <tr
          class="border-b border-gray-200"
        >
          <th
            v-for="columnName in header"
            :key="columnName"
            class="font-bold text-left"
          >
            {{ columnName }}
          </th>
        </tr>
        <tr
          v-for="row in rows"
          :key="row.key"
          class="border-b border-gray-200"
        >
          <td
            v-for="cell in row.value"
            :key="cell.key"
            class="py-4"
          >
            {{ cell.value }}
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ColumnDefinition } from "@/common/components/common/table/ColumnDefinition"
import { TableData } from "@/common/components/common/table/TableData"
import { computed, PropType} from "vue"

const props = defineProps({
  emptyMessage: {
    type: String,
    nullable: true,
    default() {
      return null
    },
  },
  data: {
    type: Object as PropType<TableData<unknown>>,
    required: true,
    nullable: true,
  },
})

const header = computed(
  () =>
    props.data?.columns?.map(
      (definition: ColumnDefinition<unknown>) => definition.name,
    ) || [],
)
const rows = computed(() => {
  if (props.data) {
    const data = props.data
    return data.data.map((item, index) => {
      return {
        row: index,
        value: data.columns.map((column, index) => {
          return {
            key: index,
            value: column.display(item),
          }
        }),
      }
    })
  } else {
    return []
  }
})
</script>