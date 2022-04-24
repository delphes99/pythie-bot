<template>
  <ui-table
    :data="statusesTableData"
    :empty-message="$t('common.noData')"
  />
</template>

<script lang="ts">
import { ConnectorEnum } from "@/common/components/common/connectorEnum"
import { ConnectorStatusEnum } from "@/common/components/common/connectorStatus"
import { ColumnDefinition } from "@/common/components/common/table/ColumnDefinition"
import { TableData } from "@/common/components/common/table/TableData"
import UiTable from "@/common/components/common/table/UiTable.vue"
import { defineComponent, inject, ref } from "vue"
import { useI18n } from "vue-i18n"

interface Status {
  name: ConnectorEnum
  status: ConnectorStatusEnum
  subStatus: Status[]
}

interface SubStatus {
  name: ConnectorEnum
  status: string
}

export default defineComponent({
  name: `DetailedConnectorStatus`,
  components: { UiTable },
  props: {
    connector: {
      type: String as () => ConnectorEnum,
      required: true,
    },
  },
  setup(props) {
    const { t } = useI18n()
    const backendUrl = inject("backendUrl") as string

    const statusesTableData = ref<TableData<SubStatus> | null>(null)

    async function getStatus() {
      const response = await fetch(`${backendUrl}/connectors/status`)
      const allStatus: Status[] = await response.json()
      const connectorStatus = allStatus.filter((s) => s.name === props.connector)[0]

      statusesTableData.value = new TableData(
        [
          {
            name: props.connector,
            status: t("connector.status." + connectorStatus.status),
          },
          ...connectorStatus.subStatus.map((sub) => ({
            name: sub.name,
            status: t("connector.status." + sub.status),
          })),
        ],
        [
          new ColumnDefinition<SubStatus>(
            t("connector.connectionName"),
            (data: SubStatus) => data.name,
          ),
          new ColumnDefinition<SubStatus>(
            t("connector.connectionStatus"),
            (data: SubStatus) => data.status,
          ),
        ],
      )
    }

    getStatus()

    setInterval(() => {
      getStatus()
    }, 2000)

    return {
      statusesTableData,
    }
  },
})
</script>