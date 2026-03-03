const KEY = "tv_token";
const KEY_U = "tv_user";
const KEY_R = "tv_role";

export function setSession({ token, username, rol }) {
  localStorage.setItem(KEY, token);
  localStorage.setItem(KEY_U, username || "");
  localStorage.setItem(KEY_R, rol || "");
}

export function clearSession() {
  localStorage.removeItem(KEY);
  localStorage.removeItem(KEY_U);
  localStorage.removeItem(KEY_R);
}

export function getToken() { return localStorage.getItem(KEY); }
export function getUsername() { return localStorage.getItem(KEY_U) || ""; }
export function getRole() { return localStorage.getItem(KEY_R) || ""; }

export function isLoggedIn() { return !!getToken(); }