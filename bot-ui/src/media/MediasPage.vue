<template>
  <UiPanel title="medias.add">
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
      <UiTextfield
          id="filename"
          v-model="filename"
          label="medias.filename"
      />
      <br>
      <UiButton
          label="common.validate"
          @on-click="upload"
      />
    </form>
  </UiPanel>
  <UiPanel title="medias.list">
    <UiTable
        :data="files"
        :empty-message="$t('medias.noFiles')"
    >
      <UiTableColumn header-name="medias.filename" property-name="fileName"/>
    </UiTable>
  </UiPanel>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue"
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue"
import UiPanel from "@/common/designSystem/panel/UiPanel.vue"
import UiTable from "@/common/designSystem/table/UiTable.vue"
import UiTableColumn from "@/common/designSystem/table/UiTableColumn.vue";
import {autowired} from "@/common/utils/Injection.util";
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