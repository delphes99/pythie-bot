module.exports = {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primaryColor: 'var(--primary-color)',
        primaryColorHover: 'var(--primary-color-hover)',
        primaryTextColor: 'var(--primary-text-color)',
        secondaryColor: 'var(--secondary-color)',
        inputColor: 'var(--input-color)',
        inputTextColor: 'var(--input-color-hover)',
        backgroundColor: 'var(--background)',
        backgroundTextColor: 'var(--background-text-color)',
        titleColor: 'var(--title-color)',
      },
      maxHeight: {
        'screen-4/5': '80vh',
      },
      maxWidth: {
        'screen-4/5': '80vw',
      },
    },
  },
  plugins: [],
}
