<template>
  <el-table
    :data="statuses"
    :empty-text="$t('common.noData')"
  >
    <el-table-column
      prop="name"
      :label="$t('connector.connectionName')"
    />
    <el-table-column
      prop="status"
      :label="$t('connector.connectionStatus')"
    />
  </el-table>
</template>

<script lang="ts">
import { ConnectorEnum } from "@/common/components/common/connectorEnum"
import { ConnectorStatusEnum } from "@/common/components/common/connectorStatus"
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
  props: {
    connector: {
      type: String as () => ConnectorEnum,
      required: true,
    },
  },
  setup(props) {
    const { t } = useI18n()
    const backendUrl = inject("backendUrl") as string
    const statuses = ref<SubStatus[]>()

    async function getStatus() {
      const response = await fetch(`${backendUrl}/connectors/status`)
      const allStatus: Status[] = await response.json()
      const connectorStatus = allStatus.filter((s) => s.name === props.connector)[0]
      statuses.value = [
        {
          name: props.connector,
          status: t("connector.status." + connectorStatus.status),
        },
        ...connectorStatus.subStatus.map((sub) => ({
          name: sub.name,
          status: t("connector.status." + sub.status),
        })),
      ]
    }

    setInterval(() => {
      getStatus()
    }, 2000)

    return {
      statuses,
    }
  },
})
</script>
