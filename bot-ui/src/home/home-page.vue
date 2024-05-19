<template>
  <div class="home">
    Pythie
  </div>
  <display-dynamic-form name="twitch-send-message-form"/>
  <form-list-by-tag tag="feature"/>
</template>
<script setup lang="ts">
import DisplayDynamicForm from "@/common/dynamicForm/displayForm/display-dynamic-form.vue";
import FormListByTag from "@/common/dynamicForm/displayForm/form-list-by-tag.vue";
</script>