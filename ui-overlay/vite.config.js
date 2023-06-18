import vue from "@vitejs/plugin-vue"

import { defineConfig } from "vite"

const path = require("path")

// https://vitejs.dev/config/
export default defineConfig({
  base: "",
  server: {
    port: 8083,
  },
  plugins: [vue()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
})