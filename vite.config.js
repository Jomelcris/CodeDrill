import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// The React dev server runs on 5173 and forwards /api calls to the Spring backend on 8080.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
