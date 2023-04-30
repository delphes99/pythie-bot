import "../src/common/assets/styles/index.css"

const preview = {
  parameters: {
    layout: "fullscreen",
    backgrounds: {
      default: "light",
    },
    actions: { argTypesRegex: "^on[A-Z].*" },
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/,
      },
    },
  },
  decorators: [(story) => ({
    components: { story },
    template: '<div class="light-theme bg-backgroundColor text-backgroundTextColor p-8"><story /></div>'
  })],
};

export default preview;

import { setup } from '@storybook/vue3';
import { createI18n } from 'vue-i18n';

const i18n = createI18n({});

setup((app) => {
  app.use(i18n);
});
