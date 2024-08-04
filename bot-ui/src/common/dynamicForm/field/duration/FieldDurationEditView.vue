<template>
  {{ field.description }}
  <ui-textfield
      :id="field.fieldName"
      v-model.number="actualHours"
      label="common.duration.hours"
  />
  <ui-textfield
      :id="field.fieldName"
      v-model.number="actualMinutes"
      label="common.duration.minutes"
  />
  <ui-textfield
      :id="field.fieldName"
      v-model.number="actualSeconds"
      label="common.duration.seconds"
  />
</template>

<script lang="ts" setup>
import UiTextfield from "@/common/designSystem/form/textfield/UiTextfield.vue";
import {FieldString} from "@/common/dynamicForm/field/string/FieldString";
import {Duration} from "@/common/utils/Duration";
import {computed, PropType} from "vue";

defineProps({
  field: {
    type: Object as PropType<FieldString>,
    required: true,
  },
})
const model = defineModel<Duration>({required: true})

console.log(model.value)

const actualHours = computed<number>({
  get() {
    return model.value.hours
  },
  set(value: number) {
    model.value = model.value.withHours(value)
  }
})

const actualMinutes = computed<number>({
  get() {
    return model.value.minutes
  },
  set(value: number) {
    model.value = model.value.withMinutes(value)
  }
})

const actualSeconds = computed<number>({
  get() {
    return model.value.seconds
  },
  set(value: number) {
    model.value = model.value.withSeconds(value)
  }
})

</script>