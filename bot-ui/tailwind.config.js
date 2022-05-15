const defaultColors = require('tailwindcss/colors')

module.exports = {
  purge: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primaryColor: '#4B5563',
        primaryColorDark: '#4B5563',
        backgroundColor: defaultColors.white,
        backgroundColorDark: defaultColors.gray["700"],
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
}
