import { Navigate, Route, Routes } from 'react-router-dom'
import Login from './pages/Login.jsx'
import Landing from './pages/Landing.jsx'
import { getCurrentUser } from './auth.js'

function RequireAuth({ children }) {
  return getCurrentUser() ? children : <Navigate to="/login" replace />
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/"
        element={
          <RequireAuth>
            <Landing />
          </RequireAuth>
        }
      />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
