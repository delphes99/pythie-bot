<template>
  <UiPanel title="monitoring.title">
    <UiTable
        :data="events"
    >
      <UiTableColumn header-name="common.date"
                     property-name="date"
                     :transform="ColumnTransformation.DATE"></UiTableColumn>
      <UiTableColumn header-name="monitoring.events.id"
                     property-name="event.incomingEvent.id">
      </UiTableColumn>
      <UiTableColumn header-name="monitoring.events.type"
                     property-name="event.incomingEvent.data.type">
      </UiTableColumn>
      <UiTableColumn header-name="monitoring.events.isReplay"
                     property-name="event.incomingEvent.replay"
                     :transform="item => item != undefined">
      </UiTableColumn>
      <UiTableColumn header-name="common.actions" v-slot="{item}">
        <ui-button label="common.view" @on-click="display(item)"></ui-button>
        <ui-button label="monitoring.events.replay" :type="UiButtonType.Warning" @on-click="replay(item)"></ui-button>
      </UiTableColumn>
    </UiTable>
  </UiPanel>
  <UiModal title="monitoring.events.title" v-model:is-open="isOpen">
    <div v-if="itemToDisplay">
      <json-viewer :value="itemToDisplay.event.incomingEvent"></json-viewer>
    </div>
  </UiModal>
</template>

<script setup lang="ts">
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiButtonType from "@/common/designSystem/button/UiButtonType";
import UiModal from "@/common/designSystem/modal/UiModal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import UiPanel from "@/common/designSystem/panel/UiPanel.vue";
import {ColumnTransformation} from "@/common/designSystem/table/ColumnTransformation";
import UiTable from "@/common/designSystem/table/UiTable.vue";
import UiTableColumn from "@/common/designSystem/table/UiTableColumn.vue";
import {autowired} from "@/common/utils/Injection.util";
import {MonitoringEvent} from "@/monitoring/MonitoringService";
import {ref} from "vue";

const monitoringService = autowired(AppInjectionKeys.MONITORING_SERVICE)

const itemToDisplay = ref<MonitoringEvent | null>(null)

function display(item: MonitoringEvent) {
  itemToDisplay.value = item
  open()
}

function replay(item: MonitoringEvent) {
  monitoringService.replay(item)
}

const events = await monitoringService.getStatistics()

const {isOpen, open} = useModal()
</script>