<template>
  <fieldset class="border p-2">
    <legend v-if="props.title">{{ props.title }}</legend>
    <UiButton label="common.add"
              @click="openAddForm"
              :type="UiButtonType.Primary"/>
    <fieldset v-for="subForm in subForms" :key="subForm.id" class="border p-2">
      <legend>{{ subForm.type }}</legend>
      <div v-for="field in subForm.fields" :key="field.fieldName">
        <component :is="field.viewComponent()"
                   v-model="field.actualValue"
                   :field="field"
                   :label="field.description"
        />
      </div>
      <UiButton label="common.delete"
                :type="UiButtonType.Warning"
                @click="deleteForm(subForm.id)"/>
    </fieldset>
  </fieldset>
  <UiModal title="common.add" v-model:is-open="isAddFormOpened">
    <UiSelect v-model="selectedType" :options="subFormTypes" :label="tag"/>
    <UiButton
        label="common.add"
        @click="addForm"
        :type="UiButtonType.Primary"/>
  </UiModal>
</template>

<script setup lang="ts">
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiButtonType from "@/common/designSystem/button/UiButtonType";
import UiSelect from "@/common/designSystem/form/select/UiSelect.vue";
import {UiSelectOption} from "@/common/designSystem/form/select/UiSelectOption";
import UiModal from "@/common/designSystem/modal/UiModal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import {DynamicForm} from "@/common/dynamicForm/DynamicForm";
import DynamicFormService from "@/common/dynamicForm/DynamicFormService";
import {DynamicFormType} from "@/common/dynamicForm/DynamicFormType";
import {autowired} from "@/common/utils/Injection.util";
import {ref} from "vue";

const props = defineProps({
  tag: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    default: null,
  }
})

const {isOpen: isAddFormOpened, open: openAddForm, close: closeAddForm} = useModal()
const selectedType = ref<DynamicFormType | null>(null)
const subFormTypes = ref<UiSelectOption<DynamicFormType>[]>([])
const subForms = ref<DynamicForm[]>([])

const backendUrl = autowired(AppInjectionKeys.BACKEND_URL)
const dynamicFormService = new DynamicFormService(backendUrl)

dynamicFormService.getFormsByTag(props.tag).then(itemsType => {
  subFormTypes.value = UiSelectOption.forString(itemsType)
})

async function addForm() {
  if (selectedType.value) {
    const description = await dynamicFormService.getForm(selectedType.value)
    subForms.value = [
      ...subForms.value,
      new DynamicForm(
          description.type,
          description.fields
      )
    ]
    closeAddForm()
  }
}

function deleteForm(id: string) {
  subForms.value = subForms.value.filter(item => item.id !== id)
}
</script>