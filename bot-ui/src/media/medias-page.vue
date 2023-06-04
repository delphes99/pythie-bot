<template>
  <ui-panel title="medias.add">
    <form>
      <label
          class="block text-titleColor text-sm font-semibold mb-2"
          for="file"
      >
        {{ $t("medias.file") }}
      </label>
      <input
          id="file"
          ref=""
          name="file"
          type="file"
          @change="selectFile"
      >
      <ui-textfield
          id="filename"
          v-model="filename"
          label="medias.filename"
      />
      <br>
      <ui-button
          label="common.validate"
          @on-click="upload"
      />
    </form>
  </ui-panel>
  <ui-panel title="medias.list">
    <ui-table
        :data="files"
        :empty-message="$t('medias.noFiles')"
    >
      <ui-table-column header-name="medias.filename" property-name="fileName"/>
    </ui-table>
  </ui-panel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/app.injection.keys";
import UiButton from "@/common/designSystem/button/ui-button.vue"
import UiTextfield from "@/common/designSystem/form/textfield/ui-textfield.vue"
import UiPanel from "@/common/designSystem/panel/ui-panel.vue"
import UiTableColumn from "@/common/designSystem/table/ui-table-column.vue";
import UiTable from "@/common/designSystem/table/ui-table.vue"
import {autowired} from "@/common/utils/injection.util";
import {ref} from "vue"
import {useI18n} from "vue-i18n"

const {t} = useI18n()

const mediaService = autowired(AppInjectionKeys.MEDIA_SERVICE)

const filename = ref<string>("")

const selectedFile = ref()
const selectFile = (event: any) => {
  let file = event.target.files[0]
  selectedFile.value = file

  filename.value = file.name
}

const upload = () => {
  mediaService.upload(filename.value, selectedFile.value)
}

const files = ref(await mediaService.list())
</script>