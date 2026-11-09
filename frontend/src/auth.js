// Keeps the logged-in user in sessionStorage (cleared when the tab is closed).
const KEY = 'codedrill_user'

export function getCurrentUser() {
  try {
    return JSON.parse(sessionStorage.getItem(KEY))
  } catch {
    return null
  }
}

export function setCurrentUser(user) {
  sessionStorage.setItem(KEY, JSON.stringify(user))
}

export function clearCurrentUser() {
  sessionStorage.removeItem(KEY)
}
