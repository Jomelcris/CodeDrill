// Small helper for calling the Spring backend (requests to /api are proxied to port 8080).
async function request(path, options = {}) {
  let res
  try {
    res = await fetch(path, {
      headers: { 'Content-Type': 'application/json' },
      ...options,
    })
  } catch {
    throw new Error('Cannot reach the server. Make sure the backend is running on port 8080.')
  }

  if (res.status === 204) return null

  let data = null
  try {
    data = await res.json()
  } catch {
    // response had no JSON body
  }

  if (!res.ok) {
    if (data && data.message) throw new Error(data.message)
    if (res.status >= 500) {
      throw new Error('Cannot reach the server. Make sure the backend and MySQL are running.')
    }
    throw new Error(`Request failed (${res.status})`)
  }
  return data
}

export const login = (email, password) =>
  request('/api/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) })

export const getUsers = () => request('/api/users')

export const createUser = (user) =>
  request('/api/users', { method: 'POST', body: JSON.stringify(user) })

export const updateUser = (id, user) =>
  request(`/api/users/${id}`, { method: 'PUT', body: JSON.stringify(user) })

export const deleteUser = (id) => request(`/api/users/${id}`, { method: 'DELETE' })
