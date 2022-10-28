import "../src/common/assets/styles/index.css"

export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*" },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
  backgrounds: { disable: true },
}

export const decorators = [(story) => ({
  components: { story },
  template: '<div class="bg-backgroundColor text-backgroundTextColor p-8"><story /></div>'
})];

import { app } from '@storybook/vue3'
import { createI18n } from 'vue-i18n';

const i18n = createI18n({});

app.use(i18n)
