const path = require("path");
module.exports = {
    stories: ["../src/**/*.stories.mdx", "../src/**/*.stories.@(js|jsx|ts|tsx)"],
    addons: [
        "@storybook/addon-links",
        "@storybook/addon-essentials",
        "storybook-tailwind-dark-mode",
        "@storybook/addon-mdx-gfm",
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