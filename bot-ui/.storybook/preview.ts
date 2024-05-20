import "@/common/style/index.css"
import {Preview, setup} from '@storybook/vue3';
import {createPinia} from "pinia";
import {App} from 'vue';
import {createI18n} from 'vue-i18n';
import Vue3Toasity from "vue3-toastify";

const pinia = createPinia();

setup((app: App) => {
    app.use(pinia);
});

const preview: Preview = {
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
        controls: {
            matchers: {
                color: /(background|color)$/i,
                date: /Date$/,
            },
        },
    },
    decorators: [(story, context) => ({
        components: {story},
        template: `
          <div class="${themeClass(context.globals.theme)} bg-backgroundColor text-backgroundTextColor w-full">
            <story/>
          </div>`
    })],
};

function themeClass(theme) {
    return `${theme.toLowerCase()}-theme`
}

export default preview;

const i18n = createI18n({});

setup((app) => {
    app.use(i18n);
    app.use(Vue3Toasity)
});
