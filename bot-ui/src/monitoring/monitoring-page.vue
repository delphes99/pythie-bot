<template>
  <ui-panel title="monitoring.title">
    <ui-table
        :data="events"
    >
      <ui-table-column header-name="common.date" property-name="date"></ui-table-column>
      <ui-table-column header-name="monitoring.events.type" v-slot="{item}">
        {{ item?.event?.event?.type }}
      </ui-table-column>
      <ui-table-column header-name="common.actions" v-slot="{item}">
        <ui-button label="common.view" @on-click="display(item)"></ui-button>
      </ui-table-column>
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
import UiButton from "@/common/designSystem/button/ui-button.vue";
import UiModal from "@/common/designSystem/modal/ui-modal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import UiPanel from "@/common/designSystem/panel/ui-panel.vue";
import UiTableColumn from "@/common/designSystem/table/ui-table-column.vue";
import UiTable from "@/common/designSystem/table/ui-table.vue";
import {autowired} from "@/common/utils/injection.util";
import {MonitoringEvent} from "@/monitoring/monitoring.service";
import {ref} from "vue";

const itemToDisplay = ref<MonitoringEvent | null>(null)

function display(item: MonitoringEvent) {
  itemToDisplay.value = item
  open()
}

const statisticService = autowired(AppInjectionKeys.MONITORING_SERVICE)
const events = await statisticService.getStatistics()

const {isOpen, open} = useModal()
</script>