import "../src/common/assets/styles/index.css"

const preview = {
    globalTypes: {
        theme: {
            name: "Theme",
            description: "Theme",
            defaultValue: "light",
            toolbar: {
                icon: "photo",
                items: ['Light', 'Dark', 'Monochrome'],
                showName: true,
                dynamicTitle: true,
            }
        }
    },
    parameters: {
        layout: "centered",
        actions: {argTypesRegex: "^on[A-Z].*"},
        controls: {
            matchers: {
                color: /(background|color)$/i,
                date: /Date$/,
            },
        },
    },
    decorators: [(story, context) => ({
        components: {story},
        template: `<div class="${themeClass(context.globals.theme)} bg-backgroundColor text-backgroundTextColor w-full"><story /></div>`
    })],
};

function themeClass(theme) {
    return `${theme.toLowerCase()}-theme`
}

export default preview;

import {setup} from '@storybook/vue3';
import {createI18n} from 'vue-i18n';

const i18n = createI18n({});

setup((app) => {
    app.use(i18n);
});
