<template>
    <h1>{{ channelName }}</h1>
    <div class="w-full flex flex-row">
        <router-link
                :to="`/twitch/${channelName}/rewards`"
                active-class="active-link"
                class="link"
        >
            Rewards
        </router-link>
        <router-link
                :to="`/twitch/${channelName}/features`"
                active-class="active-link"
                class="link"
        >
            Features
        </router-link>
    </div>
    <div class="p-4 bg-gray-300 bg-black">
        <router-view/>
    </div>
</template>

<script lang="ts" setup>
defineProps({
    channelName: {
        type: String,
        required: true,
    },
})
</script>

<style lang="css" scoped>
.link {
    @apply mr-2 px-3 bg-gray-300 text-center;
}

.active-link {
    @apply bg-gray-500;
}
</style>
