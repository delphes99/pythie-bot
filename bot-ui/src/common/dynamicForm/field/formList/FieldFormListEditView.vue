<template>
  <fieldset class="border p-2">
    <legend v-if="title">{{ title }}</legend>
    <UiButton label="common.add"
              @click="openAddForm"
              :type="UiButtonType.Primary"/>
    <fieldset v-for="subForm in subForms" :key="subForm.id" class="border p-2">
      <legend>{{ subForm.type }}</legend>
      <DisplayDynamicForm :form="subForm" :is="subForm.id"/>

      <UiButton label="common.delete"
                :type="UiButtonType.Warning"
                @click="deleteForm(subForm.id)"/>
    </fieldset>
  </fieldset>
  <UiModal title="common.add" v-model:is-open="isAddFormOpened">
    <UiSelect v-model="selectedType" :options="subFormTypesOptions" :label="tag"/>
    <UiButton
        label="common.add"
        @click="addForm"
        :type="UiButtonType.Primary"/>
  </UiModal>
</template>

<script lang="ts" setup>
import {AppInjectionKeys} from "@/AppInjectionKeys";
import UiButton from "@/common/designSystem/button/UiButton.vue";
import UiButtonType from "@/common/designSystem/button/UiButtonType";
import UiSelect from "@/common/designSystem/form/select/UiSelect.vue";
import {UiSelectOption} from "@/common/designSystem/form/select/UiSelectOption";
import UiModal from "@/common/designSystem/modal/UiModal.vue";
import {useModal} from "@/common/designSystem/modal/useModal";
import DisplayDynamicForm from "@/common/dynamicForm/displayForm/DisplayDynamicForm.vue";
import {DynamicForm} from "@/common/dynamicForm/DynamicForm";
import {DynamicFormType} from "@/common/dynamicForm/DynamicFormType";
import {FieldFormList} from "@/common/dynamicForm/field/formList/FieldFormList";
import {autowired} from "@/common/utils/Injection.util";
import {PropType, ref} from "vue";

const props = defineProps({
  field: {
    type: Object as PropType<FieldFormList>,
    required: true,
  }
})

const subForms = defineModel<DynamicForm[]>({required: true})
const tag = props.field.formFamily
const title = props.field.description

const {isOpen: isAddFormOpened, open: openAddForm, close: closeAddForm} = useModal()
const selectedType = ref<DynamicFormType | null>(null)

const dynamicFormService = autowired(AppInjectionKeys.DYNAMIC_FORM_SERVICE)

const subFormTypesOptions = UiSelectOption.forString(await dynamicFormService.getFormsByTag(tag))

async function addForm() {
  if (selectedType.value) {
    const description = await dynamicFormService.getForm(selectedType.value)
    subForms.value = [
      ...subForms.value,
      new DynamicForm(
          description.type,
          description.fields
      ),
    ]
    closeAddForm()
  }
}

function deleteForm(id: string) {
  subForms.value = subForms.value.filter(item => item.id !== id)
}

</script>