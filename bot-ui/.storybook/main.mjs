export default {
    stories: ["../src/**/*.stories.@(js|jsx|ts|tsx)"],
    addons: [
        "@storybook/addon-links",
        "@storybook/addon-essentials",
    ],
    framework: {
        name: "@storybook/vue3-vite",
        options: {}
    },
    core: {
        disableTelemetry: true
    },
    docs: {
        autodocs: true
    }
};