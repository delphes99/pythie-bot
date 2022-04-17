<template>
  <panel :title="$t('medias')">
    <table>
      <tr>
        <th>file name</th>
      </tr>
      <tr
        v-for="file in files"
        :key="file.fileName"
      >
        <td>
          {{ file.fileName }}
        </td>
      </tr>
    </table>
  </panel>
</template>

<script setup lang="ts">
import Panel from "@/common/components/common/Panel.vue"
import { Media } from "@/media/Media"
import MediasService from "@/media/MediasService"
import { inject, ref } from "vue"
import { useI18n } from "vue-i18n"

const { t } = useI18n()

const backendUrl = inject("backendUrl") as string

const mediaService = new MediasService(backendUrl)
const files = ref<Media[]>([])

mediaService.list().then((medias) => (files.value = medias))
</script>