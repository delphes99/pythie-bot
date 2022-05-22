const defaultColors = require('tailwindcss/colors')

module.exports = {
  purge: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primaryColor: '#1C658C',
        primaryColorDark: '#1C658C',
        primaryColorHover: '#0B2736',
        primaryColorHoverDark: '#0B2736',
        secondaryColor: '#398AB9',
        secondaryColorDark: '#398AB9',
        primaryTextColor: '#D8D2CB',
        primaryTextColorDark: '#D8D2CB',
        backgroundColor: '#FFFFFF',
        backgroundColorDark: defaultColors.gray["700"],
      },
    },
  },
  variants: {
    extend: {},
  },
  plugins: [],
}
