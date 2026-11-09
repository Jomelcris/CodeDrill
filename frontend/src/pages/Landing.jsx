import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { createUser, deleteUser, getUsers, updateUser } from '../api.js'
import { clearCurrentUser, getCurrentUser, setCurrentUser } from '../auth.js'

const emptyForm = { fullName: '', email: '', password: '', program: 'BSIT' }

export default function Landing() {
  const navigate = useNavigate()
  const currentUser = getCurrentUser()

  const [users, setUsers] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    loadUsers()
  }, [])

  async function loadUsers() {
    try {
      setUsers(await getUsers())
    } catch (err) {
      setError(err.message)
    }
  }

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  function logout() {
    clearCurrentUser()
    navigate('/login')
  }

  function startEdit(user) {
    setEditingId(user.userId)
    setForm({ fullName: user.fullName, email: user.email, password: '', program: user.program })
    setMessage('')
    setError('')
  }

  function cancelEdit() {
    setEditingId(null)
    setForm(emptyForm)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setMessage('')
    setError('')
    try {
      if (editingId === null) {
        await createUser(form)
        setMessage('User created.')
      } else {
        const updated = await updateUser(editingId, form)
        if (updated.userId === currentUser.userId) setCurrentUser(updated)
        setMessage('User updated.')
      }
      cancelEdit()
      loadUsers()
    } catch (err) {
      setError(err.message)
    }
  }

  async function handleDelete(user) {
    if (!window.confirm(`Delete ${user.fullName}?`)) return
    setMessage('')
    setError('')
    try {
      await deleteUser(user.userId)
      if (user.userId === currentUser.userId) {
        logout()
        return
      }
      if (editingId === user.userId) cancelEdit()
      setMessage('User deleted.')
      loadUsers()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="page">
      <header className="topbar">
        <strong>CodeDrill</strong>
        <div>
          <span className="muted">
            {currentUser.fullName} ({currentUser.program})
          </span>
          <button className="secondary" onClick={logout}>
            Log out
          </button>
        </div>
      </header>

      <main>
        <section className="card">
          <h2>Welcome, {currentUser.fullName}!</h2>
          <p>
            CodeDrill helps first-year BSIT and BSCS students practice C, Java, and C++ syntax
            through short drills.
          </p>
          <p className="muted">
            Software Increment 1: backend CRUD for the User entity. Drills, progress, and bookmarks
            will be added in the next increments.
          </p>
        </section>

        <section className="card">
          <h2>{editingId === null ? 'Add User' : `Edit User #${editingId}`}</h2>
          <form className="user-form" onSubmit={handleSubmit}>
            <input
              name="fullName"
              placeholder="Full name"
              value={form.fullName}
              onChange={handleChange}
              required
            />
            <input
              name="email"
              type="email"
              placeholder="Email"
              value={form.email}
              onChange={handleChange}
              required
            />
            <input
              name="password"
              type="password"
              placeholder={editingId === null ? 'Password (min 6 characters)' : 'New password (optional)'}
              value={form.password}
              onChange={handleChange}
              required={editingId === null}
            />
            <select name="program" value={form.program} onChange={handleChange}>
              <option value="BSIT">BSIT</option>
              <option value="BSCS">BSCS</option>
            </select>
            <button type="submit">{editingId === null ? 'Add' : 'Save'}</button>
            {editingId !== null && (
              <button type="button" className="secondary" onClick={cancelEdit}>
                Cancel
              </button>
            )}
          </form>
          {message && <p className="success">{message}</p>}
          {error && <p className="error">{error}</p>}
        </section>

        <section className="card">
          <h2>Users</h2>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Full name</th>
                <th>Email</th>
                <th>Program</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.userId}>
                  <td>{u.userId}</td>
                  <td>{u.fullName}</td>
                  <td>{u.email}</td>
                  <td>{u.program}</td>
                  <td>{new Date(u.createdAt).toLocaleString()}</td>
                  <td>
                    <button className="secondary" onClick={() => startEdit(u)}>
                      Edit
                    </button>
                    <button className="danger" onClick={() => handleDelete(u)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
              {users.length === 0 && (
                <tr>
                  <td colSpan="6" className="muted">
                    No users yet.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </section>
      </main>
    </div>
  )
}
