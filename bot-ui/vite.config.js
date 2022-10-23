import vue from "@vitejs/plugin-vue"

import { defineConfig } from "vite"

const path = require("path")

// https://vitejs.dev/config/
export default defineConfig({
  base: "",
  server: {
    port: 8082,
  },
  plugins: [vue()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  define: {
    __VUE_I18N_FULL_INSTALL__: true,
    __VUE_I18N_LEGACY_API__: false,
    __INTLIFY_PROD_DEVTOOLS__: false,
  },
})