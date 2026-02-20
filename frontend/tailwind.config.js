/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          black:    '#1A1A1A',
          charcoal: '#333333',
          gray:     '#757575',
          light:    '#F5F5F5',
          white:    '#FFFFFF',
          cream:    '#FAF9F7',
          gold:     '#C8A96E',
          error:    '#D32F2F',
          success:  '#2E7D32',
        },
      },
      fontFamily: {
        sans: ['Inter', 'Noto Sans TC', 'Helvetica Neue', 'Arial', 'sans-serif'],
      },
      aspectRatio: {
        '3/4': '3 / 4',
      },
    },
  },
  plugins: [],
}
