<template>
  <ui-panel title="monitoring.title">
    <ui-table
        :data="tableData"
        :columns="[$t('common.date'), $t('monitoring.events.type'), $t('common.actions')]"
    >
      <template #default="{item}">
        <ui-table-column :item="item">
          <template #default="{item}">
            {{ item.date }}
          </template>
        </ui-table-column>
        <ui-table-column :item="item">
          <template #default="{item}">
            {{ item.event.event.type }}
          </template>
        </ui-table-column>
        <ui-table-column :item="item">
          <template #default="{item}">
            <ui-button label="common.view" @on-click="display(item)"></ui-button>
          </template>
        </ui-table-column>
      </template>
    </ui-table>
  </ui-panel>
  <ui-modal title="monitoring.events.title" v-model:is-open="isOpen">
    <div v-if="itemToDisplay">
      <json-viewer :value="itemToDisplay.event.event"></json-viewer>
    </div>
  </ui-modal>
</template>

<script setup lang="ts">
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButton from "@/common/components/common/button/UiButton.vue";
import UiModal from "@/common/components/common/modal/UiModal.vue";
import {useModal} from "@/common/components/common/modal/useModal";
import UiPanel from "@/common/components/common/panel/UiPanel.vue";
import {TableData} from "@/common/components/common/table/TableData";
import UiTableColumn from "@/common/components/common/table/ui-table-column.vue";
import UiTable from "@/common/components/common/table/ui-table.vue";
import {autowired} from "@/common/utils/injection.util";
import {MonitoringEvent} from "@/monitoring/monitoringService";
import {ref} from "vue";

const itemToDisplay = ref<MonitoringEvent | null>(null)

function display(item: MonitoringEvent) {
  itemToDisplay.value = item
  open()
}

const statisticService = autowired(AppInjectionKeys.MONITORING_SERVICE)

const tableData = new TableData(
    await statisticService.getStatistics(),
    []
)

const {isOpen, open} = useModal()
</script>